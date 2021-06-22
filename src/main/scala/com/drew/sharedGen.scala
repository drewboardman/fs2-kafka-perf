package com.drew

import com.drew.externalmerchant.ChowNowId
import com.drew.models._
import org.scalacheck.Gen

object sharedGen extends CommonGen {
  val rnMidGen: Gen[RnMid] =
    coerceGenNonNegBigInt[RnMid]

  val restaurantIdGen: Gen[RestaurantId] =
    coerceGenNonNegative[RestaurantId]

  val chowNowIdGen: Gen[ChowNowId] =
    for {
      brandId <- coerceGenNonNegative[RestaurantBrandId]
      locationId <- coerceGenNonNegative[RestaurantLocationId]
    } yield ChowNowId(brandId, locationId)
}
