package ChatTheSpire.console

import ChatTheSpire.command.DiscardPileCommand
import basemod.DevConsole
import basemod.devcommands.ConsoleCommand

class DiscardPileConsoleCommand : ConsoleCommand() {

    init {
        minExtraTokens = 0
        maxExtraTokens = 0
    }

    override fun execute(tokens: Array<String>, depth: Int) {
        if (!DiscardPileCommand.perform(listOf())) {
            DevConsole.log("Failed to open discard pile")
        }
    }

    override fun errorMsg() {
        DevConsole.couldNotParse()
    }
}
