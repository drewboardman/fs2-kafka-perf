package com.drew

import eu.timepit.refined.types.all.{NonNegBigInt, NonNegInt}
import io.estatico.newtype.Coercible
import io.estatico.newtype.ops._
import org.scalacheck.Gen

import java.util.UUID

trait CommonGen {
  val genNonEmptyString: Gen[String] =
    Gen
      .chooseNum(10, 30)
      .flatMap { n =>
        Gen.buildableOfN[String, Char](n, Gen.alphaChar)
      }

  val genStringyInt: Gen[String] = Gen.posNum[Int].map(_.toString)

  def coerceGenUuid[A: Coercible[UUID, *]]: Gen[A]                 = Gen.uuid.map(_.coerce[A])
  def coerceGenStr[A: Coercible[String, *]]: Gen[A]                = genNonEmptyString.map(_.coerce[A])
  def coerceGenInt[A: Coercible[Int, *]]: Gen[A]                   = Gen.posNum[Int].map(_.coerce[A])
  def coerceGenStringyInt[A: Coercible[String, *]]: Gen[A]         = genStringyInt.map(_.coerce[A])
  def coerceGenNonNegative[A: Coercible[NonNegInt, *]]: Gen[A]     =
    Gen.chooseNum(0, 5000).map(NonNegInt.unsafeFrom).map(_.coerce[A])
  def coerceGenNonNegBigInt[A: Coercible[NonNegBigInt, *]]: Gen[A] =
    Gen
      .chooseNum(0, 5000)
      .map(i => NonNegBigInt.unsafeFrom(i))
      .map(_.coerce[A])
  def coerceGenOffsetInt[A: Coercible[Int, *]]: Gen[A]             = Gen.chooseNum(-500, 500).map(_.coerce[A])
  def coerceGenLat[A: Coercible[Float, *]]: Gen[A]                 = Gen.chooseNum(-90.toFloat, 90.toFloat).map(_.coerce[A])
  def coerceGenLong[A: Coercible[Float, *]]: Gen[A]                = Gen.chooseNum(-180.toFloat, 180.toFloat).map(_.coerce[A])
}
