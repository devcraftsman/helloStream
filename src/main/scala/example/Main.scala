package example

import akka.stream._
import akka.actor.ActorSystem

object Main extends App {

  implicit val system = ActorSystem("QuickStart");
  implicit val materializer = ActorMaterializer();


  Hello.run();

}
