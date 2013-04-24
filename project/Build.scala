import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "push2show"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    "org.mongodb" % "mongo-java-driver" % "2.7.3",
    "commons-io" % "commons-io" % "2.3",
    javaCore,
    javaJdbc,
    javaEbean
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
