organization := "rocks.frick"
name := "gc-core"
version := "1.0.1-SNAPSHOT"
scalaVersion := "3.3.6"

// Für Java-Kompatibilität
crossPaths := false
Compile / compile / scalacOptions ++= Seq("-deprecation", "-feature")
Compile / compile / javacOptions ++= Seq("-source", "21", "-target", "21")

Compile / unmanagedJars += baseDirectory.value / "lib" / "FitSdk" / "fit.jar"

resolvers += "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository"
libraryDependencies ++= Seq(
  "rocks.frick" % "gc-core-api" % "1.0.1-SNAPSHOT" % "compile",
  "com.typesafe.play" %% "play-json" % "2.10.0",  // Beispiel für JSON
  "org.scalatest" %% "scalatest" % "3.2.18" % Test
)



enablePlugins(AssemblyPlugin)
// Für sbt-assembly: Konflikte auflösen
assembly / assemblyMergeStrategy := {
  case PathList("META-INF", "versions", "9", "module-info.class") => MergeStrategy.first
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}

// // Verwende das assembly-JAR beim publish
// Compile / packageBin := assembly.value
assembly / assemblyJarName := s"${name.value}-assembly-${version.value}.jar"
// assembly / assemblyJarName := "gc-core-fat.jar"
assembly / test := {}  // Überspringe Tests beim assembly

// Optional: fit.jar in Assembly-JAR einfügen
Compile / unmanagedJars += baseDirectory.value / "lib" / "FitSdk" / "fit.jar"
