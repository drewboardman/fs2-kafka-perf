package com.drew

import com.drew.event._
import com.drew.externalmerchant._
import com.drew.models._
import com.drew.sharedGen._
import io.estatico.newtype.Coercible
import io.estatico.newtype.ops._
import org.scalacheck.Gen

import java.time.{Instant, LocalDate}

object generators extends CommonGen {

  def genDateTime: Gen[LocalDate] =
    Gen.choose(LocalDate.MIN.toEpochDay, LocalDate.now().toEpochDay).map(LocalDate.ofEpochDay)

  def genInstant: Gen[Instant] =
    Gen.choose(Instant.MIN.getEpochSecond, Instant.now().getEpochSecond).map(Instant.ofEpochSecond)

  def coerceGenInstant[A: Coercible[Instant, *]]: Gen[A] = genInstant.map(_.coerce[A])

  def coerceGenLocalDate[A: Coercible[LocalDate, *]]: Gen[A] = genDateTime.map(_.coerce[A])

  val externalMerchantIdGen: Gen[MerchantId] = coerceGenUuid[MerchantId]

  val merchantEvent: Gen[ExternalMerchantEvent] = for {
    eventVersion <- coerceGenStr[EventVersion]
    eventType <- Gen.oneOf(Seq("foo", "bar", "baz"))
    payload <- kafkaExternalMerchant
  } yield ExternalMerchantEvent(eventVersion, eventType, payload)

  val zupplerMerchant: Gen[ZupplerMerchant] = for {
    restaurantId <- coerceGenNonNegative[RestaurantId]
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
    hasRnmid <- Gen.prob(0.5)
    isActiveStatus <- Gen.prob(0.5)
    statusChangedTimestamp <- Gen.option(coerceGenInstant[StatusChangedTimestamp])
  } yield ZupplerMerchant(
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
    hasRnmid,
    isActiveStatus,
    statusChangedTimestamp
  )

  val chowNowMerchant: Gen[ChowNowMerchant] = for {
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
    hasRnmid <- Gen.prob(0.5)
    isActiveStatus <- Gen.prob(0.5)
    statusChangedTimestamp <- Gen.option(coerceGenInstant[StatusChangedTimestamp])
  } yield ChowNowMerchant(
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
    hasRnmid,
    isActiveStatus,
    statusChangedTimestamp
  )

  val externalMerchant: Gen[ExternalMerchant] = for {
    externalMerchantId <- externalMerchantIdGen
    rnMid <- Gen.option(rnMidGen)
    merged <- Gen.prob(0.5)
    deleted <- Gen.prob(0.5)
    mergedIntoExternalMerchantId <- Gen.option(coerceGenUuid[MergedIntoExternalMerchantId])
    deletedTimestamp <- Gen.option(coerceGenInstant[DeletedTimestamp])
    mergedTimestamp <- Gen.option(coerceGenInstant[MergedTimestamp])
    cuisines <- coerceGenStr[Cuisines]
    zuppler <- zupplerMerchant.map(Some(_))
    chownow <- chowNowMerchant.map(Some(_))
  } yield ExternalMerchant(
    externalMerchantId,
    rnMid,
    merged,
    mergedIntoExternalMerchantId,
    deleted,
    deletedTimestamp,
    mergedTimestamp,
    cuisines,
    zuppler,
    chownow
  )

  val kafkaExternalMerchant: Gen[KafkaExternalMerchant] = for {
    externalMerchantId <- externalMerchantIdGen
    rnMid <- Gen.option(rnMidGen)
    merged <- Gen.prob(0.5)
    deleted <- Gen.prob(0.5)
    mergedIntoExternalMerchantId <- Gen.option(coerceGenUuid[MergedIntoExternalMerchantId])
    deletedTimestamp <- Gen.option(coerceGenInstant[DeletedTimestamp])
    mergedTimestamp <- Gen.option(coerceGenInstant[MergedTimestamp])
    cuisines <- Gen.listOfN(5, coerceGenStr[Cuisines])
    zuppler <- Gen.option(zupplerMerchant)
  } yield KafkaExternalMerchant(
    externalMerchantId,
    rnMid,
    merged,
    mergedIntoExternalMerchantId,
    deleted,
    deletedTimestamp,
    mergedTimestamp,
    cuisines,
    zuppler,
    None
  )
}
