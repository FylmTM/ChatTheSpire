package ChatTheSpire

import ChatTheSpire.console.CardConsoleCommand
import basemod.devcommands.ConsoleCommand

fun initializeCommands() {
    ConsoleCommand.addCommand("card", CardConsoleCommand::class.java)
}
