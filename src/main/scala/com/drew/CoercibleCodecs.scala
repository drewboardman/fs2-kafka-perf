package com.drew

import cats.kernel.Eq
import io.circe.{Decoder, Encoder}
import io.estatico.newtype.Coercible
import io.estatico.newtype.ops._

import java.util.UUID

trait CoercibleCodecs {
  // These exist because we're using the newtype library
  implicit def coercibleDecoder[A: Coercible[B, *], B: Decoder]: Decoder[A] = Decoder[B].map(_.coerce[A])

  implicit def coercibleEncoder[A: Coercible[B, *], B: Encoder]: Encoder[A] = Encoder[B].contramap(_.asInstanceOf[B])

  // ----- Coercible instances ------
  private def coercibleEq[A: Eq, B: Coercible[A, *]]: Eq[B] =
    new Eq[B] {
      def eqv(x: B, y: B): Boolean = Eq[A].eqv(x.asInstanceOf[A], y.asInstanceOf[A])
    }

  implicit def coercibleStringEq[B: Coercible[String, *]]: Eq[B] = coercibleEq[String, B]
  implicit def coercibleUuidEq[B: Coercible[UUID, *]]: Eq[B]     = coercibleEq[UUID, B]
  implicit def coercibleIntEq[B: Coercible[Int, *]]: Eq[B]       = coercibleEq[Int, B]
}
