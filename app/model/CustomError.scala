package model

import play.api.libs.json.{JsObject, Json, OFormat, OWrites}

/*
 Model to handle any error thrown while crawling web data
 */

case class CustomError(url: String, message: String) {
  def toJson: JsObject = Json.obj("error" -> Json.toJson(this))
}

object CustomError {
  implicit val customErrorFormat: OFormat[CustomError] = Json.format[CustomError]
  implicit val customErrorWrite: OWrites[CustomError] = Json.writes[CustomError]
}
