package ChatTheSpire.console

import ChatTheSpire.command.BossRewardSelectCommand
import basemod.DevConsole
import basemod.devcommands.ConsoleCommand
import java.util.ArrayList

class BossRewardSelectConsoleCommand : ConsoleCommand() {

    init {
        minExtraTokens = 1
        maxExtraTokens = 1
    }

    override fun execute(tokens: Array<String>, depth: Int) {
        val parameters = tokens.drop(1).map(String::toInt)
        if (!BossRewardSelectCommand.perform(parameters)) {
            DevConsole.log("Failed to select boss reward")
        }
    }

    override fun extraOptions(tokens: Array<String>, depth: Int): ArrayList<String> {
        if (tokens.size == 2) {
            return smallNumbers()
        }
        if (tokens.size > 2) {
            tooManyTokensError()
        }
        return ArrayList()
    }

    override fun errorMsg() {
        DevConsole.couldNotParse()
    }
}
