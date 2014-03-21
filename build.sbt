name := "Model"

version := "0.3.2-SNAPSHOT"

organization := "de.sciss"

scalaVersion := "2.11.0-RC3"

description := "A simple typed publisher-observer mechanism"

homepage <<= name { n => Some(url("https://github.com/Sciss/" + n)) }

licenses := Seq("LGPL v2.1+" -> url("http://www.gnu.org/licenses/lgpl-2.1.txt"))

initialCommands in console := """import de.sciss.model._"""

libraryDependencies in ThisBuild ++= Seq(
  "org.scalatest" %% "scalatest" % "2.1.2" % "test"
)

// retrieveManaged := true

scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature")

// ---- publishing ----

publishMavenStyle := true

publishTo <<= version { v =>
  Some(if (v endsWith "-SNAPSHOT")
    "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
  else
    "Sonatype Releases"  at "https://oss.sonatype.org/service/local/staging/deploy/maven2"
  )
}

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra <<= name { n =>
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

seq(lsSettings :_*)

(LsKeys.tags in LsKeys.lsync) := Seq("model", "observer", "publisher")

(LsKeys.ghUser in LsKeys.lsync) := Some("Sciss")

(LsKeys.ghRepo in LsKeys.lsync) <<= name(Some(_))

