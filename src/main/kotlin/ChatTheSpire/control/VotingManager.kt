package ChatTheSpire.control

import ChatTheSpire.CommandManager
import ChatTheSpire.normalizeCommand
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
        val result = results.firstOrNull() ?: return

        val randomResultWithSameCount = results
            .filter {it.count == result.count}
            .shuffled()
            .firstOrNull()

        randomResultWithSameCount
            ?.command
            ?.let(CommandManager::perform)
    }

    fun vote(user: String, command: String) {
        synchronized(this) {
            if (open.value) {
                @Suppress("NAME_SHADOWING")
                val command = normalizeCommand(command)

                val status = if (userCache.contains(user)) {
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
                runLater {
                    CommandResultsLogController.add(CommandResultLogEntry(user, command, status))
                }
            } else {
                CommandResultLogEntryStatus.SKIP
                runLater {
                    CommandResultsLogController.add(CommandResultLogEntry(user, command, CommandResultLogEntryStatus.SKIP))
                }
            }
        }
    }
}
