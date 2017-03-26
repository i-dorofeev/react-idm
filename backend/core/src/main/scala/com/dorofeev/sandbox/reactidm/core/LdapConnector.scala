package com.dorofeev.sandbox.reactidm.core

import java.io.File

import com.dorofeev.sandbox.reactidm.core.Main.ResourceObject
import org.identityconnectors.common.IOUtil
import org.identityconnectors.common.security.GuardedString
import org.identityconnectors.framework.api._
import org.identityconnectors.framework.api.operations.{SearchApiOp, SyncApiOp}
import org.identityconnectors.framework.common.objects.SyncDeltaType.{CREATE_OR_UPDATE, DELETE}
import org.identityconnectors.framework.common.objects.{SyncDelta, _}
import org.identityconnectors.framework.spi.SearchResultsHandler

object LdapConnector {

  var connector: ConnectorFacade = _

  def initConnector(): Unit = {
    val factory = ConnectorInfoManagerFactory.getInstance
    val bundleUrl = IOUtil.makeURL(new File("C:\\Users\\Ilya\\dev\\sandbox\\react-idm\\connector-ldap\\target"), "connector-ldap-1.4.4-SNAPSHOT.jar")
    val manager = factory.getLocalManager(bundleUrl)
    //val connectorInfos = manager.getConnectorInfos

    val connectorKey = new ConnectorKey("com.evolveum.polygon.connector-ldap", "1.4.4-SNAPSHOT", "com.evolveum.polygon.connector.ldap.LdapConnector")
    val connectorInfo = manager.findConnectorInfo(connectorKey)

    //printConnectorInfo(connectorInfo)

    val apiConfig = connectorInfo.createDefaultAPIConfiguration
    val properties = apiConfig.getConfigurationProperties
    /*
    properties.getPropertyNames.iterator().asScala
      .map(propName => properties.getProperty(propName))
      .foreach(p => println(p.getName + "[" + p.getType + "]"))
      */

    properties.setPropertyValue("host", "192.168.99.100")
    properties.setPropertyValue("port", 389)
    properties.setPropertyValue("bindDn", "cn=admin,dc=react,dc=org")
    properties.setPropertyValue("bindPassword", new GuardedString("1qaz@WSX".toCharArray))
    properties.setPropertyValue("baseContext", "dc=react,dc=org")

    connector = ConnectorFacadeFactory.getInstance.newInstance(apiConfig)
  }

  def search(objectClass: String, onResourceObject: ResourceObject => Unit, onFinished: () => Unit): Unit = {
    val searchApiOp = connector.getOperation(classOf[SearchApiOp]).asInstanceOf[SearchApiOp]
    searchApiOp.search(new ObjectClass(objectClass), null,
      new SearchResultsHandler {
        override def handle(connectorObject: ConnectorObject): Boolean = {
          onResourceObject(ResourceObject(connectorObject.getUid.getUidValue, connectorObject.getName.getNameValue, connectorObject.getObjectClass.getObjectClassValue))
          true
        }

        override def handleResult(searchResult: SearchResult): Unit = {
          onFinished()
        }
      }, null)
  }

  def getLatestSyncToken(objectClass: String): SyncToken = {
    val syncApiOp = connector.getOperation(classOf[SyncApiOp]).asInstanceOf[SyncApiOp]
    syncApiOp.getLatestSyncToken(new ObjectClass(objectClass))
  }

  def sync(objectClass: String, syncToken: SyncToken, synchronizationEventHandler: SynchronizationEventHandler): SyncToken = {
    val syncApiOp = connector.getOperation(classOf[SyncApiOp]).asInstanceOf[SyncApiOp]
    syncApiOp.sync(new ObjectClass(objectClass), syncToken, new SyncResultsHandler {
      override def handle(syncDelta: SyncDelta): Boolean = {
        syncDelta.getDeltaType match {
          case CREATE_OR_UPDATE =>
            synchronizationEventHandler.onCreateOrUpdate(asResourceObject(syncDelta))

          case DELETE =>
            synchronizationEventHandler.onDelete(asResourceObject(syncDelta))
        }

        true
      }
    }, null)
  }

  private def asResourceObject(syncDelta: SyncDelta) = ResourceObject(syncDelta.getUid.getUidValue, "", syncDelta.getObjectClass.getObjectClassValue)

  def printConnectorInfo(ci: ConnectorInfo): Unit = {
    println("Key: " + ci.getConnectorKey)
    println("Category: " + ci.getConnectorCategory)
    println("Display name: " + ci.getConnectorDisplayName)
  }
}

abstract class SynchronizationEventHandler {

  def onCreateOrUpdate(resourceObject: ResourceObject)
  def onDelete(resourceObject: ResourceObject)
}
