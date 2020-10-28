package com.noteable.api

import com.noteable.db.DBTables
import org.scalatra.json.JacksonJsonSupport
import org.scalatra.{FutureSupport, ScalatraServlet}
import org.slf4j.LoggerFactory
import slick.jdbc.H2Profile.api._

class DBApi(db: Database) extends ScalatraServlet with FutureSupport {

  val logger = LoggerFactory.getLogger(this.getClass.getName)

  protected implicit def executor = scala.concurrent.ExecutionContext.Implicits.global


  get("/create-tables") {
    db.run(DBTables.createSchemaAction)
    logger.debug("database schema created")
  }

  get("/load-data") {
    db.run(DBTables.insertData)
    logger.debug("database data loaded")
  }

  get("/drop-tables") {
    db.run(DBTables.dropSchemaAction)
    logger.debug("database tables dropped")
  }

}
