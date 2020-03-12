package ChatTheSpire.control

import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.geometry.Pos
import javafx.scene.layout.Priority
import javafx.scene.text.FontWeight
import tornadofx.*

fun main(args: Array<String>) {
    launch<ControlPanel>(args)
}

class ControlPanel : App(ControlView::class) {

}

class ControlView : View() {

    override val root = vbox() {
        prefWidth = 250.0
        spacing = 10.0
        vgrow = Priority.ALWAYS
    }

    init {
        root += find(VotingView::class)
        root += find(CommandLogView::class)
    }
}

class VotingView : View() {
    override val root = vbox {
        hbox {
            alignment = Pos.CENTER_LEFT
            spacing = 5.0
            label("Voting") {
                style {
                    fontSize = 20.px
                    fontWeight = FontWeight.BOLD
                }
            }
            button("Start") {
                disableWhen(VotingManager.open)
                action {
                    VotingManager.start()
                }
            }
            button("Stop") {
                disableWhen(VotingManager.open.not())
                action {
                    VotingManager.stop()
                }
            }
            button("Action!") {
                disableWhen(VotingManager.open)
                action {
                    VotingManager.perform()
                }
            }
        }
        tableview(VotingManager.results) {
            prefHeight = 150.0
            column("Command", VoteResult::commandProperty) {
                prefWidth = 180.0
            }
            column("Count", VoteResult::countProperty) {
                prefWidth = 70.0
            }
        }
    }
}

class CommandLogView : View() {
    val input = SimpleStringProperty("")

    override val root = vbox {
        vgrow = Priority.ALWAYS
        textfield(input) {
            promptText = "Command"
            action {
                if (input.value.isNotBlank()) {
                    VotingManager.vote("mrloqo", input.value)
                    input.value = ""
                }
            }
        }
        listview(CommandResultsLogController.logEntries) {
            vgrow = Priority.ALWAYS
            cellFormat {
                text = "${it.user}: ${it.command}"
                style {
                    backgroundColor += when (it.status) {
                        CommandResultLogEntryStatus.ACCEPTED -> c("#3DCC91")
                        CommandResultLogEntryStatus.REJECTED -> c("#FF7373")
                        CommandResultLogEntryStatus.SKIP -> c("#8A9BA8")
                    }
                }
            }
        }
    }
}

enum class CommandResultLogEntryStatus {
    ACCEPTED,
    REJECTED,
    SKIP,
}

data class CommandResultLogEntry(val user: String, val command: String, val status: CommandResultLogEntryStatus)

object CommandResultsLogController : Controller() {

    val logEntries: ObservableList<CommandResultLogEntry> = FXCollections.observableArrayList()

    fun add(logEntry: CommandResultLogEntry) {
        logEntries.add(0, logEntry)
        if (logEntries.size > 200) {
            logEntries.remove(200, logEntries.size)
        }
    }
}
