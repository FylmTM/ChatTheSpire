package ChatTheSpire.console

import ChatTheSpire.command.CardCommand
import ChatTheSpire.command.PotionCommand
import ChatTheSpire.util.Spire
import ChatTheSpire.util.toSafeArrayList
import basemod.DevConsole
import basemod.devcommands.ConsoleCommand
import java.util.ArrayList

class PotionConsoleCommand : ConsoleCommand() {

    init {
        minExtraTokens = 1
        maxExtraTokens = 2
        requiresPlayer = true
    }

    override fun execute(tokens: Array<String>, depth: Int) {
        val parameters = tokens.drop(1).map(String::toInt)
        if (!PotionCommand.perform(parameters)) {
            DevConsole.log("Failed to use potion.")
        }
    }

    override fun extraOptions(tokens: Array<String>, depth: Int): ArrayList<String> {
        if (tokens.size == 2) {
            return Spire.potions
                ?.mapIndexed() { i, potion -> "${i + 1} :: ${potion.name}" }
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
        DevConsole.log("syntax: :potion [potion-position] {monster-position}")
    }
}
