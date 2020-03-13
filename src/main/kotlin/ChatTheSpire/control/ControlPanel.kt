package ChatTheSpire.control

import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.geometry.Pos
import javafx.scene.layout.Priority
import javafx.scene.text.FontWeight
import javafx.stage.Stage
import tornadofx.*

fun main(args: Array<String>) {
    launch<ControlPanel>(args)
}

class ControlPanel : App(ControlView::class) {
    override fun start(stage: Stage) {
        super.start(stage)
    }

    override fun stop() {
        super.stop()
    }
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
    val autoVoter: AutoVoter = AutoVoter()

    override val root = vbox {
        spacing = 10.0
        hbox {
            alignment = Pos.CENTER_LEFT
            spacing = 5.0
            hiddenWhen(autoVoter.active)
            managedWhen(visibleProperty())
            label("Voting") {
                prefWidth = 70.0
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
        hbox {
            alignment = Pos.CENTER_LEFT
            spacing = 5.0
            label("Auto") {
                prefWidth = 70.0
                style {
                    fontSize = 20.px
                    fontWeight = FontWeight.BOLD
                }
            }
            button("Start") {
                disableWhen(autoVoter.active)
                action {
                    autoVoter.begin()
                }
            }
            button("Stop") {
                disableWhen(autoVoter.active.not())
                action {
                    autoVoter.pause()
                }
            }
        }
        hbox {
            alignment = Pos.CENTER_LEFT
            spacing = 5.0
            label {
                style {
                    fontSize = 20.px
                    fontWeight = FontWeight.BOLD
                }
                textProperty().bind(autoVoter.phase.stringBinding {
                    when (it) {
                        Phase.Voting -> "Vote!"
                        Phase.VotingStopped -> "Stop."
                        Phase.Pause -> "Wait..."
                        else -> "Wait..."
                    }
                })
                prefWidth = 70.0
            }
            val bar = progressbar {
                prefWidth = 170.0
                prefHeight = 50.0
                progressProperty().bind(autoVoter.elapsed.doubleBinding {
                    when (autoVoter.phase.value) {
                        Phase.Voting -> it!!.toDouble() / VOTING_SECONDS.toDouble()
                        Phase.VotingStopped -> it!!.toDouble() / VOTING_STOPPED_SECONDS.toDouble()
                        Phase.Pause -> it!!.toDouble() / PAUSE_SECONDS.toDouble()
                    }
                })
            }
            autoVoter.phase.addListener { _, _, new ->
                when (new) {
                    Phase.Voting -> bar.style {
                        accentColor = c("#3DCC91")
                    }
                    Phase.VotingStopped -> bar.style {
                        accentColor = c("#FF7373")
                    }
                    Phase.Pause -> bar.style {
                        accentColor = c("#8A9BA8")
                    }
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

    init {
        autoVoter.start()
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
