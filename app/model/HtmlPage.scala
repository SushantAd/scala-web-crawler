package model

import play.api.libs.json.{Json, OFormat, OWrites}

/*
 Model to accept crawled data from web pages, it will currently store
 url -> Web page Url
 title -> Title of the Web Page
 body -> Complete Body of the Web Page
 ..
 Can be extended to fetch different elements of the web page.
 */

case class HtmlPage(url: String, title: String, body: String) {
  def toJson = Json.obj("result" -> Json.toJson(this))
}

object HtmlPage {
  implicit val htmlPageFormat: OFormat[HtmlPage] = Json.format[HtmlPage]
  implicit val htmlPageWrite: OWrites[HtmlPage] = Json.writes[HtmlPage]
}
