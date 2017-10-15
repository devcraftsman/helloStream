package example

import org.scalatest._
import akka.stream._
import akka.stream.scaladsl._
import akka.{ NotUsed, Done }
import akka.actor.ActorSystem
import akka.testkit.{ ImplicitSender, TestActors, TestKit,TestProbe }
import akka.util.ByteString
import akka.{pattern}
import scala.concurrent._
import scala.concurrent.duration._
import akka.stream.testkit.scaladsl.{ TestSink, TestSource }

class StreamSpec extends FlatSpec with Matchers {
  implicit val system = akka.actor.ActorSystem("system");
  implicit val materializer = ActorMaterializer();

  "The Stream test" should "work" in {
    import system.dispatcher
    import akka.pattern.pipe

    val sourceUnderTest = Source(1 to 4).grouped(2);
    val probe = TestProbe();
    sourceUnderTest.runWith(Sink.seq).pipeTo(probe.ref);
    probe.expectMsg(3.seconds, Seq(Seq(1, 2), Seq(3, 4)));

  }

  "The Stream test" should "Work with actorRedf" in {
    case object Tick
    val sourceUnderTest = Source.tick(0.seconds, 200.millis, Tick)

    val probe = TestProbe()
    val cancellable = sourceUnderTest.to(Sink.actorRef(probe.ref, "completed")).run()

    probe.expectMsg(1.second, Tick)
    probe.expectNoMsg(100.millis)
    probe.expectMsg(3.seconds, Tick)
    cancellable.cancel()
    probe.expectMsg(3.seconds, "completed")
  }

  "The Stream testkit" should "work with SinkProbe" in {
    val sourceUnderTest = Source(1 to 4).filter(_ % 2 == 0).map(_ * 2)

    sourceUnderTest
    .runWith(TestSink.probe[Int])
    .request(2)
    .expectNext(4, 8)
    .expectComplete()
  }

  "The Stream testkit" should "work with both source and sink" in {
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
  }
}
