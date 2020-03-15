package ChatTheSpire.control

import ChatTheSpire.chat.Discord
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.geometry.Pos
import javafx.scene.layout.Priority
import javafx.scene.text.FontWeight
import tornadofx.*
import java.util.concurrent.TimeUnit

fun main(args: Array<String>) {
    Discord.listen()
    launch<ControlPanel>(args)
}

class ControlPanel : App(ControlView::class)

class ControlView : View() {

    override val root = vbox {
        prefWidth = 250.0
        spacing = 10.0
        vgrow = Priority.ALWAYS
        style {
            padding = box(5.px)
        }
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
            textfield(VOTING_SECONDS) {
                prefWidth = 30.0
            }
            textfield(PAUSE_SECONDS) {
                prefWidth = 30.0
            }
        }
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
        borderpane {
            hiddenWhen(autoVoter.active.not())
            managedWhen(visibleProperty())
            left {
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
                                Phase.Voting -> "Vote now!"
                                Phase.VotingStopped -> "Voting closed."
                                Phase.Pause -> "Wait..."
                                else -> "Wait..."
                            }
                        })
                    }
                }
            }
            right {
                hbox {
                    alignment = Pos.CENTER_RIGHT
                    spacing = 5.0
                    label {
                        style {
                            fontSize = 20.px
                            fontWeight = FontWeight.BOLD
                        }
                        textProperty().bind(autoVoter.elapsed.stringBinding {
                            val total = when (autoVoter.phase.value!!) {
                                Phase.Voting -> VOTING_SECONDS
                                Phase.VotingStopped -> VOTING_STOPPED_SECONDS
                                Phase.Pause -> PAUSE_SECONDS
                            }.value
                            val left = (TimeUnit.SECONDS.convert(total - it!!.toLong(), TimeUnit.NANOSECONDS) + 1)
                            if (left > 0) {
                                left.toString()
                            } else {
                                "∞"
                            }
                        })
                    }
                }
            }
        }
        val bar = progressbar {
            hiddenWhen(autoVoter.active.not())
            managedWhen(visibleProperty())
            prefWidth = Double.MAX_VALUE
            minHeight = 50.0
            prefHeight = 50.0
            progressProperty().bind(autoVoter.elapsed.doubleBinding {
                when (autoVoter.phase.value!!) {
                    Phase.Voting -> it!!.toDouble() / VOTING_SECONDS.value.toDouble()
                    Phase.VotingStopped -> it!!.toDouble() / VOTING_STOPPED_SECONDS.value.toDouble()
                    Phase.Pause -> it!!.toDouble() / PAUSE_SECONDS.value.toDouble()
                }
            })
            style {
                accentColor = c("#8A9BA8")
            }
        }
        autoVoter.phase.addListener { _, _, new ->
            when (new!!) {
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
        tableview(VotingManager.results) {
            minHeight = 150.0
            prefHeight = 200.0
            style {
                fontSize = 20.px
            }
            column("Command", VoteResult::commandProperty) {
                prefWidth = 160.0
            }
            column("Votes", VoteResult::countProperty) {
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
        spacing = 10.0
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

    fun clear() {
        logEntries.clear()
    }
}
