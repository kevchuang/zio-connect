import sbt._

object ElasticsearchDependencies {

  lazy val `elastic4s-core`     = "com.sksamuel.elastic4s" %% "elastic4s-core"     % "8.4.4"
  lazy val `elastic4s-json-zio` = "com.sksamuel.elastic4s" %% "elastic4s-json-zio" % "8.4.4"

  lazy val `zio-prelude` = "dev.zio" %% "zio-prelude" % "1.0.0-RC16"

}
