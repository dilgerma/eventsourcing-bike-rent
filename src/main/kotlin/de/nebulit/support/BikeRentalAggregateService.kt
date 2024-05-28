package de.nebulit.support

import de.nebulit.common.AggregateService
import de.nebulit.common.persistence.EventsEntityRepository
import de.nebulit.common.persistence.InternalEvent
import de.nebulit.domain.BikeRentalAggregate
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.context.ApplicationEventPublisher
import java.util.UUID

interface BikeRentalAggregateRepository : CrudRepository<BikeRentalAggregate, Long> {
    fun findByAggregateId(aggregateId: UUID): BikeRentalAggregate?
}


@Component
class BikeRentalAggregateService(
    val repository: BikeRentalAggregateRepository,
    val eventsEntityRepository: EventsEntityRepository,
    val applicationEventPublisher: ApplicationEventPublisher
) : AggregateService<BikeRentalAggregate> {

    @Transactional
    override fun persist(aggregate: BikeRentalAggregate) {
        repository.save(aggregate)
        if (aggregate.events.isNotEmpty()) {
            eventsEntityRepository.saveAll(aggregate.events)
            aggregate.events.forEach {
                applicationEventPublisher.publishEvent(it.value as Any)
            }
        }

    }

    override fun findByAggregateId(aggregateId: UUID): BikeRentalAggregate? {
        return repository.findByAggregateId(aggregateId)
    }

    override fun findEventsByAggregateId(aggregateId: UUID): List<InternalEvent> {
        return eventsEntityRepository.findByAggregateIdAndIdGreaterThanOrderByIdAsc(
            aggregateId, 0)
    }

}
