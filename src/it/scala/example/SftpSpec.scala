package example

import java.net.InetAddress
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

import akka.NotUsed
import akka.event.{Logging, LoggingAdapter}
import akka.stream._
import akka.stream.alpakka.ftp.FtpCredentials.NonAnonFtpCredentials
import akka.stream.alpakka.ftp.scaladsl.Sftp
import akka.stream.alpakka.ftp.{FtpFile, SftpSettings}
import akka.stream.scaladsl._
import akka.stream.testkit.scaladsl.TestSink
import akka.testkit.TestProbe
import org.scalatest._
import org.scalatest.time.Seconds

import scala.concurrent.duration.{Duration, FiniteDuration}


class SftpSpec extends FlatSpec with Matchers {
  implicit val system = akka.actor.ActorSystem("system");
  implicit val materializer = ActorMaterializer();
  val log: LoggingAdapter = Logging.getLogger(system, this)

  val settings = SftpSettings(
    InetAddress.getByName("localhost"),
    2222,
    credentials = NonAnonFtpCredentials("sftpuser", "sftpuser"),
    strictHostKeyChecking = true,
    knownHosts = Some("/home/alessandro/.ssh/known_hosts")
  )

  val basepath: String = "/public/";

  "The Stream" should "read al present files in ftp" in {

    val sourceUnderTest: Source[FtpFile, NotUsed] = Sftp.ls(basepath, settings);

    val result = sourceUnderTest
      .log("Start reading...")
      .runWith(TestSink.probe[FtpFile])
      .requestNext()

    assert(result.isFile);
    assert(result.name equals ("data.txt"))
  }


  "The stream" should "list for file and download only the one that matches" in {
    val sourceUnderTest: Source[FtpFile, NotUsed] = Sftp.ls(basepath, settings);
    val result = sourceUnderTest
      .log("Start reading...")
      .filter(f => f.name.equals("data.txt"))
      .flatMapConcat(f => Sftp.fromPath(f.path, settings))
      .runWith(TestSink.probe)
      .requestNext()

    assert(result.nonEmpty);
    log.info("File Found. Content is: " + result.decodeString(Charset.defaultCharset()));
  }

  /*"The Stream test" should "work" in {
    import akka.pattern.pipe
    import system.dispatcher

    val sourceUnderTest = Source(1 to 4).grouped(2);
    val probe = TestProbe();
    sourceUnderTest.runWith(Sink.seq).pipeTo(probe.ref);
    probe.expectMsg(3.seconds, Seq(Seq(1, 2), Seq(3, 4)));

  }

  "The Stream test" should "Work with actorRef" in {
    case object Tick
    val sourceUnderTest = Source.tick(0.seconds, 200.millis, Tick)

    val probe = TestProbe()
    val cancellable = sourceUnderTest.to(Sink.actorRef(probe.ref, "completed")).run()

    probe.expectMsg(1.second, Tick)
    probe.expectNoMsg(100.millis)
    probe.expectMsg(3.seconds, Tick)
    cancellable.cancel()
    probe.expectMsg(3.seconds, "completed")
  }*/

  /*"The Stream testkit" should "work with both source and sink" in {
    implicit val ec = system.dispatcher;
    val flowUnderTest = Flow[Int].mapAsyncUnordered(2) { sleep =>
      pattern.after(10.millis * sleep, using = system.scheduler)(Future.successful(sleep))
    };

    val (pub, sub) = TestSource.probe[Int]
      .via(flowUnderTest)
      .toMat(TestSink.probe[Int])(Keep.both)
      .run()

    sub.request(n = 3)
    pub.sendNext(3)
    pub.sendNext(2)
    pub.sendNext(1)
    sub.expectNextUnordered(1, 2, 3)

    pub.sendError(new Exception("Power surge in the linear subroutine C-47!"))
    val ex = sub.expectError()
    assert(ex.getMessage.contains("C-47"))
  } */
}
