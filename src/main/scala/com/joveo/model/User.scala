package com.joveo.model

import com.joveo.constants.GenericConstants.ProductNames
import com.joveo.dto.UserDTOs.ScopeDto
import org.mongodb.scala.bson.annotations.BsonProperty
import org.json4s.native.JsonMethods.parse
import org.json4s.DefaultFormats
import org.json4s.JValue

import java.util.Date


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
                  metadata: Any,
                  status: String
                )

case class MojoProAppMetadata()

case class MojoGoAppMetadata()

case class CDAppMetadata()

// look in authenticate to find out if an
object UserUtils {
  implicit val formats = DefaultFormats
  def extract[T](input : JValue)(implicit m : Manifest[T]) = input.extract[T]

  val productMetadataClassMap: Map[String, Manifest[_]] = Map(
    ProductNames.MOJO_GO_PRODUCT_NAME -> implicitly[Manifest[MojoGoScopeMetadata]],
    ProductNames.MOJO_PRO_PRODUCT_NAME -> implicitly[Manifest[MojoProScopeMetadata]],
    ProductNames.CLIENT_DASHBOARD_PRODUCT_NAME -> implicitly[Manifest[CDScopeMetadata]]
  )

  def serialize(scopeDto: ScopeDto) = {
    extract(parse(scopeDto.metadata))(productMetadataClassMap(scopeDto.productId.toLowerCase()))
  }
}

case class MojoProScopeMetadata(clientIds: List[Int])

case class CDScopeMetadata(allowedMetrics: List[String],
                           savedSearches: List[SavedSearch] = List.empty,
                           barCharts: List[BarChartConfig],
                           canWrite: Boolean = true)

case class MojoGoScopeMetadata(
                                clientId: String,
                                recruiterId: Option[String],
                                teamId: Option[String],
                                teamAccess: Option[List[String]],
                                hierarchicalAccessScope: Option[Map[String, List[String]]],
                                assignJobsBy: Option[String] = None
                              )


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
