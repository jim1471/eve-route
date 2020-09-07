package net.tkhamez.everoute.routes

import com.mongodb.MongoException
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import net.tkhamez.everoute.Graph
import net.tkhamez.everoute.Mongo
import net.tkhamez.everoute.data.*
import net.tkhamez.everoute.gson
import java.util.*

fun Route.connection(config: Config) {
    post("/api/connection/add") {
        val response = ResponseMessage(success = false)

        val characterId = call.sessions.get<Session>()?.esiVerify?.CharacterID
        if (characterId == null) { // should not happen because of auth intercept
            response.code = ResponseCodes.AuthError
            call.respondText(gson.toJson(response), contentType = ContentType.Application.Json)
            return@post
        }

        val body = call.receiveText()
        val systems = gson.fromJson(body, Array<String>::class.java)

        if (systems.size != 2 || systems[0].isEmpty() || systems[1].isEmpty()) {
            response.code = ResponseCodes.MissingInput
            call.respondText(gson.toJson(response), contentType = ContentType.Application.Json)
            return@post
        }

        val graph = Graph()
        val system1 = graph.findSystem(systems[0])
        val system2 = graph.findSystem(systems[1])

        if (system1 == null || system2 == null) {
            response.code = ResponseCodes.SystemNotFound
            call.respondText(gson.toJson(response), contentType = ContentType.Application.Json)
            return@post
        }

        val connection = TemporaryConnection(system1.id, system1.name, system2.id, system2.name, characterId, Date())
        try {
            Mongo(config.db).temporaryConnectionStore(connection)
        } catch (e: MongoException) {
            response.code = ResponseCodes.FailedToStoreData
        }

        if (response.code == null) {
            response.code = ResponseCodes.ConnectionStored
            response.success = true
        }
        call.respondText(gson.toJson(response), contentType = ContentType.Application.Json)
    }

    delete("/api/connection/delete/{system1Id}/{system2Id}") {
        Mongo(config.db).temporaryConnectionDelete(
            call.parameters["system1Id"].toString().toInt(),
            call.parameters["system2Id"].toString().toInt()
        )
        call.respondText(gson.toJson(null), contentType = ContentType.Application.Json)
    }

    get("/api/connection/get-all") {
        val response = ResponseTemporaryConnections()

        val characterId = call.sessions.get<Session>()?.esiVerify?.CharacterID
        if (characterId == null) { // should not happen because of auth intercept
            response.code = ResponseCodes.AuthError
            call.respondText(gson.toJson(response), contentType = ContentType.Application.Json)
            return@get
        }

        val mongo = Mongo(config.db)
        mongo.temporaryConnectionsDeleteAllExpired()
        mongo.temporaryConnectionsGet(characterId).forEach {
            response.temporaryConnections.add(it)
        }

        call.respondText(gson.toJson(response), contentType = ContentType.Application.Json)
    }
}
