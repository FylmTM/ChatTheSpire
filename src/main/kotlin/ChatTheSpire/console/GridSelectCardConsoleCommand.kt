package ChatTheSpire.console

import ChatTheSpire.command.GridSelectCardCommand
import basemod.DevConsole
import basemod.devcommands.ConsoleCommand
import java.util.ArrayList

class GridSelectCardConsoleCommand : ConsoleCommand() {

    init {
        minExtraTokens = 1
        maxExtraTokens = 1
    }

    override fun execute(tokens: Array<String>, depth: Int) {
        val parameters = tokens.drop(1).map(String::toInt)
        if (!GridSelectCardCommand.perform(parameters)) {
            DevConsole.log("Failed to select grid card")
        }
    }

    override fun extraOptions(tokens: Array<String>, depth: Int): ArrayList<String> {
        if (tokens.size == 2) {
            return mediumNumbers()
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
