package com.drew

import eu.timepit.refined.types.all.{NonNegBigInt, NonNegInt}
import io.estatico.newtype.macros.newtype

import java.time.Instant
import java.util.UUID

object models {
  @newtype case class MerchantId(value: UUID)
  @newtype case class RnMid(value: NonNegBigInt)
  @newtype case class MergedIntoExternalMerchantId(value: UUID)
  @newtype case class DeletedTimestamp(value: Instant)
  @newtype case class MergedTimestamp(value: Instant)
  @newtype case class Cuisines(value: String)

  @newtype case class GeoLatitude(value: String)
  @newtype case class GeoLongitude(value: String)

  @newtype case class MerchantName(value: String)
  @newtype case class RestaurantId(value: NonNegInt)
  @newtype case class Address1(value: String)
  @newtype case class Address2(value: String)
  @newtype case class Zip5(value: String)
  @newtype case class Phone(value: String)
  @newtype case class StatusChangedTimestamp(value: Instant)
  @newtype case class City(value: String)
  @newtype case class State(value: String)

  @newtype case class ImageUrl(value: String)

  @newtype case class RestaurantBrandId(value: NonNegInt)
  @newtype case class RestaurantLocationId(value: NonNegInt)
  @newtype case class OrderingUrl(value: String)
  @newtype case class RestaurantWebsite(value: String)
}
