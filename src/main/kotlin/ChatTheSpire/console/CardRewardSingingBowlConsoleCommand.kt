package ChatTheSpire.console

import ChatTheSpire.command.CardRewardSingingBowlCommand
import basemod.DevConsole
import basemod.devcommands.ConsoleCommand

class CardRewardSingingBowlConsoleCommand : ConsoleCommand() {

    init {
        minExtraTokens = 0
        maxExtraTokens = 0
    }

    override fun execute(tokens: Array<String>, depth: Int) {
        if (!CardRewardSingingBowlCommand.perform(listOf())) {
            DevConsole.log("Failed to select singing bowl")
        }
    }

    override fun errorMsg() {
        DevConsole.couldNotParse()
    }
}
