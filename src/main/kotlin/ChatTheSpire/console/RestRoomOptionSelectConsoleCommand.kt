package ChatTheSpire.console

import ChatTheSpire.command.MapCommand
import ChatTheSpire.command.RestRoomOptionSelectCommand
import ChatTheSpire.util.toSafeArrayList
import basemod.DevConsole
import basemod.devcommands.ConsoleCommand
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import java.util.ArrayList

class RestRoomOptionSelectConsoleCommand : ConsoleCommand() {

    init {
        minExtraTokens = 1
        maxExtraTokens = 1
    }

    override fun execute(tokens: Array<String>, depth: Int) {
        val parameters = tokens.drop(1).map(String::toInt)
        if (!RestRoomOptionSelectCommand.perform(parameters)) {
            DevConsole.log("Failed to choose rest room option")
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
