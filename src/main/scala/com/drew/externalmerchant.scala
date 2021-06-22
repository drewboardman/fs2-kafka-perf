package com.drew

import com.drew.models._
import io.circe.Codec
//NOTE: Do not remove this import. IntelliJ will see it as unused, but it is needed
//      by circe for codecs on refined types
import io.circe.refined._
import io.circe.generic.semiauto.deriveCodec

object externalmerchant extends CoercibleCodecs {
  case class ZupplerMerchant(
      restaurantId: RestaurantId,
      merchantName: MerchantName,
      address1: Address1,
      city: City,
      state: State,
      zip5: Zip5,
      phone: Option[Phone], // currently don't have a way to get correct phone from zuppler
      geoLatitude: GeoLatitude,
      geoLongitude: GeoLongitude,
      delivery: Boolean,
      pickup: Boolean,
      featuredImageUrl: Option[ImageUrl],
      logoUrl: Option[ImageUrl],
      hasRnmid: Boolean,
      isActiveStatus: Boolean,
      statusChangedTimestamp: Option[StatusChangedTimestamp] = None
  )

  object ZupplerMerchant {
    implicit val codec: Codec[ZupplerMerchant] = deriveCodec[ZupplerMerchant]
  }

  case class ChowNowMerchant(
      restaurantBrandId: RestaurantBrandId,
      restaurantLocationId: RestaurantLocationId,
      merchantName: MerchantName,
      address1: Address1,
      address2: Option[Address2],
      city: City,
      state: State,
      zip5: Zip5,
      phone: Phone,
      geoLatitude: GeoLatitude,
      geoLongitude: GeoLongitude,
      delivery: Boolean,
      pickup: Boolean,
      orderingUrl: OrderingUrl,
      restaurantWebsite: Option[RestaurantWebsite],
      isActiveStatus: Boolean,
      hasRnmid: Boolean,
      statusChangedTimestamp: Option[StatusChangedTimestamp] = None
  )

  case class ChowNowId(restaurantBrandId: RestaurantBrandId, restaurantLocationId: RestaurantLocationId)

  object ChowNowMerchant {
    implicit val codec: Codec[ChowNowMerchant] = deriveCodec[ChowNowMerchant]
  }

  case class ExternalMerchant(
      externalMerchantId: MerchantId,
      rnMid: Option[RnMid],
      merged: Boolean = false,
      mergedIntoExternalMerchantId: Option[MergedIntoExternalMerchantId] = None,
      deleted: Boolean = false,
      deletedTimestamp: Option[DeletedTimestamp] = None,
      mergedTimestamp: Option[MergedTimestamp] = None,
      cuisines: Cuisines,
      zuppler: Option[ZupplerMerchant],
      chowNow: Option[ChowNowMerchant] = None
  )

  object ExternalMerchant {
    implicit val codec: Codec[ExternalMerchant] = deriveCodec[ExternalMerchant]
  }
}
