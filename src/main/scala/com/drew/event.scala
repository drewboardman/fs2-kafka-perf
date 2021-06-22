package com.drew

import com.drew.externalmerchant._
import com.drew.models._
import io.circe.generic.semiauto.{deriveCodec, deriveDecoder, deriveEncoder}
import io.circe.{Codec, Decoder, Encoder}
import io.circe.refined._
import io.estatico.newtype.macros.newtype

object event extends CoercibleCodecs {
  @newtype case class EventVersion(value: String)

  case class ExternalMerchantEvent(
      version: EventVersion,
      eventType: String,
      payload: KafkaExternalMerchant
  )

  case class KafkaExternalMerchant(
      externalMerchantId: MerchantId,
      rnMid: Option[RnMid],
      merged: Boolean = false,
      mergedIntoExternalMerchantId: Option[MergedIntoExternalMerchantId] = None,
      deleted: Boolean = false,
      deletedTimestamp: Option[DeletedTimestamp] = None,
      mergedTimestamp: Option[MergedTimestamp] = None,
      cuisines: List[Cuisines],
      zuppler: Option[ZupplerMerchant],
      chowNow: Option[ChowNowMerchant] = None
  )

  object KafkaExternalMerchant {
    implicit val codec: Codec[KafkaExternalMerchant] = deriveCodec[KafkaExternalMerchant]

    def apply(em: ExternalMerchant): KafkaExternalMerchant =
      KafkaExternalMerchant(
        em.externalMerchantId,
        em.rnMid,
        em.merged,
        em.mergedIntoExternalMerchantId,
        em.deleted,
        em.deletedTimestamp,
        em.mergedTimestamp,
        em.cuisines.value.split(",").toList.map(c => Cuisines(c.trim)),
        em.zuppler,
        em.chowNow
      )
  }

  object ExternalMerchantEvent {
    implicit val encode: Encoder[ExternalMerchantEvent] =
      deriveEncoder[ExternalMerchantEvent].mapJson(_.deepDropNullValues)
    implicit val decode: Decoder[ExternalMerchantEvent] = deriveDecoder[ExternalMerchantEvent]
  }
}
