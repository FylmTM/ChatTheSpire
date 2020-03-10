package ChatTheSpire.console

import ChatTheSpire.command.EndTurnCommand
import basemod.DevConsole
import basemod.devcommands.ConsoleCommand

class EndTurnConsoleCommand : ConsoleCommand() {

    init {
        minExtraTokens = 0
        maxExtraTokens = 0
    }

    override fun execute(tokens: Array<String>, depth: Int) {
        if (!EndTurnCommand.perform(listOf())) {
            DevConsole.log("Failed to end turn")
        }
    }

    override fun errorMsg() {
        DevConsole.couldNotParse()
        DevConsole.log("syntax: :endturn")
    }
}
