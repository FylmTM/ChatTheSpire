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

data class CommandData(
    val prefix: String,
    val command: Command,
    val consoleCommandClass: Class<out ConsoleCommand>
)

val commands = listOf(
    CommandData(
        prefix = "card",
        command = CardCommand,
        consoleCommandClass = CardConsoleCommand::class.java
    ),
    CommandData(
        prefix = "usepotion",
        command = PotionUseCommand,
        consoleCommandClass = PotionUseConsoleCommand::class.java
    ),
    CommandData(
        prefix = "destroypotion",
        command = PotionDestroyCommand,
        consoleCommandClass = PotionDestroyConsoleCommand::class.java
    ),
    CommandData(
        prefix = "map",
        command = MapCommand,
        consoleCommandClass = MapConsoleCommand::class.java
    ),
    CommandData(
        prefix = "dialog",
        command = DialogCommand,
        consoleCommandClass = DialogConsoleCommand::class.java
    ),
    CommandData(
        prefix = "endturn",
        command = EndTurnCommand,
        consoleCommandClass = EndTurnConsoleCommand::class.java
    )
)
private val commandsMap = commands.map { it.prefix to it }.toMap()

fun initializeConsoleCommands() {
    ConsoleCommand.addCommand(":test", TestConsoleCommand::class.java)

    commands.forEach { data ->
        ConsoleCommand.addCommand("${data.prefix}", data.consoleCommandClass)
    }
}

object CommandManager {

    fun canPerform(stringCommand: String): Boolean {
        val (command, parameters) = extract(stringCommand) ?: return false
        return command.canPerform(parameters)
    }

    fun perform(stringCommand: String): Boolean {
        val (command, parameters) = extract(stringCommand) ?: return false
        return command.perform(parameters)
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
