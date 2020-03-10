package ChatTheSpire

import ChatTheSpire.console.CardConsoleCommand
import ChatTheSpire.console.PotionDestroyConsoleCommand
import ChatTheSpire.console.PotionUseConsoleCommand
import ChatTheSpire.console.TestConsoleCommand
import basemod.devcommands.ConsoleCommand

fun initializeCommands() {
    ConsoleCommand.addCommand(":test", TestConsoleCommand::class.java)
    ConsoleCommand.addCommand(":card", CardConsoleCommand::class.java)
    ConsoleCommand.addCommand(":potionuse", PotionUseConsoleCommand::class.java)
    ConsoleCommand.addCommand(":potiondestroy", PotionDestroyConsoleCommand::class.java)
}
