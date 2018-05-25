package org.uzooin

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.util.Random

class RecordedSimulation extends Simulation {

	val httpProtocol = http
		.baseURL("http://computer-database.gatling.io")
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
		.userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36")

	val headers_0 = Map("Upgrade-Insecure-Requests" -> "1")

	val feeder = Iterator.continually(Map("email" -> (Random.alphanumeric.take(20).mkString + "@foo.com")))
	val feeder2 = Iterator.continually(Map("email" -> (Random.alphanumeric.take(20).mkString + "@foo.com")))

  //    .queue // default behavior: use an Iterator on the underlying sequence
  //    .random // randomly pick an entry in the sequence
  //    .shuffle // shuffle entries, then behave like queue
  //    .circular // go back to the top of the sequence once the end is reached
	val feeder3 = Array(
		Map("foo" -> "foo1", "bar" -> "bar1"),
		Map("foo" -> "foo2", "bar" -> "bar2"),
		Map("foo" -> "foo3", "bar" -> "bar3")
	).random

	val scn = scenario("RecordedSimulation")
		.feed(feeder)
		.feed(feeder2, 2)
		.feed(feeder3)
		.exec(http("GET /computer-database.gatling.io")
			.get("/")
			.headers(headers_0)
	  	.check(status.is(200)))
		.exec{session => println(session); session}
		.pause(5)
		.exec(http("GET /computer-database.gatling.io/computers")
			.get("/computers")
			.headers(headers_0)
			.check(status.not(404), status.not(500)))
		.exec{session => println(session); session}
		.pause(5)
		.exec(http("Search computers = amstrad")
			.get("/computers?f=amstrad")
			.headers(headers_0)
      .check(status.in(200 to 210))
      .check(bodyString.saveAs("responseBody"))) // check status is in a range
  	.exec{session => println(session("responseBody").as[String]); session}
		.exec{session => println(">>>>>>>>>" + session("email").as[String]); session}

	// checks : https://gatling.io/docs/current/http/http_check/#http-check-saving

	setUp(scn.inject(atOnceUsers(2))).protocols(httpProtocol)
}