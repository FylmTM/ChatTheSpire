package ChatTheSpire

import ChatTheSpire.console.CardConsoleCommand
import ChatTheSpire.console.EndTurnConsoleCommand
import ChatTheSpire.console.PotionDestroyConsoleCommand
import ChatTheSpire.console.PotionUseConsoleCommand
import ChatTheSpire.console.TestConsoleCommand
import basemod.devcommands.ConsoleCommand

fun initializeCommands() {
    // Testing
    ConsoleCommand.addCommand(":test", TestConsoleCommand::class.java)

    // Combat
    ConsoleCommand.addCommand(":card", CardConsoleCommand::class.java)
    ConsoleCommand.addCommand(":potionuse", PotionUseConsoleCommand::class.java)
    ConsoleCommand.addCommand(":potiondestroy", PotionDestroyConsoleCommand::class.java)

    // Interactions
    ConsoleCommand.addCommand(":endturn", EndTurnConsoleCommand::class.java)
}
