package com.noteable.api

import org.scalatra.test.scalatest._

class MyServletTests extends ScalatraFunSuite {

  addServlet(classOf[DoctorAppointmentApi], "/*")

  test("GET / on MyServlet should return status 200") {
    get("/*") {
      status should equal(200)
    }
  }

}
