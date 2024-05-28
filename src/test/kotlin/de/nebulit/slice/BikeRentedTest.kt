package de.nebulit.slice

import de.nebulit.ContainerConfiguration
import de.nebulit.common.CommandException

import de.nebulit.common.DelegatingCommandHandler
import de.nebulit.common.DelegatingQueryHandler
import de.nebulit.common.persistence.EventsEntityRepository
import de.nebulit.support.BikeRentalAggregateRepository
import de.nebulit.common.persistence.InternalEvent


import de.nebulit.slice.internal.RentBikeCommand;
import de.nebulit.events.BikeRentedEvent
import java.util.UUID
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Assertions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import de.nebulit.common.support.RandomData
import org.springframework.modulith.test.ApplicationModuleTest
import org.springframework.modulith.test.Scenario
import java.util.*

@ApplicationModuleTest(mode = ApplicationModuleTest.BootstrapMode.DIRECT_DEPENDENCIES)
@Import(ContainerConfiguration::class)
class BikeRentedTest {

    @Autowired
    lateinit var repository: EventsEntityRepository
    @Autowired
    lateinit var commandHandler: DelegatingCommandHandler
    @Autowired
    lateinit var queryHandler: DelegatingQueryHandler
    @Autowired
    lateinit var aggregateRepository: BikeRentalAggregateRepository

    @BeforeEach
    fun setUp() {
        aggregateRepository.save(RandomData.newInstance(listOf("events")) {
            this.aggregateId = AGGREGATE_ID
            this.events = mutableListOf()
        })
    }

    @Test
    fun `BikeRentedTest`(scenario: Scenario) {

       var whenResult = prepare(scenario)

       //THEN
    	whenResult.andWaitForEventOfType(BikeRentedEvent::class.java)
                        .toArrive()
    }

    private fun prepare(scenario: Scenario): Scenario.When<Void> {
      return scenario.stimulate { stimulus, eventPublisher ->
                    run {
                        stimulus.executeWithoutResult {
                            //GIVEN
                            
    var events = mutableListOf<InternalEvent>()
     
     
      events.forEach { event ->
                        run {
                            repository.save(event)
                            event.value?.let { eventPublisher.publishEvent(it) }
                        }
                    }
    
                            //WHEN
                            commandHandler.handle(RentBikeCommand(	aggregateId = UUID.fromString("008437bb-2878-43d9-a83b-a3498d1ba99f")))
                        }
                    }}
    }

    companion object {
        var AGGREGATE_ID = UUID.fromString("008437bb-2878-43d9-a83b-a3498d1ba99f")
    }

}
