package ChatTheSpire

import ChatTheSpire.GameState.State.COMBAT
import ChatTheSpire.GameState.State.COMBAT_REWARD
import ChatTheSpire.GameState.State.DIALOG
import ChatTheSpire.GameState.State.MAP
import ChatTheSpire.GameState.State.NOT_IN_DUNGEON
import ChatTheSpire.GameState.State.UNKNOWN
import ChatTheSpire.command.CardCommand
import ChatTheSpire.command.CombatRewardSelect
import ChatTheSpire.command.Command
import ChatTheSpire.command.DialogCommand
import ChatTheSpire.command.EndTurnCommand
import ChatTheSpire.command.MapCommand
import ChatTheSpire.command.PotionDestroyCommand
import ChatTheSpire.command.PotionUseCommand
import ChatTheSpire.command.ProceedCommand
import ChatTheSpire.util.SafeSpire
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.events.RoomEventDialog
import com.megacrit.cardcrawl.rooms.AbstractRoom

object GameState {

    enum class State(
        val title: String,
        val defaultCommand: Command?,
        val commands: List<Command>
    ) {
        UNKNOWN(
            title = "Unknown",
            defaultCommand = null,
            commands = emptyList()
        ),
        NOT_IN_DUNGEON(
            title = "Not in dungeon",
            defaultCommand = null,
            commands = emptyList()
        ),
        DIALOG(
            title = "Dialog",
            defaultCommand = DialogCommand,
            commands = listOf(
                DialogCommand,
                PotionDestroyCommand
            )
        ),
        COMBAT(
            title = "Combat",
            defaultCommand = CardCommand,
            commands = listOf(
                CardCommand,
                PotionUseCommand,
                PotionDestroyCommand,
                EndTurnCommand
            )
        ),
        COMBAT_REWARD(
            title = "Combat Reward",
            defaultCommand = CombatRewardSelect,
            commands = listOf(
                CombatRewardSelect,
                ProceedCommand,
                PotionDestroyCommand
            )
        ),
        MAP(
            title = "Map",
            defaultCommand = MapCommand,
            commands = listOf(
                MapCommand,
                PotionDestroyCommand
            )
        );

        val prefixes = commands.map(Command::prefix).toHashSet()
        val syntax = commands.map(Command::syntax).joinToString("\n")
        val debug = "$syntax".trim()
    }

    val state: State
        get() {
            if (!AbstractDungeon.isPlayerInDungeon()) {
                return NOT_IN_DUNGEON
            }

            if (AbstractDungeon.isScreenUp) {
                return when (AbstractDungeon.screen) {
                    AbstractDungeon.CurrentScreen.MAP -> MAP
                    AbstractDungeon.CurrentScreen.COMBAT_REWARD -> COMBAT_REWARD
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
