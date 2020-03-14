package ChatTheSpire.command

import ChatTheSpire.util.Automation
import ChatTheSpire.util.Job
import ChatTheSpire.util.SpireInternals
import org.apache.logging.log4j.LogManager

private val logger = LogManager.getLogger(ProceedCommand::class.java.name)

object ProceedCommand : Command {

    override val prefix: String = "n"

    override val syntax: String = "n - Proceed / Skip Rewards / Skip Card"

    override fun execute(parameters: List<Int>, doAction: Boolean): Boolean {
        if (SpireInternals.proceedIsHidden) {
            logger.info("Proceed is hidden")
            return false
        }

        if (doAction) {
            Job.execute {
                SpireInternals.proceedHitbox?.let(Automation::click)
                Automation.rest()
            }
        }

        return true
    }
}
