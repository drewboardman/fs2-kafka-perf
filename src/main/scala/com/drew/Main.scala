package com.drew

import cats.effect.{Clock, IO, IOApp}
import fs2.Stream
import fs2.kafka._
import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec
import io.circe.syntax._
import org.scalacheck.Gen
import org.typelevel.log4cats.SelfAwareStructuredLogger
import org.typelevel.log4cats.slf4j.Slf4jLogger

object Main extends IOApp.Simple {
  import Stuff._
  implicit def unsafeLogger: SelfAwareStructuredLogger[IO] = Slf4jLogger.getLogger[IO]

  // This is your new "main"!
  def run: IO[Unit] = {
    val volume = 1_000_000
    val producerSettings: ProducerSettings[IO, String, String] =
      ProducerSettings[IO, String, String]
        .withBootstrapServers("localhost:9091")

    val stream: Stream[IO, ProducerRecords[Unit, String, String]] = Stream
      .emit(Stuff.genBag.sample)
      .repeatN(volume)
      .unNone
      .map { bag =>
        val record = toRecord(bag)
        val res = ProducerRecords.one(record)
        res
      }

    val process = stream
      .through(KafkaProducer.pipe(producerSettings))
      .compile
      .drain

    withLoggedTimingF(process, s"publishing $volume events")
  }

  def toRecord(raw: Bag): ProducerRecord[String, String] = {
    val key = raw.a
    val value = raw.asJson.toString
    val size = value.getBytes("UTF-8").length
    ProducerRecord("topic", key, value)
  }

  def withLoggedTimingF[A](process: IO[A], label: String): IO[A] =
    for {
      start <- Clock[IO].realTime
      result <- process
      stop <- Clock[IO].realTime
      total = stop.length - start.length
      _ <- IO.pure(println(s"$label took: $total millis"))
    } yield result
}

object Stuff {
  case class Bag(
    a: String,
    b: String,
    c: String,
    d: String,
    e: String,
    f: String,
    g: String,
    h: String,
    i: String,
    j: String,
    k: String,
    l: String,
    m: String,
    n: String,
    o: String,
    p: String,
    q: String
  )

  implicit val codec: Codec[Bag] = deriveCodec[Bag]

  val genNonEmptyString: Gen[String] =
    Gen
      .chooseNum(10, 30)
      .flatMap { n =>
        Gen.buildableOfN[String, Char](n, Gen.alphaChar)
      }

  val genBag: Gen[Bag] = for {
  a <- genNonEmptyString
  b <- genNonEmptyString
  c <- genNonEmptyString
  d <- genNonEmptyString
  e <- genNonEmptyString
  f <- genNonEmptyString
  g <- genNonEmptyString
  h <- genNonEmptyString
  i <- genNonEmptyString
  j <- genNonEmptyString
  k <- genNonEmptyString
  l <- genNonEmptyString
  m <- genNonEmptyString
  n <- genNonEmptyString
  o <- genNonEmptyString
  p <- genNonEmptyString
  q <- genNonEmptyString
  } yield Bag(a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q)
}
