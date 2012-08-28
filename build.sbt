name := "Slogger"

version := "1.0"

resolvers ++= Seq("snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
                    "releases"  at "http://oss.sonatype.org/content/repositories/releases",
                    "Java.net Maven2 Repository" at "http://download.java.net/maven/2/",
                    "TypeSafe" at "http://repo.typesafe.com/typesafe/releases/")
                    
libraryDependencies += "org.mongodb" % "casbah_2.9.2" % "2.4.1"

seq(webSettings: _*)

// If using JRebel with 0.1.0 of the sbt web plugin
//jettyScanDirs := Nil
// using 0.2.4+ of the sbt web plugin
//scanDirectories in  := Nil

//resolvers += "Java.net Maven2 Repository" at "http://download.java.net/maven/2/"


// if you have issues pulling dependencies from the scala-tools repositories (checksums don",t match), you can disable checksums
//checksums := Nil

libraryDependencies ++= {
  val liftVersion = "2.4" // Put the current/latest lift version here
  Seq(
    "net.liftweb" %% "lift-webkit" % liftVersion ,
    "net.liftweb" %% "lift-mapper" % liftVersion ,
    "net.liftweb" %% "lift-wizard" % liftVersion
    //,"org.eclipse.jetty" % "jetty" % "8.0.4.v20111024"
     )
}

classpathTypes ~= (_ + "orbit")

libraryDependencies ++= Seq(
  "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016"      artifacts (Artifact("javax.servlet", "jar", "jar")
  )
)

libraryDependencies ++= Seq(
  "org.eclipse.jetty" % "jetty-webapp" % "8.1.4.v20120524" artifacts (Artifact("jetty-webapp", "jar", "jar")),
  "org.eclipse.jetty" % "jetty-server" % "8.1.4.v20120524"  artifacts (Artifact("jetty-server", "jar", "jar")))

libraryDependencies += "com.typesafe" % "slick_2.10.0-M6" % "0.11.0"

// when using the sbt web app plugin 0.2.4+, use "container" instead of "jetty" for the context
// Customize any further dependencies as desired
libraryDependencies ++= Seq(
  "com.google.protobuf" % "protobuf-java" % "2.3.0",
  "org.eclipse.jetty" % "jetty-webapp" % "8.0.4.v20111024" % "container",
  "org.scala-tools.testing" % "specs_2.9.0" % "1.6.8" % "test", // For specs.org tests
  "junit" % "junit" % "4.8" % "test->default", // For JUnit 4 testing
  "javax.servlet" % "servlet-api" % "2.5" % "provided->default",
  "com.h2database" % "h2" % "1.2.138", // In-process database, useful for development systems
  "ch.qos.logback" % "logback-classic" % "0.9.26", // Logging
  "com.basho.riak" % "riak-client" % "1.0.5",
   "com.typesafe" % "config" % "0.5.0",
   "com.typesafe.akka" % "akka-actor" % "2.0",
   "com.typesafe.akka" % "akka-remote" % "2.0",
   "com.typesafe.akka" % "akka-kernel" % "2.0")

// by default, it listens on port 8080; use the following to override
port in container.Configuration := 8081
