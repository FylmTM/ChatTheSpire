package ChatTheSpire.command

import ChatTheSpire.util.Automation
import ChatTheSpire.util.Job
import ChatTheSpire.util.SafeSpire
import ChatTheSpire.util.getByPosition
import org.apache.logging.log4j.LogManager

private val logger = LogManager.getLogger(MapCommand::class.java.name)

object MapCommand : Command {

    override val prefix: String = "map"

    override val syntax: String = "map [node]"

    override fun execute(parameters: List<Int>, doAction: Boolean): Boolean {
        if (parameters.size != 1) {
            logger.info("Invalid parameters size: {}", parameters.size)
            return false
        }

        val mapNodePosition = parameters[0]

        val nextMapNodes = SafeSpire.nextMapNodes
        val mapNode = nextMapNodes.getByPosition(mapNodePosition)

        if (mapNode == null) {
            logger.info("Invalid map node position: {}", mapNodePosition)
            return false
        }

        if (doAction) {
            Job.execute {
                Automation.click(mapNode.hb)
                Automation.rest()
            }
        }

        return true
    }
}
