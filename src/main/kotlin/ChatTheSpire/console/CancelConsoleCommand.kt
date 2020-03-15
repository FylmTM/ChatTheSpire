package ChatTheSpire.console

import ChatTheSpire.command.CancelCommand
import basemod.DevConsole
import basemod.devcommands.ConsoleCommand

class CancelConsoleCommand : ConsoleCommand() {

    init {
        minExtraTokens = 0
        maxExtraTokens = 0
    }

    override fun execute(tokens: Array<String>, depth: Int) {
        if (!CancelCommand.perform(listOf())) {
            DevConsole.log("Failed to cancel")
        }
    }

    override fun errorMsg() {
        DevConsole.couldNotParse()
    }
}
