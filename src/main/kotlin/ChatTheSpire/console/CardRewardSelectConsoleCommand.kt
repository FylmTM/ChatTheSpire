package ChatTheSpire.console

import ChatTheSpire.command.CardRewardSelectCommand
import ChatTheSpire.util.toSafeArrayList
import basemod.DevConsole
import basemod.devcommands.ConsoleCommand
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import java.util.ArrayList

class CardRewardSelectConsoleCommand : ConsoleCommand() {

    init {
        minExtraTokens = 1
        maxExtraTokens = 1
    }

    override fun execute(tokens: Array<String>, depth: Int) {
        val parameters = tokens.drop(1).map(String::toInt)
        if (!CardRewardSelectCommand.perform(parameters)) {
            DevConsole.log("Failed to select card reward")
        }
    }

    override fun extraOptions(tokens: Array<String>, depth: Int): ArrayList<String> {
        if (tokens.size == 2) {
            return AbstractDungeon.cardRewardScreen
                ?.rewardGroup
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
    }
}
