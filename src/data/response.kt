/**
 * Application API responses.
 *
 * These have JS counterparts located in frontend/src/data.js
 */

package net.tkhamez.everoute.data

import net.tkhamez.everoute.Route
import java.util.Date

enum class ResponseCodes {
    AlreadyUpdated,
    AuthError,
    ConnectionStored,
    FailedToStoreData,
    LoginEsiErrorAlliance,
    LoginEsiErrorVerify,
    LoginNoAlliance,
    LoginSsoFailed,
    LoginWrongAlliance,
    MissingInput,
    SearchError,
    SearchSuccess,
    SetWaypointsSuccess,
    SetWaypointsFailure,
    SystemNotFound,
    WrongSearchTerm,
}

data class ResponseMessage(
    var code: ResponseCodes? = null,
    var success: Boolean? = null,
    var param: String? = null
)

data class ResponseAuthUser(
    val name: String,
    val allianceName: String,
    val allianceTicker: String,
)

data class ResponseGates(
    var code: ResponseCodes? = null,
    var ansiblexes: MutableList<MongoAnsiblex> = mutableListOf()
)

data class ResponseSystems(
    var systems: MutableList<System> = mutableListOf()
)

data class ResponseConnectedSystems(
    var connections: MutableList<ConnectedSystems> = mutableListOf()
)

data class ResponseMapConnections(
    var code: ResponseCodes? = null,
    val ansiblexes: MutableList<ConnectedSystems> = mutableListOf(),
    val temporary: MutableList<ConnectedSystems> = mutableListOf(),
)

data class ResponseGatesUpdated(
    var code: ResponseCodes? = null,
    var allianceId: Int? = null,
    var updated: Date? = null
)

data class ResponseTemporaryConnections(
    var code: ResponseCodes? = null,
    var temporaryConnections: MutableList<MongoTemporaryConnection> = mutableListOf()
)

data class ResponseSystemNames(
    var systems: MutableList<String> = mutableListOf()
)

data class ResponseRouteLocation(
    var code: ResponseCodes? = null,
    var solarSystemId: Int? = null,
    var solarSystemName: String? = null
)

data class ResponseRouteFind(
    var code: ResponseCodes? = null,
    var route: List<Route.Waypoint> = listOf()
)
