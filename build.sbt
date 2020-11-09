lazy val baseName  = "Model"
lazy val baseNameL = baseName.toLowerCase

lazy val projectVersion = "0.3.5"
lazy val mimaVersion    = "0.3.3"

lazy val deps = new {
  val test = new {
    val scalaTest = "3.2.3"
  }
}

lazy val commonJvmSettings = Seq(
  crossScalaVersions := Seq("3.0.0-M1", "2.13.3", "2.12.12"),
)

lazy val commonSettings = Seq(
  name               := baseName,
  version            := projectVersion,
  organization       := "de.sciss",
  scalaVersion       := "2.13.3",
  description        := "A simple typed publisher-observer mechanism",
  homepage           := Some(url(s"https://github.com/Sciss/${name.value}")),
  licenses           := Seq("LGPL v2.1+" -> url("http://www.gnu.org/licenses/lgpl-2.1.txt")),
  scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature", "-encoding", "utf8", "-Xlint", "-Xsource:2.13"),
)

lazy val root = crossProject(JSPlatform, JVMPlatform).in(file("."))
  .settings(commonSettings)
  .jvmSettings(commonJvmSettings)
  .settings(publishSettings)
  .settings(
    mimaPreviousArtifacts := Set("de.sciss" %% baseNameL % mimaVersion),
    initialCommands in console := """import de.sciss.model._""",
    libraryDependencies ++= Seq(
      "org.scalatest" %%% "scalatest" % deps.test.scalaTest % Test
    )
  )

// ---- publishing ----

lazy val publishSettings = Seq(
  publishMavenStyle := true,
  publishTo := {
    Some(if (isSnapshot.value)
      "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
    else
      "Sonatype Releases" at "https://oss.sonatype.org/service/local/staging/deploy/maven2"
    )
  },
  publishArtifact in Test := false,
  pomIncludeRepository := { _ => false },
  pomExtra := { val n = name.value
  <scm>
    <url>git@github.com:Sciss/{n}.git</url>
    <connection>scm:git:git@github.com:Sciss/{n}.git</connection>
  </scm>
    <developers>
      <developer>
        <id>sciss</id>
        <name>Hanns Holger Rutz</name>
        <url>http://www.sciss.de</url>
      </developer>
    </developers>
  }
)
