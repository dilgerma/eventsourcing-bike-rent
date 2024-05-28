package de.nebulit.domain

import de.nebulit.common.AggregateRoot
import de.nebulit.common.persistence.InternalEvent
import de.nebulit.events.BikeRentedEvent
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import java.sql.Types
import java.time.LocalDate
import java.util.*
import kotlin.jvm.Transient

@Entity
@Table(name = "aggregates")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "Discriminator", discriminatorType = DiscriminatorType.STRING, length = 20)
@DiscriminatorValue("BikeRentalAggregate")
class BikeRentalAggregate(
    @JdbcTypeCode(Types.VARCHAR) @Id override var aggregateId: UUID
) : AggregateRoot {

    override var version: Long? = 0

    @Transient
    override var events: MutableList<InternalEvent> = mutableListOf()

    override fun applyEvents(events: List<InternalEvent>): AggregateRoot {
        return this
    }

    fun rent() {
        // validate availability
        this.events.add(InternalEvent().apply {
            this.aggregateId = this@BikeRentalAggregate.aggregateId
            this.value = BikeRentedEvent(this@BikeRentalAggregate.aggregateId)
        })
    }

}
