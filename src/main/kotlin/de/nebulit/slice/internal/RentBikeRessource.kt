package de.nebulit.slice.internal

import de.nebulit.common.DelegatingCommandHandler
import de.nebulit.slice.internal.RentBikeCommand
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import mu.KotlinLogging


@RestController
class SliceRessource(private var commandHandler: DelegatingCommandHandler) {

    var logger = KotlinLogging.logger {}

    
    @PostMapping("slice")
    fun processCommand(@RequestParam aggregateId:UUID) {
        commandHandler.handle(RentBikeCommand(aggregateId))
    }
    

}
