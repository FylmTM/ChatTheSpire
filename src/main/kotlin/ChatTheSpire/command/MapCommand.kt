package ChatTheSpire.command

import ChatTheSpire.control.Control
import ChatTheSpire.control.Job
import ChatTheSpire.util.Spire
import ChatTheSpire.util.getByPosition
import org.apache.logging.log4j.LogManager

private val logger = LogManager.getLogger(MapCommand::class.java.name)

object MapCommand : Command {

    override fun execute(parameters: List<Int>, doAction: Boolean): Boolean {
        if (parameters.size != 1) {
            logger.info("Invalid parameters size: {}", parameters.size)
            return false
        }

        val mapNodePosition = parameters[0]

        val nextMapNodes = Spire.nextMapNodes
        val mapNode = nextMapNodes.getByPosition(mapNodePosition)

        if (mapNode == null) {
            logger.info("Invalid map node position: {}", mapNodePosition)
            return false
        }

        if (doAction) {
            Job.execute {
                Control.click(mapNode.hb)
            }
        }

        return true
    }
}
