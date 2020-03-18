package ChatTheSpire.console

import ChatTheSpire.command.ShopPurchaseRemoveCommand
import basemod.DevConsole
import basemod.devcommands.ConsoleCommand

class ShopPurchaseRemoveConsoleCommand : ConsoleCommand() {

    init {
        minExtraTokens = 0
        maxExtraTokens = 0
    }

    override fun execute(tokens: Array<String>, depth: Int) {
        if (!ShopPurchaseRemoveCommand.perform(listOf())) {
            DevConsole.log("Failed to purchase remove")
        }
    }

    override fun errorMsg() {
        DevConsole.couldNotParse()
    }
}
