package sangria.marshalling

import spray.json._

trait SprayJsonSupportLowPrioImplicits {
  implicit val SprayJsonInputUnmarshallerJObject =
    sprayJson.SprayJsonInputUnmarshaller.asInstanceOf[InputUnmarshaller[JsObject]]
}
