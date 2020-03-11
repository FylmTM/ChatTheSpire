package ChatTheSpire.console

import ChatTheSpire.command.DialogCommand
import ChatTheSpire.util.toSafeArrayList
import basemod.DevConsole
import basemod.devcommands.ConsoleCommand
import com.megacrit.cardcrawl.events.RoomEventDialog
import java.util.ArrayList

class DialogConsoleCommand : ConsoleCommand() {

    init {
        minExtraTokens = 1
        maxExtraTokens = 1
    }

    override fun execute(tokens: Array<String>, depth: Int) {
        val parameters = tokens.drop(1).map(String::toInt)
        if (!DialogCommand.perform(parameters)) {
            DevConsole.log("Failed to choose dialog option")
        }
    }

    override fun extraOptions(tokens: Array<String>, depth: Int): ArrayList<String> {
        if (tokens.size == 2) {
            return RoomEventDialog.optionList
                ?.indices
                ?.map { i -> "${i + 1}" }
                .toSafeArrayList()
        }
        if (tokens.size > 2) {
            tooManyTokensError()
        }
        return ArrayList()
    }

    override fun errorMsg() {
        DevConsole.couldNotParse()
        DevConsole.log("syntax: :dialog [option-position]")
    }
}
