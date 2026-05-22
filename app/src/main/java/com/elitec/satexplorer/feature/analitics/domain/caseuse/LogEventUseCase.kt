package com.elitec.satexplorer.feature.analitics.domain.caseuse

import com.elitec.satexplorer.feature.analitics.domain.entity.EventLog
import java.util.concurrent.CopyOnWriteArrayList

class LogEventUseCase {
    private val events = CopyOnWriteArrayList<EventLog>()

    operator fun invoke(type: String, metadata: String) {
        events += EventLog(
            id = System.nanoTime(),
            type = type,
            timestamp = System.currentTimeMillis(),
            metadata = metadata
        )
    }

    fun recentEvents(limit: Int = 6): List<EventLog> {
        if (events.isEmpty()) {
            invoke("LIVE", "Tracking service initialized")
            invoke("ORBIT_LOCKED", "Primary orbit lock confirmed")
            invoke("LOW_ALTITUDE", "Low altitude pass predicted")
        }
        return events.sortedByDescending { it.timestamp }.take(limit)
    }
}