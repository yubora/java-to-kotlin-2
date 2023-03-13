package com.group.libraryapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LibraryAppApplication

// Kotlin에서 top level 함수는 static 함수로 간주된다.
fun main(args: Array<String>) {
    runApplication<LibraryAppApplication>(*args)
}