package org.uzooin

import io.gatling.core.Predef._
import io.gatling.http.Predef.{bodyString, http, jsonPath, status}

class JsonServerSimulation extends Simulation {
  val httpProtocol = http
    .baseURL("https://jsonplaceholder.typicode.com")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36")

  val headers_0 = Map("Upgrade-Insecure-Requests" -> "1")

  val scn = scenario("JsonServerSimulation")
    .exec(http("GET /posts/1")
      .get("/posts/1")
      .headers(headers_0)
        .check(status.is(200))
        .check(jsonPath("$..userId").ofType[Int].optional.saveAs("userId"))
//  Gatling provides built-in support for the following types:
//
//  String
//  Boolean
//  Int
//  Long
//  Double
//  Float
//  Seq (JSON array)
//  Map (JSON object)
//  Any
      .check(bodyString.saveAs("responseBody")))
    .exec{session => println(session); session}
    .exec{session => println(session("responseBody").as[String]); session}
    .exec{session => println(session("userId").as[Int]); session}

  // https://github.com/linagora/james-gatling/blob/master/src/main/scala-2.11/org/apache/james/gatling/utils/RetryAuthentication.scala

  // checks : https://gatling.io/docs/current/http/http_check/#http-check-saving
  setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}
