package de.nebulit.slice.internal

import de.nebulit.common.Command
import java.util.UUID

data class RentBikeCommand(override var aggregateId:UUID) : Command
