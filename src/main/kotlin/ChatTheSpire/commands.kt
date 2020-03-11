package ChatTheSpire

import ChatTheSpire.console.CardConsoleCommand
import ChatTheSpire.console.DialogConsoleCommand
import ChatTheSpire.console.EndTurnConsoleCommand
import ChatTheSpire.console.MapConsoleCommand
import ChatTheSpire.console.PotionDestroyConsoleCommand
import ChatTheSpire.console.PotionUseConsoleCommand
import ChatTheSpire.console.TestConsoleCommand
import basemod.devcommands.ConsoleCommand

fun initializeCommands() {
    // Testing
    ConsoleCommand.addCommand(":test", TestConsoleCommand::class.java)

    // Combat
    ConsoleCommand.addCommand(":card", CardConsoleCommand::class.java)
    ConsoleCommand.addCommand("c", CardConsoleCommand::class.java)
    ConsoleCommand.addCommand(":potionuse", PotionUseConsoleCommand::class.java)
    ConsoleCommand.addCommand("pu", PotionUseConsoleCommand::class.java)
    ConsoleCommand.addCommand(":potiondestroy", PotionDestroyConsoleCommand::class.java)
    ConsoleCommand.addCommand("pd", PotionDestroyConsoleCommand::class.java)

    // Interactions
    ConsoleCommand.addCommand(":map", MapConsoleCommand::class.java)
    ConsoleCommand.addCommand("m", MapConsoleCommand::class.java)
    ConsoleCommand.addCommand(":dialog", DialogConsoleCommand::class.java)
    ConsoleCommand.addCommand("d", DialogConsoleCommand::class.java)
    ConsoleCommand.addCommand(":endturn", EndTurnConsoleCommand::class.java)
    ConsoleCommand.addCommand("e", EndTurnConsoleCommand::class.java)
}
