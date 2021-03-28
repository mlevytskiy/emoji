import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    application
    kotlin("jvm") version "1.3.50"
}

group = "com.example"
version = "0.0.1"

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

repositories {
    mavenLocal()
    jcenter()
}

dependencies {
    compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
    compile("io.ktor:ktor-server-netty:$ktor_version")
    compile("ch.qos.logback:logback-classic:$logback_version")
//    compile("com.ryanharter.ktor:ktor-moshi:+")
    compile("io.ktor:ktor-jackson:$ktor_version")
    compile("io.ktor:ktor-freemarker:$ktor_version")
    compile("io.ktor:ktor-auth:$ktor_version")
    compile("io.ktor:ktor-locations:$ktor_version")
    compile("org.jetbrains.exposed:exposed:0.17.4")
    compile("com.h2database:h2:1.4.199")
    compile("com.zaxxer:HikariCP:3.4.1")
    compile("org.postgresql:postgresql:+")
    compile("io.ktor:ktor-auth-jwt:$ktor_version")
    testCompile("io.ktor:ktor-server-tests:$ktor_version")
    compile("com.natpryce:konfig:+")
    compile("org.neo4j:neo4j-ogm-core:3.2.21")
    runtime("org.neo4j:neo4j-ogm-bolt-driver:3.2.21")
}

kotlin.sourceSets["main"].kotlin.srcDirs("src")
kotlin.sourceSets["test"].kotlin.srcDirs("test")

sourceSets["main"].resources.srcDirs("resources")
sourceSets["test"].resources.srcDirs("testresources")

task("stage") {
    dependsOn("installDist")
}
