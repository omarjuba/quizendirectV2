
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class prof extends Simulation {

	val httpProtocol = http
		.baseUrl("https://etud-kvm-csadik.leria-etud.univ-angers.fr")
		.inferHtmlResources(BlackList(), WhiteList())
		.userAgentHeader("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36")

	val headers_0 = Map(
		"accept" -> "*/*",
		"accept-encoding" -> "gzip, deflate, br",
		"accept-language" -> "fr-FR,fr;q=0.9,en-US;q=0.8,en;q=0.7",
		"content-type" -> "application/json",
		"origin" -> "https://etud-kvm-csadik.leria-etud.univ-angers.fr",
		"sec-ch-ua" -> """ Not A;Brand";v="99", "Chromium";v="90""",
		"sec-ch-ua-mobile" -> "?0",
		"sec-fetch-dest" -> "empty",
		"sec-fetch-mode" -> "cors",
		"sec-fetch-site" -> "same-origin",
		"x-requested-with" -> "XMLHttpRequest")

	val headers_2 = Map(
		"accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"accept-encoding" -> "gzip, deflate, br",
		"accept-language" -> "fr-FR,fr;q=0.9,en-US;q=0.8,en;q=0.7",
		"sec-ch-ua" -> """ Not A;Brand";v="99", "Chromium";v="90""",
		"sec-ch-ua-mobile" -> "?0",
		"sec-fetch-dest" -> "document",
		"sec-fetch-mode" -> "navigate",
		"sec-fetch-site" -> "same-origin",
		"sec-fetch-user" -> "?1",
		"upgrade-insecure-requests" -> "1")

	val headers_3 = Map(
		"sec-ch-ua" -> """ Not A;Brand";v="99", "Chromium";v="90""",
		"sec-ch-ua-mobile" -> "?0")

	val headers_8 = Map(
		"Origin" -> "https://etud-kvm-csadik.leria-etud.univ-angers.fr",
		"sec-ch-ua" -> """ Not A;Brand";v="99", "Chromium";v="90""",
		"sec-ch-ua-mobile" -> "?0")

	val headers_9 = Map(
		"accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"accept-encoding" -> "gzip, deflate, br",
		"accept-language" -> "fr-FR,fr;q=0.9,en-US;q=0.8,en;q=0.7",
		"sec-ch-ua" -> """ Not A;Brand";v="99", "Chromium";v="90""",
		"sec-ch-ua-mobile" -> "?0",
		"sec-fetch-dest" -> "document",
		"sec-fetch-mode" -> "navigate",
		"sec-fetch-site" -> "same-origin",
		"upgrade-insecure-requests" -> "1")

	val headers_22 = Map(
		"accept" -> "*/*",
		"accept-encoding" -> "gzip, deflate, br",
		"accept-language" -> "fr-FR,fr;q=0.9,en-US;q=0.8,en;q=0.7",
		"sec-ch-ua" -> """ Not A;Brand";v="99", "Chromium";v="90""",
		"sec-ch-ua-mobile" -> "?0",
		"sec-fetch-dest" -> "empty",
		"sec-fetch-mode" -> "cors",
		"sec-fetch-site" -> "same-origin")

    val uri1 = "https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"
    val uri2 = "https://code.jquery.com/jquery-1.11.1.min.js"
    val uri3 = "https://cdnjs.cloudflare.com/ajax/libs/Chart.js/3.5.1/chart.min.js"
    val uri5 = "https://cdn.jsdelivr.net/npm"
    val uri6 = "https://maxcdn.bootstrapcdn.com/bootstrap"

	val scn = scenario("prof")
		.exec(http("request_0")
			.post("/graphql")
			.headers(headers_0)
			.body(RawFileBody("prof/0000_request.json"))
			.resources(http("request_1")
			.post("/graphql")
			.headers(headers_0)
			.body(RawFileBody("prof/0001_request.json")),
            http("request_2")
			.get("/connection")
			.headers(headers_2),
            http("request_3")
			.get(uri6 + "/3.3.0/js/bootstrap.min.js")
			.headers(headers_3),
            http("request_4")
			.get(uri2)
			.headers(headers_3),
            http("request_5")
			.get("/hubGestion.css")
			.headers(headers_3),
            http("request_6")
			.get(uri6 + "/3.3.0/css/bootstrap.min.css")
			.headers(headers_3),
            http("request_7")
			.get("/hubGestion.js")
			.headers(headers_3),
            http("request_8")
			.get(uri6 + "/3.3.0/fonts/glyphicons-halflings-regular.woff")
			.headers(headers_8)))
		.pause(1)
		.exec(http("request_9")
			.get("/creationSalon")
			.headers(headers_9)
			.resources(http("request_10")
			.get(uri2)
			.headers(headers_3),
            http("request_11")
			.get("/callAPI.js")
			.headers(headers_3),
            http("request_12")
			.get(uri6 + "/3.4.1/css/bootstrap.min.css")
			.headers(headers_3),
            http("request_13")
			.get(uri6 + "/3.4.1/js/bootstrap.min.js")
			.headers(headers_3),
            http("request_14")
			.get("/creationSalon.css")
			.headers(headers_3),
            http("request_15")
			.get(uri5 + "/sockjs-client@1/dist/sockjs.min.js")
			.headers(headers_3),
            http("request_16")
			.get(uri5 + "/stomp-websocket@2.3.4-next/lib/stomp.min.js")
			.headers(headers_3),
            http("request_17")
			.get(uri1)
			.headers(headers_3),
            http("request_18")
			.get("/creationSalon.js")
			.headers(headers_3),
            http("request_19")
			.get(uri3)
			.headers(headers_8),
            http("request_20")
			.post("/graphql")
			.headers(headers_0)
			.body(RawFileBody("prof/0020_request.json"))))
		.pause(4)
		.exec(http("request_21")
			.get(uri6 + "/3.4.1/fonts/glyphicons-halflings-regular.woff2")
			.headers(headers_8))
		.pause(1)
		.exec(http("request_22")
			.get("/ws/info?t=1637765128832")
			.headers(headers_22)
			.resources(http("request_23")
			.post("/graphql")
			.headers(headers_0)
			.body(RawFileBody("prof/0023_request.json"))))
		.pause(13)
		.exec(http("request_24")
			.post("/graphql")
			.headers(headers_0)
			.body(RawFileBody("prof/0024_request.json")))

	setUp(scn.inject(atOnceUsers(50))).protocols(httpProtocol)
}
