package com.noteable.db

import slick.jdbc.H2Profile.api._
import java.sql.Timestamp

import com.noteable.model.DataTypes
import org.joda.time.DateTime

object DBTables {

  class Doctor(tag: Tag) extends Table[(Int, String, String)](tag, "DOCTOR") {
    def id = column[Int]("ID", O.PrimaryKey) // This is the primary key column
    def firstName = column[String]("FIRST_NAME")

    def lastName = column[String]("LAST_NAME")

    def * = (id, firstName, lastName)
  }

  // Definition of the COFFEES table
  class Appointment(tag: Tag) extends Table[(Int, Timestamp, Timestamp, String, Int)](tag, "APPOINTMENT") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def startTime = column[Timestamp]("START_TIME")

    def endTime = column[Timestamp]("END_TIME")

    def appointmentType = column[String]("APPOINTMENT_TYPE")

    def docterId = column[Int]("DOCTOR_ID")

    def * = (id, startTime, endTime, appointmentType, docterId)

    // A reified foreign key relation that can be navigated to create a join
    def supplier = foreignKey("DOCTOR_FK", docterId, doctors)(_.id)
  }

  val doctors = TableQuery[Doctor]

  val appointments = TableQuery[Appointment]

  val createSchemaAction = (doctors.schema ++ appointments.schema).create

  val dropSchemaAction = (doctors.schema ++ appointments.schema).drop

  def findAllDoctors = doctors

  val insertData = DBIO.seq(
    DBTables.doctors += (1, "Chris", "Mas"),
    DBTables.doctors += (2, "New", "Year"),
    DBTables.doctors += (3, "Birth", "Day"),

    DBTables.appointments += (1, Timestamp.valueOf("2020-10-10 09:00:00.000"), Timestamp.valueOf("2020-10-10 09:30:00.000"), "New", 1),
    DBTables.appointments += (2, Timestamp.valueOf("2020-10-10 09:30:00.000"), Timestamp.valueOf("2020-10-10 09:45:00.000"), "other", 1),

    DBTables.appointments += (3, Timestamp.valueOf("2020-10-10 09:00:00.000"), Timestamp.valueOf("2020-10-10 09:30:00.000"), "New", 2),
    DBTables.appointments += (4, Timestamp.valueOf("2020-10-10 09:30:00.000"), Timestamp.valueOf("2020-10-10 09:45:00.000"), "other", 2),

    DBTables.appointments += (5, Timestamp.valueOf("2020-10-10 09:00:00.000"), Timestamp.valueOf("2020-10-10 09:30:00.000"), "New", 3),
    DBTables.appointments += (6, Timestamp.valueOf("2020-10-10 09:30:00.000"), Timestamp.valueOf("2020-10-10 09:45:00.000"), "other", 3),
  )

  def findAllByDoctorAndDate(doctorId: Int, date: String) = {
    val startTime = Timestamp.valueOf(date)
    val endTime = new Timestamp(DateTime.parse(Timestamp.valueOf(date).toString).plusDays(1).getMillis)

    sql"""select d.id, d.first_name, d.last_name, a.appointment_type, a.start_time, a.end_time, a.doctorId
          from doctor d, appointment a
          where d.id == s.doctor_id and d.id == ${doctorId}
          and a.start_time >= ${startTime}
          and a.end_time < ${endTime}
      """.as[(Int, String, String, String, Timestamp, Timestamp, Int)]

  }

  def deleteAppointment(doctorId: Int, date: Timestamp) = {
    appointments.filter(app => app.id === doctorId && app.startTime === date).delete
  }

  def createAppointment(app: DataTypes.Appointment) = {
    DBIO.seq(
      sqlu"""
          insert into appointment (start_time, end_time, appointment_type, doctor_id)
          values( ${app.startTime}, ${app.endTme}, ${app.appointmentType}, ${app.doctorId})""")
  }

}
