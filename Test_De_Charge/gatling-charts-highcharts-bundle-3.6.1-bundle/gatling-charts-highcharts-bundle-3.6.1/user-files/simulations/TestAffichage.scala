
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class TestAffichage extends Simulation {

	val httpProtocol_prof = http
		.baseUrl("http://localhost:20020")
		.inferHtmlResources(BlackList(), WhiteList())
		.acceptHeader("*/*")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("vi-VN,vi;q=0.9,fr-FR;q=0.8,fr;q=0.7,en-US;q=0.6,en;q=0.5")
		.contentTypeHeader("application/json")
		.originHeader("http://localhost:8081")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.45 Safari/537.36")

	val headers_prof = Map(
		"Sec-Fetch-Dest" -> "empty",
		"Sec-Fetch-Mode" -> "cors",
		"Sec-Fetch-Site" -> "same-site",
		"sec-ch-ua" -> """ Not A;Brand";v="99", "Chromium";v="96", "Google Chrome";v="96""",
		"sec-ch-ua-mobile" -> "?0",
		"sec-ch-ua-platform" -> "Windows")


	val httpProtocol_etud = http
		.baseUrl("https://maxcdn.bootstrapcdn.com")
		.inferHtmlResources(BlackList(), WhiteList())
		.originHeader("http://localhost:8081")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.45 Safari/537.36")

	val headers_etud = Map(
		"sec-ch-ua" -> """ Not A;Brand";v="99", "Chromium";v="96", "Google Chrome";v="96""",
		"sec-ch-ua-mobile" -> "?0",
		"sec-ch-ua-platform" -> "Windows")

	

	val scn_prof = scenario("ProfLance")
		.exec(http("request_0")
			.post("/graphql")
			.headers(headers_prof)
			.body(RawFileBody("proflance/0000_request.json")))

	val scn_etud = scenario("EtuAffiche")
		.exec(http("request_0")
			.get("/bootstrap/3.3.0/fonts/glyphicons-halflings-regular.woff")
			.headers(headers_etud))



	setUp( 
		( scn_prof.inject(
			atOnceUsers(5), 
			constantUsersPerSec(10).during(15.seconds))).protocols(httpProtocol_prof),
		( scn_etud.inject(
			atOnceUsers(50),
			constantUsersPerSec(20).during(20.seconds))).protocols(httpProtocol_etud)
	)

}