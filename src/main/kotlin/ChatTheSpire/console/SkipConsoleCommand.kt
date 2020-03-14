package ChatTheSpire.console

import ChatTheSpire.command.SkipCommand
import basemod.DevConsole
import basemod.devcommands.ConsoleCommand

class SkipConsoleCommand : ConsoleCommand() {

    init {
        minExtraTokens = 0
        maxExtraTokens = 0
    }

    override fun execute(tokens: Array<String>, depth: Int) {
        if (!SkipCommand.perform(listOf())) {
            DevConsole.log("Failed to skip")
        }
    }

    override fun errorMsg() {
        DevConsole.couldNotParse()
    }
}
