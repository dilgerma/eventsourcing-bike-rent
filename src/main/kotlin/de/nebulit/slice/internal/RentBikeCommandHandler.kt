package de.nebulit.slice.internal

import de.nebulit.common.*
import de.nebulit.common.persistence.InternalEvent
import org.springframework.stereotype.Component
import de.nebulit.domain.BikeRentalAggregate
import java.util.UUID
import mu.KotlinLogging
import org.springframework.transaction.annotation.Transactional


@Component
class RentBikeCommandCommandHandler(
    private var aggregateService: AggregateService<BikeRentalAggregate>,
) : BaseCommandHandler<BikeRentalAggregate>(aggregateService) {

    var logger = KotlinLogging.logger {}

    @Transactional
    override fun handle(inputCommand: Command): List<InternalEvent> {
        assert(inputCommand is RentBikeCommand)
        val command = inputCommand as RentBikeCommand
        val aggregate = findAggregate(command.aggregateId)
        aggregate.rent()
        // TODO process logic
        aggregateService.persist(aggregate)
        return aggregate.events
    }

    override fun supports(command: Command): Boolean {
        return command is RentBikeCommand
    }

}
