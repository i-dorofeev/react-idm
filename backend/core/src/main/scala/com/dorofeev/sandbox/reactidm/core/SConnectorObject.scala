package com.dorofeev.sandbox.reactidm.core

import org.identityconnectors.framework.common.objects._

import scala.collection.convert.Wrappers.{JListWrapper, JSetWrapper}

case class SObjectClass(value: String)

sealed abstract class SAttribute(name: String)

case class SAttributeString(name: String, values: List[String])
  extends SAttribute(name)

case class SAttributeInt(name: String, values: List[Int])
  extends SAttribute(name)

case class SAttributeUid(value: String, revision: String, nameHint: SAttributeName)
  extends SAttribute(Uid.NAME)

case class SAttributeName(value: String)
  extends SAttribute(Name.NAME)

case class SConnectorObject(objectClass: SObjectClass, attributes: Set[SAttribute]) {

  def uid: SAttributeUid = attributes collect { case a:SAttributeUid => a } head
  def name: SAttributeName = attributes collect { case a:SAttributeName => a } head
}

object SAttributeUid {
  def apply(uid: Uid): SAttributeUid = {
    new SAttributeUid(uid.getUidValue, uid.getRevision,
      if (uid.getNameHint != null) SAttributeName(uid.getNameHint) else null)
  }
}

object SAttributeName {
  def apply(name: Name): SAttributeName = {
    new SAttributeName(name.getNameValue)
  }
}

object SAttributeString {
  def apply(attr: Attribute): SAttributeString = {
    new SAttributeString(attr.getName, JListWrapper(attr.getValue).map({value => value.asInstanceOf[String]}).toList)
  }
}

object SAttributeInt {
  def apply(attr: Attribute): SAttributeInt = {
    new SAttributeInt(attr.getName, JListWrapper(attr.getValue).map({value => value.asInstanceOf[Int]}).toList)
  }
}

object SObjectClass {
  def apply(objectClass: ObjectClass): SObjectClass = {
    new SObjectClass(objectClass.getObjectClassValue)
  }
}

object SConnectorObject {

  private val classString = classOf[String]
  private val classInt = classOf[Int]

  def apply(obj: ConnectorObject)(implicit schema: Schema): SConnectorObject = {
    val objectClassInfo = schema.findObjectClassInfo(obj.getObjectClass.getObjectClassValue)
    val attrMap = JSetWrapper(objectClassInfo.getAttributeInfo).map({ ai => ai.getName -> ai }).toMap
    new SConnectorObject(
      SObjectClass(obj.getObjectClass),
      JSetWrapper(obj.getAttributes).map {
        case uidAttr: Uid => SAttributeUid(uidAttr)
        case nameAttr: Name => SAttributeName(nameAttr)
        case attr => attrMap.get(attr.getName).map { ai => ai.getType }.map {
          case `classString` => SAttributeString(attr)
          case `classInt` => SAttributeInt(attr)
        }.orNull
      }.toSet
    )
  }
}