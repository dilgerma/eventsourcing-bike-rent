package de.nebulit.events

import de.nebulit.common.Event

import java.util.UUID

data class BikeRentedEvent(var aggregateId:UUID) : Event
