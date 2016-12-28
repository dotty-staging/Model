lazy val baseName  = "Model"
lazy val baseNameL = baseName.toLowerCase

lazy val projectVersion = "0.3.3"
lazy val mimaVersion    = "0.3.2"

name               := baseName
version            := projectVersion
organization       := "de.sciss"
scalaVersion       := "2.11.8"
crossScalaVersions := Seq("2.12.1", "2.11.8", "2.10.6")
description        := "A simple typed publisher-observer mechanism"
homepage           := Some(url(s"https://github.com/Sciss/${name.value}"))
licenses           := Seq("LGPL v2.1+" -> url("http://www.gnu.org/licenses/lgpl-2.1.txt"))

mimaPreviousArtifacts := Set("de.sciss" %% baseNameL % mimaVersion)

initialCommands in console := """import de.sciss.model._"""

libraryDependencies +=
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"

scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature", "-Xfuture", "-encoding", "utf8", "-Xlint")

// ---- publishing ----

publishMavenStyle := true

publishTo :=
  Some(if (isSnapshot.value)
    "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
  else
    "Sonatype Releases"  at "https://oss.sonatype.org/service/local/staging/deploy/maven2"
  )

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

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

// ---- ls.implicit.ly ----

// seq(lsSettings :_*)
// (LsKeys.tags   in LsKeys.lsync) := Seq("model", "observer", "publisher")
// (LsKeys.ghUser in LsKeys.lsync) := Some("Sciss")
// (LsKeys.ghRepo in LsKeys.lsync) := Some(name.value)

