package ChatTheSpire.console

import ChatTheSpire.command.MapBossCommand
import basemod.DevConsole
import basemod.devcommands.ConsoleCommand

class MapBossConsoleCommand : ConsoleCommand() {

    init {
        minExtraTokens = 0
        maxExtraTokens = 0
    }

    override fun execute(tokens: Array<String>, depth: Int) {
        if (!MapBossCommand.perform(listOf())) {
            DevConsole.log("Failed to end turn")
        }
    }

    override fun errorMsg() {
        DevConsole.couldNotParse()
    }
}
