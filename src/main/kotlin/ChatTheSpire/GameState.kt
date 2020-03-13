package ChatTheSpire

import ChatTheSpire.GameState.State.COMBAT
import ChatTheSpire.GameState.State.DIALOG
import ChatTheSpire.GameState.State.MAP
import ChatTheSpire.GameState.State.NOT_IN_DUNGEON
import ChatTheSpire.GameState.State.UNKNOWN
import ChatTheSpire.command.CardCommand
import ChatTheSpire.command.Command
import ChatTheSpire.command.DialogCommand
import ChatTheSpire.command.EndTurnCommand
import ChatTheSpire.command.MapCommand
import ChatTheSpire.command.PotionDestroyCommand
import ChatTheSpire.command.PotionUseCommand
import ChatTheSpire.util.SafeSpire
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.events.RoomEventDialog
import com.megacrit.cardcrawl.rooms.AbstractRoom

object GameState {

    enum class State(val commands: List<Command>) {
        UNKNOWN(emptyList()),
        NOT_IN_DUNGEON(emptyList()),
        DIALOG(
            listOf(
                DialogCommand,
                PotionDestroyCommand
            )
        ),
        COMBAT(
            listOf(
                EndTurnCommand,
                CardCommand,
                PotionDestroyCommand,
                PotionUseCommand
            )
        ),
        MAP(
            listOf(
                MapCommand,
                PotionDestroyCommand
            )
        );

        val prefixes = commands.map(Command::prefix).toHashSet()
        val syntax = commands.map(Command::syntax).joinToString("\n")
        val debug = "State: $name\n\n${syntax}"
    }

    val state: State
        get() {
            if (!AbstractDungeon.isPlayerInDungeon()) {
                return NOT_IN_DUNGEON
            }

            if (AbstractDungeon.isScreenUp) {
                return when (AbstractDungeon.screen) {
                    AbstractDungeon.CurrentScreen.MAP -> MAP
                    else -> UNKNOWN
                }
            }

            if (RoomEventDialog.optionList?.isNotEmpty() == true) {
                return DIALOG
            }

            if (SafeSpire.room?.event?.imageEventText?.optionList?.isNotEmpty() == true) {
                return DIALOG
            }

            if (SafeSpire.room?.phase == AbstractRoom.RoomPhase.COMBAT) {
                return COMBAT
            }

            return UNKNOWN
        }

    fun canPerform(commandPrefix: String): Boolean =
        state.prefixes.contains(commandPrefix)
}
