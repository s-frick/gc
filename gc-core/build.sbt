name := "gc-core"
version := "0.1.0"
scalaVersion := "3.3.6"

// Für Java-Kompatibilität
crossPaths := false
Compile / compile / scalacOptions ++= Seq("-deprecation", "-feature")

unmanagedJars in Compile += baseDirectory.value / "lib" / "FitSdk" / "fit.jar"

// Falls du JSON, CSV o.Ä. brauchst
libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-json" % "2.10.0",  // Beispiel für JSON
  "org.scalatest" %% "scalatest" % "3.2.18" % Test
)
