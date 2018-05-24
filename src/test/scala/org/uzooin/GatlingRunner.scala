package org.uzooin

import io.gatling.app.Gatling
import io.gatling.core.config.GatlingPropertiesBuilder

// https://james-willett.com/2018/05/debug-gatling/
object GatlingRunner {
  def main(args: Array[String]): Unit = {
    val simClass = classOf[RecordedSimulation].getName
    println(simClass)

    val prop = new GatlingPropertiesBuilder
    prop.simulationClass(simClass)
    println(prop)

    Gatling.fromMap(prop.build)
  }

}
