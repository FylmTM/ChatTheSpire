package ChatTheSpire.console

import ChatTheSpire.command.CardCommand
import ChatTheSpire.util.Spire
import ChatTheSpire.util.toSafeArrayList
import basemod.DevConsole
import basemod.devcommands.ConsoleCommand
import java.util.ArrayList

class CardConsoleCommand : ConsoleCommand() {

    init {
        minExtraTokens = 1
        maxExtraTokens = 2
        requiresPlayer = false
    }

    override fun execute(tokens: Array<String>, depth: Int) {
        val parameters = tokens.drop(1).map(String::toInt)
        if (!CardCommand.perform(parameters)) {
            DevConsole.log("Failed to play card.")
        }
    }

    override fun extraOptions(tokens: Array<String>, depth: Int): ArrayList<String> {
        if (tokens.size == 2) {
            return Spire.hand
                ?.mapIndexed() { i, card -> "${i + 1} :: ${card.name}" }
                .toSafeArrayList()
        }
        if (tokens.size == 3) {
            return Spire.monsters
                ?.mapIndexed() { i, monster -> "${i + 1} :: ${monster.name}" }
                .toSafeArrayList()
        }
        if (tokens.size > 3) {
            tooManyTokensError()
        }
        return ArrayList()
    }

    override fun errorMsg() {
        DevConsole.couldNotParse()
        DevConsole.log("syntax: [card-id] {monster-id}")
    }
}
