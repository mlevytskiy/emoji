package com.example

import com.example.api.*
import com.example.model.EPSession
import com.example.model.WumfUser
import com.example.repository.*
import com.fasterxml.jackson.databind.SerializationFeature
import freemarker.cache.ClassTemplateLoader
import io.ktor.application.*
import io.ktor.auth.Authentication
import io.ktor.auth.authentication
import io.ktor.auth.jwt.jwt
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.features.StatusPages
import io.ktor.freemarker.FreeMarker
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.*
import io.ktor.jackson.jackson
import io.ktor.locations.Locations
import io.ktor.locations.locations
import io.ktor.request.header
import io.ktor.request.host
import io.ktor.response.respondRedirect
import io.ktor.response.respondText

import io.ktor.routing.routing
import io.ktor.sessions.SessionTransportTransformerMessageAuthentication
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import java.net.URI
import java.util.concurrent.TimeUnit

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    install(DefaultHeaders)
    install(StatusPages) {
        exception<Throwable> { e ->
            call.respondText(e.localizedMessage, ContentType.Text.Plain, HttpStatusCode.InternalServerError)
        }
    }
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }

    install(Locations)

    install(Sessions) {
        cookie<EPSession>("session") {
            transform(SessionTransportTransformerMessageAuthentication(hashKey))
        }
    }

    val db = WumfUsersRepository()
    val neo4jRepository = Neo4jRepositoryImpl()
    DatabaseFactory.init()

    val jwtService = JwtService()
    var user: WumfUser? = null

    install(Authentication) {
        jwt("jwt") {
            verifier(jwtService.verifier)
            realm = "emojiphrases app"
            validate {
                val payload = it.payload
                val claim = payload.getClaim("id")
                val claimInt = claim.asInt()
                user = db.user(claimInt)
                user
            }
        }
    }

    routing {
        static("static") {
            resources("images")
        }

        // API
        login(neo4jRepository, jwtService, db)
        registration(neo4jRepository, jwtService, db)
        reg2(neo4jRepository, jwtService)
        checkRegistration(neo4jRepository)
        addApp(neo4jRepository)
        removeApp(neo4jRepository)
        getApps(neo4jRepository)
        getFriends(neo4jRepository)
        getNotMyApps(neo4jRepository)
    }
}

const val API_VERSION = "/api/v1"

suspend fun ApplicationCall.redirect(location: Any) {
    respondRedirect(application.locations.href(location))
}

fun ApplicationCall.refererHost() = request.header(HttpHeaders.Referrer)?.let { URI.create(it).host }

fun ApplicationCall.securityCode(date: Long, user: WumfUser, hashFunction: (String) -> String) =
    hashFunction("$date:${user.telegramId}:${request.host()}:${refererHost()}")

fun ApplicationCall.verifyCode(date:Long, user: WumfUser, code:String, hashFunction: (String) -> String) =
    securityCode(date, user, hashFunction) == code &&
            (System.currentTimeMillis() - date).let { it > 0 && it < TimeUnit.MILLISECONDS.convert(2, TimeUnit.HOURS) }

val ApplicationCall.apiUser get() = authentication.principal<WumfUser>()
