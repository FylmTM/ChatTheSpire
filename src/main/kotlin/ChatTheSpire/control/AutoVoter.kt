package ChatTheSpire.control

import ChatTheSpire.command.CardCommand
import javafx.animation.AnimationTimer
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleObjectProperty
import org.apache.logging.log4j.LogManager
import java.util.concurrent.TimeUnit

enum class Phase {
    Voting,
    VotingStopped,
    Pause
}

val VOTING_SECONDS = SimpleLongProperty(TimeUnit.NANOSECONDS.convert(15, TimeUnit.SECONDS))
val VOTING_STOPPED_SECONDS = SimpleLongProperty(TimeUnit.NANOSECONDS.convert(1, TimeUnit.SECONDS))
val PAUSE_SECONDS = SimpleLongProperty(TimeUnit.NANOSECONDS.convert(5, TimeUnit.SECONDS))

private val logger = LogManager.getLogger(CardCommand::class.java.name)

class AutoVoter : AnimationTimer() {

    val active = SimpleBooleanProperty(false)
    val elapsed = SimpleLongProperty(0)
    var phase = SimpleObjectProperty(Phase.Pause)

    private var start: Long = 0

    fun begin() {
        VotingManager.stop()
        phase.set(Phase.Pause)
        elapsed.set(0)
        start = 0
        active.set(true)
    }

    fun pause() {
        active.set(false)
        VotingManager.stop()
        phase.set(Phase.Pause)
        elapsed.set(0)
    }

    override fun handle(now: Long) {
        if (active.value == true && start != 0L) {
            elapsed.set(now - start)

            when (phase.value) {
                Phase.Voting -> if (elapsed.value > VOTING_SECONDS.value) {
                    logger.info("Transition to VotingStopped")
                    VotingManager.stop()
                    phase.set(Phase.VotingStopped)
                    start = now
                }
                Phase.VotingStopped -> if (elapsed.value > VOTING_STOPPED_SECONDS.value) {
                    logger.info("Transition to Pause")
                    VotingManager.perform()
                    phase.set(Phase.Pause)
                    start = now
                }
                Phase.Pause -> if (elapsed.value > PAUSE_SECONDS.value) {
                    logger.info("Transition to Voting")
                    VotingManager.start()
                    phase.set(Phase.Voting)
                    start = now
                }
            }
        } else {
            start = now
        }
    }
}