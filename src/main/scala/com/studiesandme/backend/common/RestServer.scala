package com.studiesandme.backend.common

import java.lang.management.{ManagementFactory, MemoryMXBean}

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.StrictLogging
import javax.inject.{Inject, Named}

import scala.collection.JavaConverters._

class RestServer @Inject() (
    @Named("rest.port") port: Int,
    restService:              RestService,
)(
    implicit actorSystem: ActorSystem,
    actorMaterializer:    ActorMaterializer,
) extends StrictLogging {

  // Function to find and log the git version of the running artifact
  def logVersion(): Unit = {
    val resourceFile = "META-INF/MANIFEST.MF"

    getClass.getClassLoader
      .getResources(resourceFile)
      .asScala
      .toList
      .map { url =>
        // Load all manifest resources
        scala.io.Source.fromURL(url, "ISO-8859-1").getLines().toList
      }
      .filter { manifestLines =>
        // Take all manifests that contain Git-Head-Rev and are published by leoilab
        manifestLines.exists(_.contains("Git-Head-Rev")) && manifestLines.contains("Implementation-Vendor: com.leoilab")
      }
      .map { lines =>
        // Take all git revisions
        lines.filter(_.contains("Git-Head-Rev")).map { line =>
          line.split(": ").last
        }
      }
      .foreach(v => logger.info(s"Starting git version: ${v.mkString}"))
  }

  def logJvmMemoryAllocation(): Unit = {
    val memoryBean: MemoryMXBean = ManagementFactory.getMemoryMXBean
    logger.info(s"JVM heap memory usage: ${memoryBean.getHeapMemoryUsage}")
    logger.info(s"JVM non-heap memory usage: ${memoryBean.getNonHeapMemoryUsage}")
  }

  def start() = {
    logVersion()
    logJvmMemoryAllocation()
    logger.info(s"Starting akka-http rest service on port: $port")

    Http().bindAndHandle(restService.route, "0.0.0.0", port)

    ()
  }
}
