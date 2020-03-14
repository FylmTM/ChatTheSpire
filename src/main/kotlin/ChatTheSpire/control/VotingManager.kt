package ChatTheSpire.control

import ChatTheSpire.CommandManager
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class VoteResult(command: String, count: Int) {
    val commandProperty = SimpleStringProperty(command)
    val command: String by commandProperty

    val countProperty = SimpleIntegerProperty(count)
    val count by countProperty
}

object VotingManager {

    val open = SimpleBooleanProperty(false)
    val results = mutableListOf<VoteResult>().asObservable()
    private val userCache: MutableSet<String> = HashSet()
    private val acceptedCache: MutableMap<String, VoteResult> = HashMap()
    private val rejectedCache: MutableSet<String> = HashSet()

    fun start() {
        results.clear()
        userCache.clear()
        acceptedCache.clear()
        rejectedCache.clear()
        open.set(true)
    }

    fun stop() {
        open.set(false)
    }

    fun perform() {
        results.firstOrNull()
            ?.command
            ?.let(CommandManager::perform)
    }

    fun vote(user: String, command: String) {
        synchronized(this) {
            val status = if (open.value) {
                if (userCache.contains(user)) {
                    CommandResultLogEntryStatus.SKIP
                } else if (rejectedCache.contains(command)) {
                    CommandResultLogEntryStatus.REJECTED
                } else if (acceptedCache.contains(command)) {
                    val result = acceptedCache[command]!!
                    result.countProperty.set(result.countProperty.value + 1)
                    userCache.add(user)
                    results.sortByDescending { it.count }
                    CommandResultLogEntryStatus.ACCEPTED
                } else {
                    val canPerform = CommandManager.canPerform(command)
                    if (canPerform) {
                        val result = VoteResult(command, 1)
                        results.add(result)
                        results.sortByDescending { it.count }
                        userCache.add(user)
                        acceptedCache[command] = result
                        CommandResultLogEntryStatus.ACCEPTED
                    } else {
                        rejectedCache.add(command)
                        CommandResultLogEntryStatus.REJECTED
                    }
                }
            } else {
                CommandResultLogEntryStatus.SKIP
            }

            runLater {
                CommandResultsLogController.add(CommandResultLogEntry(user, command, status))
            }
        }
    }
}
