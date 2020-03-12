package ChatTheSpire.console

import ChatTheSpire.command.PotionDestroyCommand
import ChatTheSpire.util.SafeSpire
import ChatTheSpire.util.toSafeArrayList
import basemod.DevConsole
import basemod.devcommands.ConsoleCommand
import java.util.ArrayList

class PotionDestroyConsoleCommand : ConsoleCommand() {

    init {
        minExtraTokens = 1
        maxExtraTokens = 1
    }

    override fun execute(tokens: Array<String>, depth: Int) {
        val parameters = tokens.drop(1).map(String::toInt)
        if (!PotionDestroyCommand.perform(parameters)) {
            DevConsole.log("Failed to destroy potion")
        }
    }

    override fun extraOptions(tokens: Array<String>, depth: Int): ArrayList<String> {
        if (tokens.size == 2) {
            return SafeSpire.potions
                ?.mapIndexed() { i, potion -> "${i + 1} :: ${potion.name}" }
                .toSafeArrayList()
        }
        if (tokens.size > 2) {
            tooManyTokensError()
        }
        return ArrayList()
    }

    override fun errorMsg() {
        DevConsole.couldNotParse()
        DevConsole.log("syntax: :potiondestroy [potion-position] {monster-position}")
    }
}
