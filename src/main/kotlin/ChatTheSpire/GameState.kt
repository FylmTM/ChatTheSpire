package ChatTheSpire

import ChatTheSpire.GameState.State.BOSS_REWARD
import ChatTheSpire.GameState.State.CARD_REWARD
import ChatTheSpire.GameState.State.COMBAT
import ChatTheSpire.GameState.State.COMBAT_REWARD
import ChatTheSpire.GameState.State.DIALOG
import ChatTheSpire.GameState.State.GRID
import ChatTheSpire.GameState.State.HAND_SELECT
import ChatTheSpire.GameState.State.MAP
import ChatTheSpire.GameState.State.NOT_IN_DUNGEON
import ChatTheSpire.GameState.State.REST
import ChatTheSpire.GameState.State.TREASURE
import ChatTheSpire.GameState.State.UNKNOWN
import ChatTheSpire.command.BossRewardSelectCommand
import ChatTheSpire.command.CancelCommand
import ChatTheSpire.command.CardCommand
import ChatTheSpire.command.CardRewardSelectCommand
import ChatTheSpire.command.CardRewardSingingBowlCommand
import ChatTheSpire.command.CombatRewardSelectCommand
import ChatTheSpire.command.Command
import ChatTheSpire.command.DeckCommand
import ChatTheSpire.command.DialogCommand
import ChatTheSpire.command.DiscardPileCommand
import ChatTheSpire.command.DrawPileCommand
import ChatTheSpire.command.EndTurnCommand
import ChatTheSpire.command.ExhaustPileCommand
import ChatTheSpire.command.GridSelectCardCommand
import ChatTheSpire.command.HandCardSelectCommand
import ChatTheSpire.command.MapBossCommand
import ChatTheSpire.command.MapCommand
import ChatTheSpire.command.PotionDestroyCommand
import ChatTheSpire.command.PotionUseCommand
import ChatTheSpire.command.ProceedCommand
import ChatTheSpire.command.RestRoomOptionSelectCommand
import ChatTheSpire.command.TreasureChestOpenCommand
import ChatTheSpire.util.SafeSpire
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.events.RoomEventDialog
import com.megacrit.cardcrawl.rooms.AbstractRoom
import com.megacrit.cardcrawl.rooms.RestRoom
import com.megacrit.cardcrawl.rooms.TreasureRoom
import com.megacrit.cardcrawl.rooms.TreasureRoomBoss

object GameState {

    var currentScreen: AbstractDungeon.CurrentScreen? = null

    enum class State(
        val title: String,
        val defaultCommand: Command?,
        val commands: List<Command>,
        val votingSecondsScale: Float
    ) {
        UNKNOWN(
            title = "Unknown",
            defaultCommand = null,
            commands = emptyList(),
            votingSecondsScale = 1.0F
        ),
        NOT_IN_DUNGEON(
            title = "Not In Dungeon",
            defaultCommand = null,
            commands = emptyList(),
            votingSecondsScale = 1.0F
        ),
        DIALOG(
            title = "Dialog",
            defaultCommand = DialogCommand,
            commands = listOf(
                DialogCommand,
                PotionDestroyCommand
            ),
            votingSecondsScale = 1.5F
        ),
        REST(
            title = "Rest",
            defaultCommand = RestRoomOptionSelectCommand,
            commands = listOf(
                RestRoomOptionSelectCommand,
                ProceedCommand
            ),
            votingSecondsScale = 1.0F
        ),
        COMBAT(
            title = "Combat",
            defaultCommand = CardCommand,
            commands = listOf(
                CardCommand,
                PotionUseCommand,
                PotionDestroyCommand,
                EndTurnCommand,
                DeckCommand,
                DrawPileCommand,
                DiscardPileCommand,
                ExhaustPileCommand
            ),
            votingSecondsScale = 1.0F
        ),
        TREASURE(
            title = "Treasure",
            defaultCommand = null,
            commands = listOf(
                TreasureChestOpenCommand,
                ProceedCommand
            ),
            votingSecondsScale = 1.0F
        ),
        COMBAT_REWARD(
            title = "Combat Reward",
            defaultCommand = CombatRewardSelectCommand,
            commands = listOf(
                CombatRewardSelectCommand,
                ProceedCommand,
                PotionDestroyCommand,
                DeckCommand
            ),
            votingSecondsScale = 1.0F
        ),
        CARD_REWARD(
            title = "Card Reward",
            defaultCommand = CardRewardSelectCommand,
            commands = listOf(
                CardRewardSelectCommand,
                ProceedCommand,
                CardRewardSingingBowlCommand,
                DeckCommand
            ),
            votingSecondsScale = 2.0F
        ),
        BOSS_REWARD(
            title = "Boss Reward",
            defaultCommand = BossRewardSelectCommand,
            commands = listOf(
                BossRewardSelectCommand,
                ProceedCommand,
                CancelCommand
            ),
            votingSecondsScale = 2.5F
        ),
        MAP(
            title = "Map",
            defaultCommand = MapCommand,
            commands = listOf(
                MapCommand,
                MapBossCommand,
                CancelCommand
            ),
            votingSecondsScale = 1.5F
        ),
        GRID(
            title = "Grid",
            defaultCommand = GridSelectCardCommand,
            commands = listOf(
                GridSelectCardCommand,
                ProceedCommand,
                CancelCommand
            ),
            votingSecondsScale = 2.0F
        ),
        HAND_SELECT(
            title = "Hand select",
            defaultCommand = HandCardSelectCommand,
            commands = listOf(
                HandCardSelectCommand,
                ProceedCommand
            ),
            votingSecondsScale = 1.5F
        );

        val prefixes = commands.map(Command::prefix).toHashSet()
        val syntax = commands.map(Command::syntax).joinToString("\n")
        val debug = "$title\n\n$syntax".trim()
    }

    val state: State
        get() {
            if (!AbstractDungeon.isPlayerInDungeon()) {
                return NOT_IN_DUNGEON
            }

            if (AbstractDungeon.isScreenUp) {
                return when (currentScreen) {
                    AbstractDungeon.CurrentScreen.MAP -> MAP
                    AbstractDungeon.CurrentScreen.COMBAT_REWARD -> COMBAT_REWARD
                    AbstractDungeon.CurrentScreen.CARD_REWARD -> CARD_REWARD
                    AbstractDungeon.CurrentScreen.GRID -> GRID
                    AbstractDungeon.CurrentScreen.HAND_SELECT -> HAND_SELECT
                    AbstractDungeon.CurrentScreen.BOSS_REWARD -> BOSS_REWARD
                    else -> UNKNOWN
                }
            }

            if (SafeSpire.room is RestRoom) {
                return REST
            }

            if (SafeSpire.room is TreasureRoom) {
                return TREASURE
            }

            if (SafeSpire.room is TreasureRoomBoss) {
                return TREASURE
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
