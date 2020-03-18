package ChatTheSpire.console

import ChatTheSpire.command.ShopPurchaseCommand
import basemod.DevConsole
import basemod.devcommands.ConsoleCommand
import java.util.ArrayList

class ShopPurchaseConsoleCommand : ConsoleCommand() {

    init {
        minExtraTokens = 1
        maxExtraTokens = 1
    }

    override fun execute(tokens: Array<String>, depth: Int) {
        val parameters = tokens.drop(1).map(String::toInt)
        if (!ShopPurchaseCommand.perform(parameters)) {
            DevConsole.log("Failed to purchase thing")
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
