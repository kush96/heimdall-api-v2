package com.joveo.dao

import com.joveo.Application.logger
import com.joveo.dao.`trait`.UserDao
import com.joveo.model.{Pager, User}
import org.mongodb.scala.MongoCollection
import org.mongodb.scala.bson.{BsonArray, BsonDocument, BsonString}
import org.mongodb.scala.model.Aggregates._
import org.mongodb.scala.model.Filters
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Projections.{fields, include}
import org.mongodb.scala.model.Updates.{combine, set}

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

case class MongoUserDaoImpl(collection: MongoCollection[User])(implicit ec: ExecutionContext) extends UserDao {

  override def addUser(user: User): Future[String] = {
    for {
      _ <- collection.insertOne(user).toFuture()
    } yield user.id
  }

  override def getUser(email: String): Future[Option[User]] = {
    collection.find(equal("emailId", email)).headOption()
  }

  override def updateUser(user: User): Future[String] = {
    collection.updateOne(Filters.equal("email", user.email),
      combine(
        set("scopes", user.scopes)
      )
    ).toFuture().map(isInserted => isInserted.wasAcknowledged() match {
      case true => user.id
    })
  }

  override def userCountForRole(roleId: String): Future[Long] = {
    collection.countDocuments(equal("scopes.role", roleId)).toFuture()
  }

  override def getRoleCountForAccount(application: String, accountId: String)  = {

    val filterBson = and(equal("scopes.productId", application),equal("scopes.accountId", accountId))
    logger.info("before aggregation")

    val k = collection.aggregate(List(
      `match`(filterBson),
//      unwind("$scopes"),
//      `match`(filterBson),
//      group("$scopes.role", org.mongodb.scala.model.Accumulators.sum("userCount", 1)),
//      project(fields(include("userCount"))),
    )).headOption()
//      .map(doc => RoleCount("id" -> doc.get("_id"), "userCount" -> doc.get("userCount")))
//      .collect()
//      .subscribe((docs: Seq[Document]) => println(docs))

//    val k = collection.aggregate(
//      List(
//        `match`(equal("email", "preeti@joveo.com"))
//      )
//    ).headOption()
//    logger.info("before aggregation")
    println(k.map(user => user))
    val result = Await.result(k, Duration(10, TimeUnit.SECONDS))
    println(result)
    k
  }

  override def getRoleCountForAccount2(productId: String, accountId: String, pager: Pager) = {
    //val filterBson = in("jobGroupId", jobGroupIds: _*)
    val filterBson = and(equal("scopes.productId", productId),equal("scopes.accountId", accountId))


        val (skip, limit) = ((pager.page - 1) * pager.limit, pager.limit)
        collection.aggregate(
          List(`match`(filterBson),
            unwind("$scopes"),
            `match`(filterBson),
            group("$scopes.role", org.mongodb.scala.model.Accumulators.sum("userCount", 1)),
            project(fields(include("userCount"))),
//            group(null, org.mongodb.scala.model.Accumulators.push("keywords", "$awKeywords")),
//            unwind("$keywords"),
//            unwind("$keywords"),
//            group("$id", org.mongodb.scala.model.Accumulators.push("results", "$keywords")),
//            project(fields(include("results"), excludeId())),
            project(BsonDocument(
              List(
                //"userCountResults" -> BsonDocument("$slice" -> BsonArray(BsonString("$userCount"), skip, limit)
                "userCountResults" -> BsonDocument("$slice" -> BsonArray(BsonString("$userCount"), skip, limit)
                )
              )
            ))
          ))

  }
}

