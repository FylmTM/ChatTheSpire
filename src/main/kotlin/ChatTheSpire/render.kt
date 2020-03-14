package ChatTheSpire

import ChatTheSpire.util.SafeSpire
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Align
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.events.RoomEventDialog
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.potions.PotionSlot

fun renderHints(sb: SpriteBatch, font: BitmapFont) {
    // State
    val gameState = GameState.state
    FontHelper.renderFont(
        sb,
        FontHelper.tipBodyFont,
        gameState.debug,
        30.0F * Settings.scale,
        Settings.HEIGHT * 0.80F,
        Color.WHITE
    )

    // Potions
    if (AbstractDungeon.topPanel?.potionUi?.isHidden != false) {
        SafeSpire.potions?.forEachIndexed { i, potion ->
            if (potion !is PotionSlot) {
                font.draw(
                    sb,
                    "${i + 1}",
                    potion.hb.x,
                    potion.hb.y - 5.0F * Settings.scale,
                    potion.hb.width,
                    Align.center,
                    false
                )
            }
        }
    }

    if (AbstractDungeon.isScreenUp) {
        // Available map nodes
        when (GameState.currentScreen) {
            AbstractDungeon.CurrentScreen.MAP -> {
                SafeSpire.nextMapNodes.forEachIndexed { i, node ->
                    font.draw(sb, "${i + 1}", node.hb.x, node.hb.y, node.hb.width, Align.center, false)
                }
            }
            AbstractDungeon.CurrentScreen.COMBAT_REWARD -> {
                AbstractDungeon.combatRewardScreen?.rewards?.forEachIndexed { i, reward ->
                    font.draw(
                        sb,
                        "${i + 1}",
                        reward.hb.x - 15.0F * Settings.scale,
                        reward.hb.y + reward.hb.height - 35.0F * Settings.scale
                    )
                }
            }
            AbstractDungeon.CurrentScreen.CARD_REWARD -> {
                AbstractDungeon.cardRewardScreen?.rewardGroup?.forEachIndexed { i, card ->
                    font.draw(
                        sb,
                        "${i + 1}",
                        card.hb.x,
                        card.hb.y - 10.0F * Settings.scale,
                        card.hb.width,
                        Align.center,
                        false
                    )
                }
            }
            else -> {
            }
        }
    } else {
        // Options for room dialog (Neow)
        RoomEventDialog.optionList?.forEachIndexed { i, option ->
            font.draw(
                sb,
                "${i + 1}",
                option.hb.x - 15.0F * Settings.scale,
                option.hb.y + option.hb.height - 25.0F * Settings.scale
            )
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
                    card.hb.y + card.hb.height + 30.0F + Settings.scale
                )
            }
        }

        // Monsters
        SafeSpire.monsters?.forEachIndexed { i, monster ->
            if (!monster.isDead) {
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
}
