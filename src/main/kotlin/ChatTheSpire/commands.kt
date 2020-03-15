package ChatTheSpire

import ChatTheSpire.command.CardCommand
import ChatTheSpire.command.CardRewardSelectCommand
import ChatTheSpire.command.CombatRewardSelectCommand
import ChatTheSpire.command.Command
import ChatTheSpire.command.DeckCommand
import ChatTheSpire.command.DialogCommand
import ChatTheSpire.command.DiscardPileCommand
import ChatTheSpire.command.DrawPileCommand
import ChatTheSpire.command.EndTurnCommand
import ChatTheSpire.command.ExhaustPileCommand
import ChatTheSpire.command.MapCommand
import ChatTheSpire.command.PotionDestroyCommand
import ChatTheSpire.command.PotionUseCommand
import ChatTheSpire.command.ProceedCommand
import ChatTheSpire.command.SkipCommand
import ChatTheSpire.console.CardConsoleCommand
import ChatTheSpire.console.CardRewardSelectConsoleCommand
import ChatTheSpire.console.CombatRewardSelectConsoleCommand
import ChatTheSpire.console.DeckConsoleCommand
import ChatTheSpire.console.DialogConsoleCommand
import ChatTheSpire.console.DiscardPileConsoleCommand
import ChatTheSpire.console.DrawPileConsoleCommand
import ChatTheSpire.console.EndTurnConsoleCommand
import ChatTheSpire.console.ExhaustPileConsoleCommand
import ChatTheSpire.console.MapConsoleCommand
import ChatTheSpire.console.PotionDestroyConsoleCommand
import ChatTheSpire.console.PotionUseConsoleCommand
import ChatTheSpire.console.ProceedConsoleCommand
import ChatTheSpire.console.SkipConsoleCommand
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
    ),
    CommandData(
        command = ProceedCommand,
        consoleCommandClass = ProceedConsoleCommand::class.java
    ),
    CommandData(
        command = SkipCommand,
        consoleCommandClass = SkipConsoleCommand::class.java
    ),
    CommandData(
        command = CombatRewardSelectCommand,
        consoleCommandClass = CombatRewardSelectConsoleCommand::class.java
    ),
    CommandData(
        command = CardRewardSelectCommand,
        consoleCommandClass = CardRewardSelectConsoleCommand::class.java
    ),
    CommandData(
        command = DeckCommand,
        consoleCommandClass = DeckConsoleCommand::class.java
    ),
    CommandData(
        command = DiscardPileCommand,
        consoleCommandClass = DiscardPileConsoleCommand::class.java
    ),
    CommandData(
        command = DrawPileCommand,
        consoleCommandClass = DrawPileConsoleCommand::class.java
    ),
    CommandData(
        command = ExhaustPileCommand,
        consoleCommandClass = ExhaustPileConsoleCommand::class.java
    )
)
private val commandsMap = commands.map { it.command.prefix to it }.toMap().also {
    if (it.size != commands.size) {
        throw RuntimeException("Check command prefixes, there is duplicate")
    }
}
private val allPrefixes = commands.map { it.command.prefix }

fun initializeConsoleCommands() {
    commands.forEach { data ->
        ConsoleCommand.addCommand(data.command.prefix, data.consoleCommandClass)
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
private val commandWithoutPrefixRegex = "^\\d+(\\s+\\d+)*$".toRegex()
private val commandRegex = "^(${allPrefixes.joinToString("|")})(\\s+\\d+)*$".toRegex()

fun extract(command: String): Pair<Command, List<Int>>? {
    val gameState = GameState.state
    if (command.isBlank()) {
        return null
    }
    val cleanCommand = command.trim().toLowerCase()
    if (gameState.defaultCommand != null && cleanCommand.matches(commandWithoutPrefixRegex)) {
        return Pair(gameState.defaultCommand, cleanCommand.split(whitespaceRegex).map(String::toInt))
    }

    if (!cleanCommand.matches(commandRegex)) {
        return null
    }

    val parts = cleanCommand.split(whitespaceRegex)
    val prefix = parts[0]
    val parameters = parts.subList(1, parts.size).map(String::toInt)

    val data = commandsMap[prefix] ?: return null
    return Pair(data.command, parameters)
}

fun possiblyCommand(command: String): Boolean {
    return command.matches(commandWithoutPrefixRegex) || command.matches(commandRegex)
}
