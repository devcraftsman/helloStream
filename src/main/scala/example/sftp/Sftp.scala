package example.sftp

import java.net.InetAddress

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.alpakka.ftp.FtpCredentials.NonAnonFtpCredentials
import akka.stream.alpakka.ftp.SftpSettings

/**
  * Created by devcraftsman on 10/16/17.
  * ----------------------------------------------------
  * This software is licensed under the Apache 2 license
  * see: [http://www.apache.org/licenses/LICENSE-2.0]
  **/
object Sftp {

  def run()(implicit system: ActorSystem, materializer: ActorMaterializer): Unit = {

    val settings = SftpSettings(
      InetAddress.getLocalHost,
      2222,
      credentials = NonAnonFtpCredentials("sftpuser", "sftpuser")
    )



  }
}
