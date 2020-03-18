package ChatTheSpire.console

import ChatTheSpire.command.MerchantOpenShopCommand
import basemod.DevConsole
import basemod.devcommands.ConsoleCommand

class MerchantOpenShopConsoleCommand : ConsoleCommand() {

    init {
        minExtraTokens = 0
        maxExtraTokens = 0
    }

    override fun execute(tokens: Array<String>, depth: Int) {
        if (!MerchantOpenShopCommand.perform(listOf())) {
            DevConsole.log("Failed to open merchant shop")
        }
    }

    override fun errorMsg() {
        DevConsole.couldNotParse()
    }
}
