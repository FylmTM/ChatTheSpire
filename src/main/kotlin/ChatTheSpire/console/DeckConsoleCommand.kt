package ChatTheSpire.console

import ChatTheSpire.command.DeckCommand
import basemod.DevConsole
import basemod.devcommands.ConsoleCommand

class DeckConsoleCommand : ConsoleCommand() {

    init {
        minExtraTokens = 0
        maxExtraTokens = 0
    }

    override fun execute(tokens: Array<String>, depth: Int) {
        if (!DeckCommand.perform(listOf())) {
            DevConsole.log("Failed to open deck")
        }
    }

    override fun errorMsg() {
        DevConsole.couldNotParse()
    }
}
