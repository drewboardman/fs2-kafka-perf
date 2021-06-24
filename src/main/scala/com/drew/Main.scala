package com.drew

import cats.effect.{Clock, IO, IOApp}
import fs2.{Chunk, Pure, Stream}
import fs2.kafka._
import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec
import io.circe.syntax._
import org.scalacheck.Gen
import org.typelevel.log4cats.SelfAwareStructuredLogger
import org.typelevel.log4cats.slf4j.Slf4jLogger

import scala.concurrent.duration.DurationInt

object Main extends IOApp.Simple {
  import Stuff._
  implicit def unsafeLogger: SelfAwareStructuredLogger[IO] = Slf4jLogger.getLogger[IO]

  // This is your new "main"!
  def run: IO[Unit] = {
    val volume = 1_000_000

    val stream: Stream[IO, Chunk[String]] = Stream
      .emit(Stuff.genBag.sample)
      .repeatN(volume)
      .unNone
      .chunkN(1_000)
      .map(toJSON)

    val process: IO[Unit] = stream
      .compile
      .drain

    withLoggedTimingF(process, s"converting $volume objects to JSON")
  }

  def toJSON(chunk: Chunk[Bag]): Chunk[String] = chunk.map(_.asJson.toString)

  def toRecords(chunk: Chunk[Bag]): ProducerRecords[Unit, String, String] = {
    val records = chunk.map { bag =>
      val key = bag.a
      val value = bag.asJson.toString
      ProducerRecord("topic", key, value)
    }
    ProducerRecords(records)
  }

  def toRecord(raw: Bag): ProducerRecord[String, String] = {
    val key = raw.a
    val value = raw.asJson.toString
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
