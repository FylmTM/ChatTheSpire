package ChatTheSpire

import ChatTheSpire.command.BossRewardSelectCommand
import ChatTheSpire.command.CancelCommand
import ChatTheSpire.command.CardCommand
import ChatTheSpire.command.CardRewardSelectCommand
import ChatTheSpire.command.CardRewardSingingBowlCommand
import ChatTheSpire.command.CombatRewardSelectCommand
import ChatTheSpire.command.Command
import ChatTheSpire.command.DeckCommand
import ChatTheSpire.command.DialogCommand
import ChatTheSpire.command.DiscardPileCommand
import ChatTheSpire.command.DrawPileCommand
import ChatTheSpire.command.EndTurnCommand
import ChatTheSpire.command.ExhaustPileCommand
import ChatTheSpire.command.GridSelectCardCommand
import ChatTheSpire.command.HandCardSelectCommand
import ChatTheSpire.command.MapBossCommand
import ChatTheSpire.command.MapCommand
import ChatTheSpire.command.MerchantOpenShopCommand
import ChatTheSpire.command.PotionDestroyCommand
import ChatTheSpire.command.PotionUseCommand
import ChatTheSpire.command.ProceedCommand
import ChatTheSpire.command.RestRoomOptionSelectCommand
import ChatTheSpire.command.ShopPurchaseCommand
import ChatTheSpire.command.ShopPurchaseRemoveCommand
import ChatTheSpire.command.TreasureChestOpenCommand
import ChatTheSpire.console.BossRewardSelectConsoleCommand
import ChatTheSpire.console.CancelConsoleCommand
import ChatTheSpire.console.CardConsoleCommand
import ChatTheSpire.console.CardRewardSelectConsoleCommand
import ChatTheSpire.console.CardRewardSingingBowlConsoleCommand
import ChatTheSpire.console.CombatRewardSelectConsoleCommand
import ChatTheSpire.console.DeckConsoleCommand
import ChatTheSpire.console.DialogConsoleCommand
import ChatTheSpire.console.DiscardPileConsoleCommand
import ChatTheSpire.console.DrawPileConsoleCommand
import ChatTheSpire.console.EndTurnConsoleCommand
import ChatTheSpire.console.ExhaustPileConsoleCommand
import ChatTheSpire.console.GridSelectCardConsoleCommand
import ChatTheSpire.console.HandCardSelectConsoleCommand
import ChatTheSpire.console.MapBossConsoleCommand
import ChatTheSpire.console.MapConsoleCommand
import ChatTheSpire.console.MerchantOpenShopConsoleCommand
import ChatTheSpire.console.PotionDestroyConsoleCommand
import ChatTheSpire.console.PotionUseConsoleCommand
import ChatTheSpire.console.ProceedConsoleCommand
import ChatTheSpire.console.RestRoomOptionSelectConsoleCommand
import ChatTheSpire.console.ShopPurchaseConsoleCommand
import ChatTheSpire.console.ShopPurchaseRemoveConsoleCommand
import ChatTheSpire.console.TreasureChestOpenConsoleCommand
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
    ),
    CommandData(
        command = RestRoomOptionSelectCommand,
        consoleCommandClass = RestRoomOptionSelectConsoleCommand::class.java
    ),
    CommandData(
        command = CancelCommand,
        consoleCommandClass = CancelConsoleCommand::class.java
    ),
    CommandData(
        command = GridSelectCardCommand,
        consoleCommandClass = GridSelectCardConsoleCommand::class.java
    ),
    CommandData(
        command = HandCardSelectCommand,
        consoleCommandClass = HandCardSelectConsoleCommand::class.java
    ),
    CommandData(
        command = CardRewardSingingBowlCommand,
        consoleCommandClass = CardRewardSingingBowlConsoleCommand::class.java
    ),
    CommandData(
        command = TreasureChestOpenCommand,
        consoleCommandClass = TreasureChestOpenConsoleCommand::class.java
    ),
    CommandData(
        command = MapBossCommand,
        consoleCommandClass = MapBossConsoleCommand::class.java
    ),
    CommandData(
        command = BossRewardSelectCommand,
        consoleCommandClass = BossRewardSelectConsoleCommand::class.java
    ),
    CommandData(
        command = MerchantOpenShopCommand,
        consoleCommandClass = MerchantOpenShopConsoleCommand::class.java
    ),
    CommandData(
        command = ShopPurchaseCommand,
        consoleCommandClass = ShopPurchaseConsoleCommand::class.java
    ),
    CommandData(
        command = ShopPurchaseRemoveCommand,
        consoleCommandClass = ShopPurchaseRemoveConsoleCommand::class.java
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
private val commandWithoutPrefixRegex = "^\\d+(\\s+\\d+)*$".toRegex(RegexOption.IGNORE_CASE)
private val commandRegex = "^(${allPrefixes.joinToString("|")})(\\s+\\d+)*$".toRegex(RegexOption.IGNORE_CASE)

fun extract(command: String): Pair<Command, List<Int>>? {
    val gameState = GameState.state
    if (gameState.defaultCommand != null && command.matches(commandWithoutPrefixRegex)) {
        return Pair(gameState.defaultCommand, command.split(whitespaceRegex).map(String::toInt))
    }

    if (!command.matches(commandRegex)) {
        return null
    }

    val parts = command.split(whitespaceRegex)
    val prefix = parts[0]
    val parameters = parts.subList(1, parts.size).map(String::toInt)

    val data = commandsMap[prefix] ?: return null
    return Pair(data.command, parameters)
}

fun possiblyCommand(command: String): Boolean {
    return command.matches(commandWithoutPrefixRegex) || command.matches(commandRegex)
}

fun normalizeCommand(command: String): String {
    return command
        .trim()
        .toLowerCase()
        .split(whitespaceRegex)
        .joinToString(" ")
}
