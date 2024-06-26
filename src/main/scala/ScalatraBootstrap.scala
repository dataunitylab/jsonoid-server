// $COVERAGE-OFF$
import io.github.dataunitylab.jsonoid.server._
import org.scalatra._
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext): Unit = {
    context.mount(new JsonoidServlet, "/*")
  }
}
// $COVERAGE-ON$
