package com.tobeict.intergamma.scheduler

import com.tobeict.intergamma.service.ItemService
import io.micrometer.core.annotation.Timed
import mu.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.concurrent.TimeUnit

private val logger = KotlinLogging.logger {}

@Component
class ReservationScheduler(private val itemService: ItemService) {

    @Timed(value = "intergamma_clear_reservations")
    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.MINUTES)
    fun clearReservations() {
        logger.info { "Scheduled clearing of old reservations" }
        itemService.clearReservationUntil(Instant.now().minus(5, TimeUnit.MINUTES.toChronoUnit()))
    }
}