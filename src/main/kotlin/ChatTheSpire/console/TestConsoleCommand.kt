package ChatTheSpire.console

import ChatTheSpire.control.Control
import ChatTheSpire.control.Control.longSleep
import ChatTheSpire.control.Control.normalSleep
import ChatTheSpire.control.Control.quickSleep
import ChatTheSpire.control.Control.shortSleep
import ChatTheSpire.control.Control.veryLongSleep
import ChatTheSpire.control.Job
import ChatTheSpire.util.Spire
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
            Spire.monsters?.get(0)?.hb?.let(Control::click)
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
