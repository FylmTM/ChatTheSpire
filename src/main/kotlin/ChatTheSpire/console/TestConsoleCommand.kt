package ChatTheSpire.console

import ChatTheSpire.util.Automation
import ChatTheSpire.util.Automation.longSleep
import ChatTheSpire.util.Automation.normalSleep
import ChatTheSpire.util.Automation.quickSleep
import ChatTheSpire.util.Automation.shortSleep
import ChatTheSpire.util.Automation.veryLongSleep
import ChatTheSpire.util.Job
import ChatTheSpire.util.SafeSpire
import basemod.DevConsole
import basemod.devcommands.ConsoleCommand
import com.megacrit.cardcrawl.helpers.CardLibrary

class TestConsoleCommand : ConsoleCommand() {

    init {
        minExtraTokens = 0
        maxExtraTokens = 0
    }

    override fun execute(tokens: Array<String>, depth: Int) {
        Job.execute {
            // Prepare
            run("hand remove all")
            longSleep()
            run("deck remove all")
            longSleep()
            run("maxhp lose 100")
            longSleep()
            run("maxhp add 99")
            longSleep()

            // Prepare fight
            run("fight Transient")
            veryLongSleep()
            run("energy inf")
            normalSleep()
            run("power Invincible 1")
            quickSleep()
            SafeSpire.monsters?.get(0)?.hb?.let(Automation::click)
            quickSleep()

            // Play cards
            val cards = CardLibrary.getCardList(CardLibrary.LibraryType.RED)
            for (card in cards) {
                val id = card.cardID.replace(" ", "_")
                execute(arrayOf("hand", "add", id))
                shortSleep()
                execute(arrayOf(":card", "1", "1"))
                longSleep()
                longSleep()
                longSleep()

                run("hand remove all")
                longSleep()
            }
        }
    }

    override fun errorMsg() {
        DevConsole.couldNotParse()
        DevConsole.log("syntax: :test")
    }

    fun run(command: String) {
        execute(command.split(" ").toTypedArray())
    }
}
