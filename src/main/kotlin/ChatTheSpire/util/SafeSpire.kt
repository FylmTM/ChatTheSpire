package ChatTheSpire.util

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.map.MapRoomNode
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.potions.AbstractPotion
import com.megacrit.cardcrawl.rooms.AbstractRoom
import com.megacrit.cardcrawl.rooms.AbstractRoom.RoomPhase

/**
 * This object provides safe access to Slay the Spire internals.
 */
object SafeSpire {

    val room: AbstractRoom?
        get() = AbstractDungeon.currMapNode?.getRoom()

    val monsters: ArrayList<AbstractMonster>?
        get() = room?.monsters?.monsters

    val player: AbstractPlayer?
        get() = AbstractDungeon.player

    val potions: ArrayList<AbstractPotion>?
        get() = AbstractDungeon.player?.potions

    val hand: ArrayList<AbstractCard>?
        get() = player?.hand?.group

    val nextMapNodes: List<MapRoomNode>
        get() {
            if (AbstractDungeon.getCurrRoom().phase != RoomPhase.COMPLETE) {
                return listOf()
            }

            return SpireInternals.visibleMapNodes
                .filter {
                    if (!AbstractDungeon.firstRoomChosen && it.y == 0) {
                        true
                    } else {
                        val normalConnection = AbstractDungeon.getCurrMapNode().isConnectedTo(it)
                        val wingedConnection = AbstractDungeon.getCurrMapNode().wingedIsConnectedTo(it)

                        normalConnection || wingedConnection
                    }
                }
        }
}
