package ChatTheSpire.console

import ChatTheSpire.command.CardRewardSkipCommand
import basemod.DevConsole
import basemod.devcommands.ConsoleCommand

class CardRewardSkipConsoleCommand : ConsoleCommand() {

    init {
        minExtraTokens = 0
        maxExtraTokens = 0
    }

    override fun execute(tokens: Array<String>, depth: Int) {
        if (!CardRewardSkipCommand.perform(listOf())) {
            DevConsole.log("Failed to skip card reward")
        }
    }

    override fun errorMsg() {
        DevConsole.couldNotParse()
    }
}
