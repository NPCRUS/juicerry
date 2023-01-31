package serde

import io.circe.{Decoder, Encoder}
import components.ThreeStateSwitch.State

import scala.util.Try

object ThreeStateSwitchSerde {

  given Encoder[State] = Encoder.encodeString.contramap(v => v.toString)

  given Decoder[State] = Decoder.decodeString.emapTry(s => Try(State.valueOf(s)))

}
