package ChatTheSpire.console

import ChatTheSpire.command.DrawPileCommand
import basemod.DevConsole
import basemod.devcommands.ConsoleCommand

class DrawPileConsoleCommand : ConsoleCommand() {

    init {
        minExtraTokens = 0
        maxExtraTokens = 0
    }

    override fun execute(tokens: Array<String>, depth: Int) {
        if (!DrawPileCommand.perform(listOf())) {
            DevConsole.log("Failed to open draw pile")
        }
    }

    override fun errorMsg() {
        DevConsole.couldNotParse()
    }
}
