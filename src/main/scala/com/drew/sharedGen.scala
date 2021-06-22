package com.drew

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

  val zupplerInfoGen: Gen[ZupplerInfo] = for {
    rnMid <- Gen.option(rnMidGen)
    restaurantId <- restaurantIdGen
    merchantName <- coerceGenStr[MerchantName]
    address1 <- coerceGenStr[Address1]
    city <- coerceGenStr[City]
    state <- coerceGenStr[State]
    zip5 <- coerceGenStr[Zip5]
    phone <- Gen.option(coerceGenStr[Phone])
    geoLat <- coerceGenStr[GeoLatitude]
    geoLong <- coerceGenStr[GeoLongitude]
    delivery <- Gen.prob(0.5)
    pickup <- Gen.prob(0.5)
    featuredImageUrl <- Gen.option(coerceGenStr[ImageUrl])
    logoUrl <- Gen.option(coerceGenStr[ImageUrl])
    cuisines <- coerceGenStr[Cuisines]
    hasRnmid <- Gen.prob(0.5)
    isActiveStatus <- Gen.prob(0.5)
  } yield ZupplerInfo(
    rnMid,
    restaurantId,
    merchantName,
    address1,
    city,
    state,
    zip5,
    phone,
    geoLat,
    geoLong,
    delivery,
    pickup,
    featuredImageUrl,
    logoUrl,
    cuisines,
    hasRnmid,
    isActiveStatus
  )

  val chowNowInfoGen: Gen[ChowNowInfo] = for {
    restaurantBranchId <- coerceGenNonNegative[RestaurantBrandId]
    restaurantLocationId <- coerceGenNonNegative[RestaurantLocationId]
    merchantName <- coerceGenStr[MerchantName]
    address1 <- coerceGenStr[Address1]
    address2 <- Gen.option(coerceGenStr[Address2])
    city <- coerceGenStr[City]
    state <- coerceGenStr[State]
    zip5 <- coerceGenStr[Zip5]
    phone <- coerceGenStr[Phone]
    geoLat <- coerceGenStr[GeoLatitude]
    geoLong <- coerceGenStr[GeoLongitude]
    delivery <- Gen.prob(0.5)
    pickup <- Gen.prob(0.5)
    orderingUrl <- coerceGenStr[OrderingUrl]
    restaurantWebsite <- Gen.option(coerceGenStr[RestaurantWebsite])
    isActiveStatus <- Gen.prob(0.5)
    cuisines <- coerceGenStr[Cuisines]
  } yield ChowNowInfo(
    restaurantBranchId,
    restaurantLocationId,
    merchantName,
    address1,
    address2,
    city,
    state,
    zip5,
    phone,
    geoLat,
    geoLong,
    delivery,
    pickup,
    orderingUrl,
    restaurantWebsite,
    cuisines,
    isActiveStatus
  )

  val zupplerInfoRequestGen: Gen[ZupplerInfoRequest] =
    Gen
      .listOf(zupplerInfoGen)
      .map(ZupplerInfoRequest)

  val chowNowInfoRequestGen: Gen[ChowNowInfoRequest] =
    for {
      merchants <- Gen.listOf(chowNowInfoGen)
      fn <- genNonEmptyString
    } yield ChowNowInfoRequest(Some(fn), Some(merchants.size), merchants)
}
