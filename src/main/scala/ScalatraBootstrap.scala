import com.mchange.v2.c3p0.ComboPooledDataSource
import com.noteable.api.{DBApi, DoctorAppointmentApi}
import javax.servlet.ServletContext
import org.scalatra._
import org.slf4j.LoggerFactory
import slick.jdbc.H2Profile.api._

class ScalatraBootstrap extends LifeCycle {

  val cpds = new ComboPooledDataSource
  val logger = LoggerFactory.getLogger(this.getClass.getSimpleName)

  override def init(context: ServletContext) {
    val db = Database.forDataSource(cpds, None)

    context.mount(new DBApi(db), "/db")
    context.mount(new DoctorAppointmentApi(db), "/doctor")
  }

  private def closeDbConnection() {
    cpds.close
    logger.debug("Database server closed")
  }

  override def destroy(context: ServletContext) {
    super.destroy(context)
    closeDbConnection
  }
}
