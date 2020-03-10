package ChatTheSpire

import ChatTheSpire.console.CardConsoleCommand
import ChatTheSpire.console.PotionConsoleCommand
import ChatTheSpire.console.TestConsoleCommand
import basemod.devcommands.ConsoleCommand

fun initializeCommands() {
    ConsoleCommand.addCommand(":test", TestConsoleCommand::class.java)
    ConsoleCommand.addCommand(":card", CardConsoleCommand::class.java)
    ConsoleCommand.addCommand(":potion", PotionConsoleCommand::class.java)
}
