import sbt._

object ElasticsearchDependencies {

  lazy val elastic4sVersion          = "8.5.2"
  lazy val `elastic4s-client-esjava` = "com.sksamuel.elastic4s" %% "elastic4s-client-esjava" % elastic4sVersion
  lazy val `elastic4s-core`          = "com.sksamuel.elastic4s" %% "elastic4s-core"          % elastic4sVersion
  lazy val `elastic4s-effect-zio`    = "com.sksamuel.elastic4s" %% "elastic4s-effect-zio"    % elastic4sVersion
  lazy val `elastic4s-json-zio`      = "com.sksamuel.elastic4s" %% "elastic4s-json-zio"      % elastic4sVersion

  lazy val `zio-prelude` = "dev.zio" %% "zio-prelude" % "1.0.0-RC16"

}
