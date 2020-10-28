package com.noteable.model

import java.sql.Timestamp

object DataTypes {

  case class Doctor(id: Int, firstName: String, lastName: String)

  case class Appointment(id: Int, startTime: Timestamp, endTme: Timestamp, appointmentType: String, doctorId: Int)

  case class DoctorAppointment(doctor: Doctor, appointment: Appointment)

}
