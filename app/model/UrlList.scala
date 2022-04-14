package model

import play.api.libs.json._

case class UrlList(urls: List[String])

object UrlList {
  implicit val urlListFormat = Json.format[UrlList]
}
