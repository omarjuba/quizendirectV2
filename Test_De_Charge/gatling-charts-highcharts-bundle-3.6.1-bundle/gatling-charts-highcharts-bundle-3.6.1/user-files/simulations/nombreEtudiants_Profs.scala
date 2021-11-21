
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class Users extends Simulation {

	val httpProtocol = http
		.baseUrl("http://localhost:20020")
		.inferHtmlResources(BlackList(""".*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.54 Safari/537.36")

	val headers_0 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Encoding" -> "gzip, deflate, br",
		"Accept-Language" -> "vi-VN,vi;q=0.9,fr-FR;q=0.8,fr;q=0.7,en-US;q=0.6,en;q=0.5",
		"Sec-Fetch-Dest" -> "document",
		"Sec-Fetch-Mode" -> "navigate",
		"Sec-Fetch-Site" -> "same-origin",
		"Sec-Fetch-User" -> "?1",
		"Upgrade-Insecure-Requests" -> "1",
		"sec-ch-ua" -> """Google Chrome";v="95", "Chromium";v="95", ";Not A Brand";v="99""",
		"sec-ch-ua-mobile" -> "?0",
		"sec-ch-ua-platform" -> "Windows")

	val headers_1 = Map(
		"Accept" -> "*/*",
		"Accept-Encoding" -> "gzip, deflate, br",
		"Accept-Language" -> "vi-VN,vi;q=0.9,fr-FR;q=0.8,fr;q=0.7,en-US;q=0.6,en;q=0.5",
		"Sec-Fetch-Dest" -> "script",
		"Sec-Fetch-Mode" -> "no-cors",
		"Sec-Fetch-Site" -> "same-origin",
		"sec-ch-ua" -> """Google Chrome";v="95", "Chromium";v="95", ";Not A Brand";v="99""",
		"sec-ch-ua-mobile" -> "?0",
		"sec-ch-ua-platform" -> "Windows")

	val headers_2 = Map("Accept" -> "*/*")

	val headers_3 = Map(
		"Accept" -> "*/*",
		"Accept-Encoding" -> "gzip, deflate, br",
		"Accept-Language" -> "vi-VN,vi;q=0.9,fr-FR;q=0.8,fr;q=0.7,en-US;q=0.6,en;q=0.5",
		"Content-Type" -> "application/json",
		"Origin" -> "http://localhost:8081",
		"Sec-Fetch-Dest" -> "empty",
		"Sec-Fetch-Mode" -> "cors",
		"Sec-Fetch-Site" -> "same-site",
		"sec-ch-ua" -> """Google Chrome";v="95", "Chromium";v="95", ";Not A Brand";v="99""",
		"sec-ch-ua-mobile" -> "?0",
		"sec-ch-ua-platform" -> "Windows")

	val headers_4 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Encoding" -> "gzip, deflate, br",
		"Accept-Language" -> "vi-VN,vi;q=0.9,fr-FR;q=0.8,fr;q=0.7,en-US;q=0.6,en;q=0.5",
		"Sec-Fetch-Dest" -> "document",
		"Sec-Fetch-Mode" -> "navigate",
		"Sec-Fetch-Site" -> "same-origin",
		"Upgrade-Insecure-Requests" -> "1",
		"sec-ch-ua" -> """Google Chrome";v="95", "Chromium";v="95", ";Not A Brand";v="99""",
		"sec-ch-ua-mobile" -> "?0",
		"sec-ch-ua-platform" -> "Windows")

	val headers_5 = Map(
		"sec-ch-ua" -> """Google Chrome";v="95", "Chromium";v="95", ";Not A Brand";v="99""",
		"sec-ch-ua-mobile" -> "?0",
		"sec-ch-ua-platform" -> "Windows")

	val headers_6 = Map(
		"Origin" -> "http://localhost:8081",
		"sec-ch-ua" -> """Google Chrome";v="95", "Chromium";v="95", ";Not A Brand";v="99""",
		"sec-ch-ua-mobile" -> "?0",
		"sec-ch-ua-platform" -> "Windows")

	val headers_7 = Map(
		"Accept" -> "*/*",
		"Accept-Encoding" -> "gzip, deflate, br",
		"Accept-Language" -> "vi-VN,vi;q=0.9,fr-FR;q=0.8,fr;q=0.7,en-US;q=0.6,en;q=0.5",
		"Origin" -> "http://localhost:8081",
		"Sec-Fetch-Dest" -> "empty",
		"Sec-Fetch-Mode" -> "cors",
		"Sec-Fetch-Site" -> "same-site",
		"sec-ch-ua" -> """Google Chrome";v="95", "Chromium";v="95", ";Not A Brand";v="99""",
		"sec-ch-ua-mobile" -> "?0",
		"sec-ch-ua-platform" -> "Windows")

	val headers_8 = Map(
		"Accept" -> "*/*",
		"Accept-Encoding" -> "gzip, deflate, br",
		"Accept-Language" -> "vi-VN,vi;q=0.9,fr-FR;q=0.8,fr;q=0.7,en-US;q=0.6,en;q=0.5",
		"Content-Type" -> "application/json",
		"Origin" -> "http://localhost:8081",
		"Sec-Fetch-Dest" -> "empty",
		"Sec-Fetch-Mode" -> "cors",
		"Sec-Fetch-Site" -> "same-site",
		"sec-ch-ua" -> """Google Chrome";v="95", "Chromium";v="95", ";Not A Brand";v="99""",
		"sec-ch-ua-mobile" -> "?0",
		"sec-ch-ua-platform" -> "Windows")

	val headers_9 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Encoding" -> "gzip, deflate, br",
		"Accept-Language" -> "vi-VN,vi;q=0.9,fr-FR;q=0.8,fr;q=0.7,en-US;q=0.6,en;q=0.5",
		"Sec-Fetch-Dest" -> "document",
		"Sec-Fetch-Mode" -> "navigate",
		"Sec-Fetch-Site" -> "same-origin",
		"Sec-Fetch-User" -> "?1",
		"Upgrade-Insecure-Requests" -> "1",
		"sec-ch-ua" -> """Google Chrome";v="95", "Chromium";v="95", ";Not A Brand";v="99""",
		"sec-ch-ua-mobile" -> "?0",
		"sec-ch-ua-platform" -> "Windows")

	val headers_10 = Map(
		"sec-ch-ua" -> """Google Chrome";v="95", "Chromium";v="95", ";Not A Brand";v="99""",
		"sec-ch-ua-mobile" -> "?0",
		"sec-ch-ua-platform" -> "Windows")

	val headers_11 = Map(
		"Origin" -> "http://localhost:8081",
		"sec-ch-ua" -> """Google Chrome";v="95", "Chromium";v="95", ";Not A Brand";v="99""",
		"sec-ch-ua-mobile" -> "?0",
		"sec-ch-ua-platform" -> "Windows")

	val headers_12 = Map("Accept" -> "*/*")

	val headers_13 = Map(
		"Accept" -> "*/*",
		"Accept-Encoding" -> "gzip, deflate, br",
		"Accept-Language" -> "vi-VN,vi;q=0.9,fr-FR;q=0.8,fr;q=0.7,en-US;q=0.6,en;q=0.5",
		"Sec-Fetch-Dest" -> "script",
		"Sec-Fetch-Mode" -> "no-cors",
		"Sec-Fetch-Site" -> "same-origin",
		"sec-ch-ua" -> """Google Chrome";v="95", "Chromium";v="95", ";Not A Brand";v="99""",
		"sec-ch-ua-mobile" -> "?0",
		"sec-ch-ua-platform" -> "Windows")

	val headers_14 = Map(
		"Accept" -> "text/javascript, application/javascript, application/ecmascript, application/x-ecmascript, */*; q=0.01",
		"Accept-Encoding" -> "gzip, deflate, br",
		"Accept-Language" -> "vi-VN,vi;q=0.9,fr-FR;q=0.8,fr;q=0.7,en-US;q=0.6,en;q=0.5",
		"Sec-Fetch-Dest" -> "empty",
		"Sec-Fetch-Mode" -> "cors",
		"Sec-Fetch-Site" -> "same-origin",
		"X-Requested-With" -> "XMLHttpRequest",
		"sec-ch-ua" -> """Google Chrome";v="95", "Chromium";v="95", ";Not A Brand";v="99""",
		"sec-ch-ua-mobile" -> "?0",
		"sec-ch-ua-platform" -> "Windows")

	val headers_15 = Map(
		"Accept" -> "*/*",
		"Accept-Encoding" -> "gzip, deflate, br",
		"Accept-Language" -> "vi-VN,vi;q=0.9,fr-FR;q=0.8,fr;q=0.7,en-US;q=0.6,en;q=0.5",
		"Origin" -> "http://localhost:8081",
		"Sec-Fetch-Dest" -> "empty",
		"Sec-Fetch-Mode" -> "cors",
		"Sec-Fetch-Site" -> "same-site",
		"sec-ch-ua" -> """Google Chrome";v="95", "Chromium";v="95", ";Not A Brand";v="99""",
		"sec-ch-ua-mobile" -> "?0",
		"sec-ch-ua-platform" -> "Windows")

    val uri1 = "https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"
    val uri2 = "localhost"
    val uri3 = "http://code.jquery.com/jquery-1.11.1.min.js"
    val uri4 = "https://cdnjs.cloudflare.com/ajax/libs/Chart.js/3.5.1/chart.min.js"
    val uri5 = "https://cdn.jsdelivr.net/npm"
    val uri6 = "http://maxcdn.bootstrapcdn.com/bootstrap"

	val uri7 = "localhost"
    val uri8 = "http://code.jquery.com/jquery-1.11.1.min.js"
    val uri9 = "https://cdnjs.cloudflare.com/ajax/libs/quill/1.3.7/quill.min.js"
    val uri10 = "https://cdn.jsdelivr.net/npm"
    val uri11 = "http://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js"


	val scn1 = scenario("Prof")
		.exec(http("request_0")
			.get("http://" + uri2 + ":8081/connection")
			.headers(headers_0)
			.resources(http("request_1")
			.get(uri3),
            http("request_2")
			.get("http://" + uri2 + ":8081/callAPI.js")
			.headers(headers_1),
            http("request_3")
			.get("http://" + uri2 + ":8081/comptePage-claire.js")
			.headers(headers_1),
            http("request_4")
			.get(uri6 + "/3.3.0/js/bootstrap.min.js")
			.headers(headers_2)))
		.pause(5)
		.exec(http("request_5")
			.post("/graphql")
			.headers(headers_3)
			.body(RawFileBody("prof/0005_request.json"))
			.resources(http("request_6")
			.post("/graphql")
			.headers(headers_3)
			.body(RawFileBody("prof/0006_request.json")),
            http("request_7")
			.get("http://" + uri2 + ":8081/connection")
			.headers(headers_0),
            http("request_8")
			.get(uri3),
            http("request_9")
			.get("http://" + uri2 + ":8081/hubGestion.js")
			.headers(headers_1),
            http("request_10")
			.get(uri6 + "/3.3.0/js/bootstrap.min.js")
			.headers(headers_2)))
		.pause(1)
		.exec(http("request_11")
			.get("http://" + uri2 + ":8081/creationSalon")
			.headers(headers_0)
			.resources(http("request_12")
			.get("http://" + uri2 + ":8081/creationSalon")
			.headers(headers_4),
            http("request_13")
			.get(uri3),
            http("request_14")
			.get("http://" + uri2 + ":8081/callAPI.js")
			.headers(headers_1),
            http("request_15")
			.get(uri1)
			.headers(headers_5),
            http("request_16")
			.get(uri5 + "/sockjs-client@1/dist/sockjs.min.js")
			.headers(headers_5),
            http("request_17")
			.get(uri5 + "/stomp-websocket@2.3.4-next/lib/stomp.min.js")
			.headers(headers_5),
            http("request_18")
			.get(uri4)
			.headers(headers_6),
            http("request_19")
			.get("http://" + uri2 + ":8081/creationSalon.js")
			.headers(headers_1),
            http("request_20")
			.get(uri6 + "/3.4.1/js/bootstrap.min.js")
			.headers(headers_2),
            http("request_21")
			.post("/graphql")
			.headers(headers_3)
			.body(RawFileBody("prof/0021_request.json"))))
		.pause(8)
		.exec(http("request_22")
			.get("/ws/info?t=1635848081457")
			.headers(headers_7)
			.resources(http("request_23")
			.post("/graphql")
			.headers(headers_3)
			.body(RawFileBody("prof/0023_request.json"))))


	val scn2 = scenario("Etud1")
		.exec(http("request_0")
			.post("/graphql")
			.headers(headers_8)
			.body(RawFileBody("etud1/0000_request.json"))
			.resources(http("request_1")
			.get("http://" + uri7 + ":8081/quiz?codeAcces=2345")
			.headers(headers_9),
            http("request_2")
			.get(uri8),
            http("request_3")
			.get(uri10 + "/sockjs-client@1/dist/sockjs.min.js")
			.headers(headers_10),
            http("request_4")
			.get(uri10 + "/stomp-websocket@2.3.4-next/lib/stomp.min.js")
			.headers(headers_10),
            http("request_5")
			.get(uri9)
			.headers(headers_11),
            http("request_6")
			.get(uri11)
			.headers(headers_12),
            http("request_7")
			.get("http://" + uri7 + ":8081/Quiz.js")
			.headers(headers_13),
            http("request_8")
			.get("http://" + uri7 + ":8081/callAPI.js?_=1635850800603")
			.headers(headers_14),
            http("request_9")
			.get("/ws/info?t=1635850800658")
			.headers(headers_15)))

	setUp( 
		( scn1.inject(
			atOnceUsers(5), 
			constantUsersPerSec(10).during(15.seconds).randomized )).protocols(httpProtocol),
		( scn2.inject(
			atOnceUsers(20),
			constantUsersPerSec(20).during(20.seconds).randomized )).protocols(httpProtocol)
	)


}
