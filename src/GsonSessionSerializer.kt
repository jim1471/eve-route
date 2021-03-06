package net.tkhamez.everoute

import com.google.gson.Gson
import io.ktor.sessions.SessionSerializer
import net.tkhamez.everoute.data.Session

class GsonSessionSerializer(
    private val type: java.lang.reflect.Type,
    private val gson: Gson = net.tkhamez.everoute.gson,
    configure: Gson.() -> Unit = {}
): SessionSerializer<Session> {
    init {
        configure(gson)
    }

    override fun serialize(session: Session): String = gson.toJson(session)
    override fun deserialize(text: String): Session = gson.fromJson(text, type)
}
