package ChatTheSpire.console

import ChatTheSpire.command.ProceedCommand
import basemod.DevConsole
import basemod.devcommands.ConsoleCommand

class ProceedConsoleCommand : ConsoleCommand() {

    init {
        minExtraTokens = 0
        maxExtraTokens = 0
    }

    override fun execute(tokens: Array<String>, depth: Int) {
        if (!ProceedCommand.perform(listOf())) {
            DevConsole.log("Failed to proceed")
        }
    }

    override fun errorMsg() {
        DevConsole.couldNotParse()
    }
}
