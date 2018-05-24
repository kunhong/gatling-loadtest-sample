package org.uzooin

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class RecordedSimulation extends Simulation {

	val httpProtocol = http
		.baseURL("http://computer-database.gatling.io")
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
		.userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36")

	val headers_0 = Map("Upgrade-Insecure-Requests" -> "1")



	val scn = scenario("RecordedSimulation")
		.exec(http("request_0")
			.get("/")
			.headers(headers_0)
	  	.check(status.is(200)))
		.exec{session => println(session); session}
		.pause(5)
		.exec(http("request_1")
			.get("/computers?f=amstrad")
			.headers(headers_0)
      .check(status.in(200 to 210))
      .check(bodyString.saveAs("responseBody"))) // check status is in a range
  	.exec{session => println(session("responseBody").as[String]); session}



	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}