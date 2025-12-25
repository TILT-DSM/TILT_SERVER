package org.example.tiltserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TiltServerApplication

fun main(args: Array<String>) {
    runApplication<TiltServerApplication>(*args)
}
