package ChatTheSpire

import ChatTheSpire.command.CardCommand
import ChatTheSpire.command.Command
import ChatTheSpire.command.DialogCommand
import ChatTheSpire.command.EndTurnCommand
import ChatTheSpire.command.MapCommand
import ChatTheSpire.command.PotionDestroyCommand
import ChatTheSpire.command.PotionUseCommand
import ChatTheSpire.console.CardConsoleCommand
import ChatTheSpire.console.DialogConsoleCommand
import ChatTheSpire.console.EndTurnConsoleCommand
import ChatTheSpire.console.MapConsoleCommand
import ChatTheSpire.console.PotionDestroyConsoleCommand
import ChatTheSpire.console.PotionUseConsoleCommand
import ChatTheSpire.console.TestConsoleCommand
import basemod.devcommands.ConsoleCommand
import org.apache.logging.log4j.LogManager

private val logger = LogManager.getLogger(CommandManager::class.java.name)

data class CommandData(
    val command: Command,
    val consoleCommandClass: Class<out ConsoleCommand>
)

val commands = listOf(
    CommandData(
        command = CardCommand,
        consoleCommandClass = CardConsoleCommand::class.java
    ),
    CommandData(
        command = PotionUseCommand,
        consoleCommandClass = PotionUseConsoleCommand::class.java
    ),
    CommandData(
        command = PotionDestroyCommand,
        consoleCommandClass = PotionDestroyConsoleCommand::class.java
    ),
    CommandData(
        command = MapCommand,
        consoleCommandClass = MapConsoleCommand::class.java
    ),
    CommandData(
        command = DialogCommand,
        consoleCommandClass = DialogConsoleCommand::class.java
    ),
    CommandData(
        command = EndTurnCommand,
        consoleCommandClass = EndTurnConsoleCommand::class.java
    )
)
private val commandsMap = commands.map { it.command.prefix to it }.toMap()

fun initializeConsoleCommands() {
    ConsoleCommand.addCommand(":test", TestConsoleCommand::class.java)

    commands.forEach { data ->
        ConsoleCommand.addCommand("${data.command.prefix}", data.consoleCommandClass)
    }
}

object CommandManager {

    fun canPerform(stringCommand: String): Boolean {
        val (command, parameters) = extract(stringCommand) ?: return false
        return if (GameState.canPerform(command.prefix)) {
            command.canPerform(parameters)
        } else {
            logger.info("Cannot perform {} in state {}", command.prefix, GameState.state)
            false
        }
    }

    fun perform(stringCommand: String): Boolean {
        val (command, parameters) = extract(stringCommand) ?: return false
        return if (GameState.canPerform(command.prefix)) {
            logger.info("Cannot perform {} in state {}", command.prefix, GameState.state)
            command.perform(parameters)
        } else {
            false
        }
    }
}

private val whitespaceRegex = "\\s+".toRegex()
private val validCommandRegex = "^\\w+( \\d+)*$".toRegex()

fun extract(command: String): Pair<Command, List<Int>>? {
    if (command.isBlank()) {
        return null
    }
    val cleanCommand = command.trim().toLowerCase()
    if (!cleanCommand.matches(validCommandRegex)) {
        return null
    }

    val parts = cleanCommand.split(whitespaceRegex)
    val prefix = parts[0]
    val parameters = parts.subList(1, parts.size).map(String::toInt)

    val data = commandsMap[prefix] ?: return null
    return Pair(data.command, parameters)
}
