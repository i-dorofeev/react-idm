package com.dorofeev.sandbox.reactidm.core

import java.io.File
import java.util

import org.identityconnectors.common.IOUtil
import org.identityconnectors.common.security.GuardedString
import org.identityconnectors.framework.api.operations.{SchemaApiOp, SearchApiOp, TestApiOp}
import org.identityconnectors.framework.api.{ConnectorFacadeFactory, ConnectorInfo, ConnectorInfoManagerFactory, ConnectorKey}
import org.identityconnectors.framework.common.objects.{ConnectorObject, ObjectClass, ResultsHandler, SearchResult}
import org.identityconnectors.framework.spi.SearchResultsHandler

import scala.collection.JavaConverters._

object Main extends App {

  println("hello")

  val factory = ConnectorInfoManagerFactory.getInstance
  val bundleUrl = IOUtil.makeURL(new File("C:\\Users\\Ilya\\dev\\sandbox\\react-idm\\connector-ldap\\target"), "connector-ldap-1.4.4-SNAPSHOT.jar")
  val manager = factory.getLocalManager(bundleUrl)
  val connectorInfos = manager.getConnectorInfos

  val connectorKey = new ConnectorKey("com.evolveum.polygon.connector-ldap", "1.4.4-SNAPSHOT", "com.evolveum.polygon.connector.ldap.LdapConnector")
  val connectorInfo = manager.findConnectorInfo(connectorKey)

  printConnectorInfo(connectorInfo)

  val apiConfig = connectorInfo.createDefaultAPIConfiguration
  val properties = apiConfig.getConfigurationProperties
  properties.getPropertyNames.iterator().asScala
      .map(propName => properties.getProperty(propName))
      //.filter(prop => prop.isRequired)
      .foreach(p => println(p.getName + "[" + p.getType + "]"))

  properties.setPropertyValue("host", "192.168.99.100")
  properties.setPropertyValue("port", 389)
  properties.setPropertyValue("bindDn", "cn=admin,dc=react,dc=org")
  properties.setPropertyValue("bindPassword", new GuardedString("1qaz@WSX".toCharArray))
  properties.setPropertyValue("baseContext", "dc=react,dc=org")

  val conn = ConnectorFacadeFactory.getInstance.newInstance(apiConfig)
  conn.validate

  conn.getSupportedOperations.iterator.asScala
    .foreach(op => println(op))

  val testApiOp = conn.getOperation(classOf[TestApiOp]).asInstanceOf[TestApiOp]
  testApiOp.test()

  val schemaApiOp = conn.getOperation(classOf[SchemaApiOp]).asInstanceOf[SchemaApiOp]
  val schema = schemaApiOp.schema()

  val searchApiOp = conn.getOperation(classOf[SearchApiOp]).asInstanceOf[SearchApiOp]

  val resourceObjects = new util.ArrayList[AbstractResourceObject]
  searchApiOp.search(new ObjectClass("inetOrgPerson"), null,
    new SearchResultsHandler {
      override def handle(connectorObject: ConnectorObject): Boolean = {
        println(connectorObject)
        resourceObjects.add(ResourceObject(connectorObject.getUid.getUidValue, connectorObject.getObjectClass.getObjectClassValue))
        true
      }

      override def handleResult(searchResult: SearchResult): Unit = {
        println(searchResult)
      }
    }, null)

  searchApiOp.search(new ObjectClass("groupOfUniqueNames"), null,
    new SearchResultsHandler {
      override def handle(connectorObject: ConnectorObject): Boolean = {
        println(connectorObject)
        resourceObjects.add(ResourceObject(connectorObject.getUid.getUidValue, connectorObject.getObjectClass.getObjectClassValue))
        true
      }

      override def handleResult(searchResult: SearchResult): Unit = {
        println(searchResult)
      }
    }, null)

  println(resourceObjects)

  println("goodbye")

  def printConnectorInfo(ci: ConnectorInfo): Unit = {
    println("Key: " + ci.getConnectorKey)
    println("Category: " + ci.getConnectorCategory)
    println("Display name: " + ci.getConnectorDisplayName)
  }

  abstract class AbstractResourceObject

  case class ResourceObject(id: String, objectClass: String) extends AbstractResourceObject
  case class ResourceAssociation(subjectId: String, objectId: String) extends AbstractResourceObject

}
