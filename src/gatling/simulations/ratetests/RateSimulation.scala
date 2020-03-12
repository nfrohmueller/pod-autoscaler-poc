package ratetests

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class RateSimulation extends Simulation {
  val httpProtocoll = http.baseUrl("http://localhost:3149")

  val scn = scenario("Constant Rate")
    .exec(http("test requests").get("/test")).pause(5)

  setUp(
    scn.inject(
      rampUsersPerSec (1) to 100 during (2 minutes)
    )
  ).protocols(httpProtocoll)
}
