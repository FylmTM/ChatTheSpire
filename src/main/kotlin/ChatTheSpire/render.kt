package ChatTheSpire

import ChatTheSpire.util.SafeSpire
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Align
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.events.RoomEventDialog
import com.megacrit.cardcrawl.potions.PotionSlot

fun renderHints(sb: SpriteBatch, font: BitmapFont) {
    // Potions
    if (AbstractDungeon.topPanel?.potionUi?.isHidden != false) {
        SafeSpire.potions?.forEachIndexed { i, potion ->
            if (potion !is PotionSlot) {
                font.draw(sb, "${i + 1}", potion.hb.x, potion.hb.y, potion.hb.width, Align.center, false)
            }
        }
    }

    if (AbstractDungeon.isScreenUp) {
        // Available map nodes
        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.MAP) {
            SafeSpire.nextMapNodes.forEachIndexed { i, node ->
                font.draw(sb, "${i + 1}", node.hb.x, node.hb.y, node.hb.width, Align.center, false)
            }
        }
    } else {
        // Options for room dialog (Neow)
        RoomEventDialog.optionList?.forEachIndexed { i, option ->
            font.draw(sb, "${i + 1}", option.hb.x, option.hb.y + option.hb.height - 10.0F * Settings.scale)
        }

        // Options in event room
        SafeSpire.room?.event?.imageEventText?.optionList?.forEachIndexed { i, option ->
            font.draw(sb, "${i + 1}", option.hb.x, option.hb.y + option.hb.height - 10.0F * Settings.scale)
        }

        // Hand
        if (SafeSpire.hand != null) {
            val size = SafeSpire.hand!!.size
            SafeSpire.hand!!.forEachIndexed { i, card ->
                font.draw(
                    sb,
                    "${i + 1}",
                    card.hb.x + card.hb.width * ((i).toFloat() / size),
                    card.hb.y + card.hb.height + 15.0F + Settings.scale
                )
            }
        }

        SafeSpire.monsters?.forEachIndexed { i, monster ->
            font.draw(
                sb,
                "${i + 1}",
                monster.intentHb.x,
                monster.intentHb.y + monster.intentHb.height + 30.0F * Settings.scale,
                monster.intentHb.width,
                Align.center,
                false
            )
        }
    }
}
