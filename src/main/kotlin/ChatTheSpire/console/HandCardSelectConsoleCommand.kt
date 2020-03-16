package ChatTheSpire.console

import ChatTheSpire.command.HandCardSelectCommand
import ChatTheSpire.util.SafeSpire
import ChatTheSpire.util.toSafeArrayList
import basemod.DevConsole
import basemod.devcommands.ConsoleCommand
import java.util.ArrayList

class HandCardSelectConsoleCommand : ConsoleCommand() {

    init {
        minExtraTokens = 1
        maxExtraTokens = 1
    }

    override fun execute(tokens: Array<String>, depth: Int) {
        val parameters = tokens.drop(1).map(String::toInt)
        if (!HandCardSelectCommand.perform(parameters)) {
            DevConsole.log("Failed to select card")
        }
    }

    override fun extraOptions(tokens: Array<String>, depth: Int): ArrayList<String> {
        if (tokens.size == 2) {
            return SafeSpire.hand
                ?.mapIndexed() { i, card -> "${i + 1} :: ${card.name}" }
                .toSafeArrayList()
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
