package com.joveo.model

import com.joveo.constants.GenericConstants.ProductNames
import com.joveo.dto.UserDTOs.ScopeDto
//import com.joveo.model.main.formats
import org.joda.time.DateTime
import org.json4s.FieldSerializer
import org.json4s.JsonAST.{JField, JString}
//import org.json4s.JsonDSL.jobject2assoc
import org.json4s.JsonDSL._
import org.json4s.native.{Json, JsonMethods, Serialization}
import org.mongodb.scala.bson.annotations.BsonProperty
import org.json4s.native.JsonMethods.parse
import org.json4s.native.Serialization.write
import org.json4s.{DefaultFormats, Formats, JObject, JString, JValue, JsonAST, ShortTypeHints}
import spray.json.{JsObject, pimpAny}

import java.util.Date
import scala.::


case class User(
                 @BsonProperty("_id") id: String = "",
                 email: String,
                 displayName: String,
                 scopes: List[Scope],
                 profilePictureUrl: String,
                 appMetadata: Map[String, String]
               )

case class Scope(
                  productId: String,
                  accountId: String,
                  roleId: String,
                  createdBy: String,
                  createdAt: Date,
                  metadata: ScopeMetadata,
                  status: String
                )

case class MojoProAppMetadata()

case class MojoGoAppMetadata()

case class CDAppMetadata()

// look in authenticate to find out if an
object UserUtils {
  implicit val formats = Serialization.formats(ShortTypeHints(List(classOf[CDScopeMetadata], classOf[MojoGoScopeMetadata], classOf[MojoProScopeMetadata], classOf[VPScopeMetadata]))) + FieldSerializer[MojoGoScopeMetadata with ScopeMetadata]() + FieldSerializer[MojoProScopeMetadata with ScopeMetadata]() + FieldSerializer[CDScopeMetadata with ScopeMetadata]()

  val productMetadataClassNameMap: Map[String, String] = Map(
    ProductNames.MOJO_GO_PRODUCT_NAME -> getClassNameForSer(MojoGoScopeMetadata.getClass),
    ProductNames.MOJO_PRO_PRODUCT_NAME -> getClassNameForSer(MojoProScopeMetadata.getClass),
    ProductNames.CLIENT_DASHBOARD_PRODUCT_NAME -> getClassNameForSer(CDScopeMetadata.getClass)
  )
  val productMetadataClassSimpleNameMap: Map[String, String] = Map(
    ProductNames.MOJO_GO_PRODUCT_NAME -> MojoGoScopeMetadata.getClass.getSimpleName,
    ProductNames.MOJO_PRO_PRODUCT_NAME -> MojoProScopeMetadata.getClass.getSimpleName,
    ProductNames.CLIENT_DASHBOARD_PRODUCT_NAME -> CDScopeMetadata.getClass.getSimpleName
  )

  private def getClassNameForSer(clazz: Class[_ <: Any]): String = {
    val className = clazz.getName.split('.').last
    className.substring(0, className.length - 1)
  }

  def getClassNameForMongoSer(productName:String) = productMetadataClassSimpleNameMap.get(productName).map(_.dropRight(1))

  def serialize2(scopeDto: ScopeDto): ScopeMetadata = {
    val scopeMetadataJson = scopeDto.metadata.asInstanceOf[JObject] ~ ("jsonClass" -> productMetadataClassNameMap(scopeDto.productId.toLowerCase()))
    val scopeMetadataStr = JsonMethods.compact(JsonMethods.render(scopeMetadataJson))
    Serialization.read[ScopeMetadata](scopeMetadataStr)
  }

  def toJValue(scopeMetadata: ScopeMetadata): JValue = {
    JsonMethods.parse(Serialization.write(scopeMetadata))
  }
}

sealed class ScopeMetadata()

case class MojoProScopeMetadata(clientIds: List[String], _t: Option[String] = UserUtils.getClassNameForMongoSer(ProductNames.MOJO_PRO_PRODUCT_NAME)) extends ScopeMetadata

case class CDScopeMetadata(allowedMetrics: List[String],
                           savedSearches: List[SavedSearch] = List.empty,
                           barCharts: List[BarChartConfig],
                           canWrite: Boolean = true,
                           _t: Option[String] =UserUtils.getClassNameForMongoSer(ProductNames.CLIENT_DASHBOARD_PRODUCT_NAME)) extends ScopeMetadata

