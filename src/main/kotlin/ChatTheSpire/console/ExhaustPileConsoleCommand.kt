package ChatTheSpire.console

import ChatTheSpire.command.ExhaustPileCommand
import basemod.DevConsole
import basemod.devcommands.ConsoleCommand

class ExhaustPileConsoleCommand : ConsoleCommand() {

    init {
        minExtraTokens = 0
        maxExtraTokens = 0
    }

    override fun execute(tokens: Array<String>, depth: Int) {
        if (!ExhaustPileCommand.perform(listOf())) {
            DevConsole.log("Failed to open exhaust pile")
        }
    }

    override fun errorMsg() {
        DevConsole.couldNotParse()
    }
}
