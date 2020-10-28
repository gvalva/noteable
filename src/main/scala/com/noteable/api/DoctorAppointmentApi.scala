package com.noteable.api

import java.sql.Timestamp

import com.noteable.db.DBTables
import com.noteable.model.DataTypes.{Appointment, Doctor, DoctorAppointment}
import org.joda.time.DateTime
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json.JacksonJsonSupport
import org.scalatra.{FutureSupport, NotFound, ScalatraServlet}
import org.slf4j.LoggerFactory
import slick.jdbc.H2Profile.api._

class DoctorAppointmentApi(val db: Database) extends ScalatraServlet with FutureSupport with JacksonJsonSupport {

  protected implicit def executor = scala.concurrent.ExecutionContext.Implicits.global

  val logger = LoggerFactory.getLogger(this.getClass.getName)

  protected implicit lazy val jsonFormats: Formats = DefaultFormats

  before() {
    contentType = formats("json")
  }

  get("/") {
    db.run(DBTables.findAllDoctors.result).map { rs =>
      rs.map {
        case (id, first, last) => Doctor(id, first, last)
        case _ => Doctor
      }
    }
  }

  get("/:id/:date") {
    val doctorId = params("id")
    val date = params("date")

    db.run(DBTables.findAllByDoctorAndDate(doctorId.toInt, date)).map { rs =>
      rs.map {
        case (id, first, last, appType, start, end, appId) =>
          DoctorAppointment(Doctor(id, first, last), Appointment(appId, start, end, appType, id))
        case _ => Doctor
      }
    }
  }

  delete("/:id/:date") {
    val doctorId = params("id")
    val date = params("date")

    db.run(DBTables.deleteAppointment(doctorId.toInt, Timestamp.valueOf(date)))
  }

  post("/") {
    val doctorAppointment = parsedBody.extract[DoctorAppointment]

    val appointment = doctorAppointment.appointment
    val appointmentMinutes = DateTime.parse(appointment.startTime.toString).getMinuteOfHour

    if (appointmentMinutes % 15 != 0) {
      NotFound("Sorry invalid start time")
    } else {
      db.run(DBTables.createAppointment(appointment))
    }
  }

}