case class MojoGoScopeMetadata(
                                clientId: String,
                                recruiterId: Option[String],
                                teamId: Option[String],
                                teamAccess: Option[List[String]],
                                hierarchicalAccessScope: Option[Map[String, List[String]]],
                                assignJobsBy: Option[String] = None,
                                ssoEnabled: Boolean = false,
                                _t: Option[String] =UserUtils.getClassNameForMongoSer(ProductNames.MOJO_GO_PRODUCT_NAME)
                              ) extends ScopeMetadata

case class VPScopeMetadata(agencies: List[String],
                           publisherFamily: String,
                           liveJobs: Long,
                           totalJobs: Long
                          ) extends ScopeMetadata

/*CD metadata classes*/
case class SavedSearch(name: String, filters: SearchFilters)

case class SearchFilters(
                          scope: Int,
                          publisherIds: List[String],
                          locationIds: List[String],
                          entityIds: List[String],
                          jobFilterType: Int,
                          jobTypeFilter: Int,
                          jobStatusFilter: Int,
                          jobFilterValues: List[String],
                          selectedJobs: List[JobDto],
                          platformFilters: Option[String] = None
                        )

case class JobDto(
                   id: String,
                   display: String,
                   city: String,
                   state: String,
                   category: String,
                   reqId: String,
                   stats: DashboardMetric = DashboardMetric(0, 0, 0, 0, 0, 0),
                   jobGroupId: Option[String] = None,
                   company: String = "",
                   url: String = "",
                   inferredStatus: Boolean = false)

case class DashboardMetric(
                            spent: Double,
                            clicks: Int,
                            applies: Int,
                            cta: Double,
                            cpc: Double,
                            cpa: Double,
                            hires: Long = 0,
                            cph: Double = 0.0,
                            ath: Double = 0.0,
                            allClicks: Long = 0,
                            allBotClicks: Long = 0,
                            allViews: Long = 0,
                            allApplyStarts: Long = 0,
                            allApplies: Long = 0,
                            allCta: Double = 0.0,
                            allHires: Long = 0,
                            allCph: Double = 0.0,
                            allAth: Double = 0.0)

case class BarChartConfig(indicators: List[DashBoardItem])

case class DashBoardItem(
                          id: String,
                          display: String,
                          client: Option[DashBoardItem] = None
                        )

//object main extends App{
//  import org.json4s.native.JsonMethods.parse
//  import org.json4s.DefaultFormats
//  import org.json4s.JValue
//  import org.json4s.DoubleJsonFormats.GenericFormat
////  implicit val formats: Formats = Serialization.formats(ShortTypeHints(List(classOf[Entity1], classOf[Entity2]))) + FieldSerializer[Entity1 with Entity]
////  import org.json4s.JsonDSL._
//  sealed trait Entity{
//    final val _t = this.getClass.getName
//  }
//  case class Entity1(name : String, value : Int) extends Entity{
//
//  }
//  case class Entity2(name : String, value : Long) extends Entity
//  val x = parse(""" { "name" : "abu", "value" : 1 }  """)
//  val y = x.asInstanceOf[JObject] ~ ("jsonClass"->"main$Entity1")
////  print(y)
//  val ser = write(Entity1("kush",4))
//  val jsonStr = JsonMethods.compact(JsonMethods.render(y))
//  val z = Serialization.read[Entity](jsonStr)
//  println(z)
//  //  def extract[T](input : JValue)(implicit m : Manifest[T]) = input.extract[T]
////
////  val mapping: Map[String, Manifest[_]] = Map(
////    "entity1" -> implicitly[Manifest[Entity1]],
////    "entity2" -> implicitly[Manifest[Entity2]]
////  )
////
////  val input = parse(""" { "name" : "abu", "value" : 1 } """)
////  val x = extract(input)(mapping("entity1")).asInstanceOf[]//Entity1("abu", 1)
////  extract(input)(mapping("entity2")) //Entity2("abu", 1L)
//}

//object main2 extends App {
//  case class Door(name:String) extends Thing
//  sealed class Thing {
//    val created: DateTime = DateTime.now
//  }
//  val qwerty = MojoProScopeMetadata(List())
//  print(qwerty)
////  implicit val formats = DefaultFormats + FieldSerializer[Door with Thing]()
////
////  val obj = new Door("dooor")
////  val x = write(obj)
////  print(x)
//}