package ChatTheSpire.util

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.potions.AbstractPotion
import com.megacrit.cardcrawl.rooms.AbstractRoom

/**
 * This object provides safe access to Slay the Spire internals.
 */
object Spire {

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
}
