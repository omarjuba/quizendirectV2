
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class etud extends Simulation {

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

	val headers_1 = Map(
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

	val headers_2 = Map(
		"sec-ch-ua" -> """ Not A;Brand";v="99", "Chromium";v="90""",
		"sec-ch-ua-mobile" -> "?0")

	val headers_7 = Map(
		"Origin" -> "https://etud-kvm-csadik.leria-etud.univ-angers.fr",
		"sec-ch-ua" -> """ Not A;Brand";v="99", "Chromium";v="90""",
		"sec-ch-ua-mobile" -> "?0")

	val headers_12 = Map(
		"accept" -> "text/javascript, application/javascript, application/ecmascript, application/x-ecmascript, */*; q=0.01",
		"accept-encoding" -> "gzip, deflate, br",
		"accept-language" -> "fr-FR,fr;q=0.9,en-US;q=0.8,en;q=0.7",
		"sec-ch-ua" -> """ Not A;Brand";v="99", "Chromium";v="90""",
		"sec-ch-ua-mobile" -> "?0",
		"sec-fetch-dest" -> "empty",
		"sec-fetch-mode" -> "cors",
		"sec-fetch-site" -> "same-origin",
		"x-requested-with" -> "XMLHttpRequest")

	val headers_13 = Map(
		"accept" -> "*/*",
		"accept-encoding" -> "gzip, deflate, br",
		"accept-language" -> "fr-FR,fr;q=0.9,en-US;q=0.8,en;q=0.7",
		"sec-ch-ua" -> """ Not A;Brand";v="99", "Chromium";v="90""",
		"sec-ch-ua-mobile" -> "?0",
		"sec-fetch-dest" -> "empty",
		"sec-fetch-mode" -> "cors",
		"sec-fetch-site" -> "same-origin")

	val headers_16 = Map(
		"accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"accept-encoding" -> "gzip, deflate, br",
		"accept-language" -> "fr-FR,fr;q=0.9,en-US;q=0.8,en;q=0.7",
		"sec-ch-ua" -> """ Not A;Brand";v="99", "Chromium";v="90""",
		"sec-ch-ua-mobile" -> "?0",
		"sec-fetch-dest" -> "document",
		"sec-fetch-mode" -> "navigate",
		"sec-fetch-site" -> "same-origin",
		"upgrade-insecure-requests" -> "1")

    val uri1 = "https://code.jquery.com/jquery-1.11.1.min.js"
    val uri2 = "https://cdnjs.cloudflare.com/ajax/libs/quill/1.3.7"
    val uri4 = "https://cdn.jsdelivr.net/npm"
    val uri5 = "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.0"
    val uri6 = "https://netdna.bootstrapcdn.com/bootstrap/3.0.0"

	val scn = scenario("etud")
		.exec(http("request_0")
			.post("/graphql")
			.headers(headers_0)
			.body(RawFileBody("etud/0000_request.json"))
			.resources(http("request_1")
			.get("/quiz?codeAcces=1344")
			.headers(headers_1),
            http("request_2")
			.get(uri1)
			.headers(headers_2),
            http("request_3")
			.get("/quiz.css")
			.headers(headers_2),
            http("request_4")
			.get(uri5 + "/css/bootstrap.min.css")
			.headers(headers_2),
            http("request_5")
			.get(uri5 + "/js/bootstrap.min.js")
			.headers(headers_2),
            http("request_6")
			.get(uri4 + "/sockjs-client@1/dist/sockjs.min.js")
			.headers(headers_2),
            http("request_7")
			.get(uri2 + "/quill.bubble.css")
			.headers(headers_7),
            http("request_8")
			.get(uri2 + "/quill.snow.css")
			.headers(headers_7),
            http("request_9")
			.get(uri4 + "/stomp-websocket@2.3.4-next/lib/stomp.min.js")
			.headers(headers_2),
            http("request_10")
			.get(uri2 + "/quill.min.js")
			.headers(headers_7),
            http("request_11")
			.get("/Quiz.js")
			.headers(headers_2),
            http("request_12")
			.get("/callAPI.js?_=1637765138752")
			.headers(headers_12),
            http("request_13")
			.get("/ws/info?t=1637765138797")
			.headers(headers_13)))
		.pause(4)
		.exec(http("request_14")
			.get(uri5 + "/fonts/glyphicons-halflings-regular.woff")
			.headers(headers_7))
		.pause(5)
		.exec(http("request_15")
			.post("/graphql")
			.headers(headers_0)
			.body(RawFileBody("etud/0015_request.json")))
		.pause(11)
		.exec(http("request_16")
			.get("/")
			.headers(headers_16)
			.resources(http("request_17")
			.get(uri1)
			.headers(headers_2),
            http("request_18")
			.get(uri6 + "/css/bootstrap.min.css")
			.headers(headers_2),
            http("request_19")
			.get(uri6 + "/js/bootstrap.min.js")
			.headers(headers_2)))

	setUp(scn.inject(atOnceUsers(20),constantUsersPerSec(10).during(15.seconds))).protocols(httpProtocol)
}
