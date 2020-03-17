package ChatTheSpire.console

import ChatTheSpire.command.TreasureChestOpenCommand
import basemod.DevConsole
import basemod.devcommands.ConsoleCommand

class TreasureChestOpenConsoleCommand : ConsoleCommand() {

    init {
        minExtraTokens = 0
        maxExtraTokens = 0
    }

    override fun execute(tokens: Array<String>, depth: Int) {
        if (!TreasureChestOpenCommand.perform(listOf())) {
            DevConsole.log("Failed to open treasure chest")
        }
    }

    override fun errorMsg() {
        DevConsole.couldNotParse()
    }
}
