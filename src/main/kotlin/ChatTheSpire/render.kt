package ChatTheSpire

import ChatTheSpire.util.SafeSpire
import ChatTheSpire.util.SpireInternals
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Align
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.events.RoomEventDialog
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.potions.PotionSlot
import com.megacrit.cardcrawl.rooms.RestRoom

fun renderHints(sb: SpriteBatch, font: BitmapFont) {
    if (!AbstractDungeon.isPlayerInDungeon()) {
        return
    }

    // State
    val gameState = GameState.state
    if (gameState == GameState.State.UNKNOWN) {
        // don't render anything, if we have no idea what we want
        return
    }

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
                    potion.hb.y + potion.hb.height - 1.0F * Settings.scale,
                    potion.hb.width,
                    Align.left,
                    false
                )
            }
        }
    }

    // Deck button
    if (!SpireInternals.deckButtonDisabled) {
        font.draw(
            sb,
            "D",
            AbstractDungeon.topPanel.deckHb.x,
            AbstractDungeon.topPanel.deckHb.y - 5.0F * Settings.scale,
            AbstractDungeon.topPanel.deckHb.width,
            Align.center,
            false
        )
    }

    // Overlay proceed
    if (!SpireInternals.proceedIsHidden) {
        SpireInternals.proceedHitbox?.let {
            font.draw(
                sb,
                "N",
                it.x,
                it.y,
                it.width,
                Align.center,
                false
            )
        }
        SpireInternals.proceedHitbox
    }

    // Overlay cancel
    if (!AbstractDungeon.overlayMenu.cancelButton.isHidden) {
        AbstractDungeon.overlayMenu.cancelButton.hb?.let {
            font.draw(
                sb,
                "B",
                it.x,
                it.y,
                it.width,
                Align.center,
                false
            )
        }
        SpireInternals.proceedHitbox
    }

    if (AbstractDungeon.isScreenUp) {
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

                SpireInternals.cardRewardSkipButton?.let {
                    if (!SpireInternals.skipButtonIsHidden(it)) {
                        font.draw(
                            sb,
                            "N",
                            it.hb.x,
                            it.hb.y,
                            it.hb.width,
                            Align.center,
                            false
                        )
                    }
                }
            }
            AbstractDungeon.CurrentScreen.GRID -> {
                if (AbstractDungeon.gridSelectScreen?.confirmScreenUp == false) {
                    AbstractDungeon.gridSelectScreen?.targetGroup?.group?.forEachIndexed { i, card ->
                        font.draw(
                            sb,
                            "${i + 1}",
                            card.hb.x,
                            card.hb.y + 25.0F * Settings.scale,
                            card.hb.width,
                            Align.center,
                            false
                        )
                    }
                }

                if (!SpireInternals.gridSelectConfirmButtonIsHidden) {
                    AbstractDungeon.gridSelectScreen.confirmButton.hb.let {
                        font.draw(
                            sb,
                            "N",
                            it.x,
                            it.y,
                            it.width,
                            Align.center,
                            false
                        )
                    }
                }
            }
            AbstractDungeon.CurrentScreen.HAND_SELECT -> {
                if (!SpireInternals.cardSelectConfirmButtonIsHidden) {
                    AbstractDungeon.handCardSelectScreen.button.hb.let {
                        font.draw(
                            sb,
                            "N",
                            it.x,
                            it.y,
                            it.width,
                            Align.center,
                            false
                        )
                    }
                }
                renderHand(sb, font)
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
            font.draw(
                sb,
                "${i + 1}",
                option.hb.x - 15.0F * Settings.scale,
                option.hb.y + option.hb.height - 25.0F * Settings.scale
            )
        }

        // Hand
        renderHand(sb, font)

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

        // End turn button
        SpireInternals.endTurnHitbox?.let {
            font.draw(
                sb,
                "E",
                it.x,
                it.y + it.height + 20 * Settings.scale,
                it.width,
                Align.center,
                false
            )
        }

        // Combat panels
        if (AbstractDungeon.overlayMenu.combatPanelsShown) {
            // Draw pile
            if (!AbstractDungeon.overlayMenu.combatDeckPanel.isHidden) {
                SpireInternals.drawPilePanelHitbox?.let {
                    font.draw(
                        sb,
                        "A",
                        it.x,
                        it.y + it.height + 30 * Settings.scale,
                        it.width,
                        Align.center,
                        false
                    )
                }
            }

            // Discard pile
            if (!AbstractDungeon.overlayMenu.discardPilePanel.isHidden) {
                SpireInternals.discardPilePanelHitbox?.let {
                    font.draw(
                        sb,
                        "S",
                        it.x - 20.0F * Settings.scale,
                        it.y + it.height - 21.0F * Settings.scale
                    )
                }
            }

            // Exhaust pile
            if (!AbstractDungeon.overlayMenu.exhaustPanel.isHidden && !AbstractDungeon.player.exhaustPile.isEmpty) {
                SpireInternals.exhaustPilePanelHitbox?.let {
                    font.draw(
                        sb,
                        "X",
                        it.x - 20.0F * Settings.scale,
                        it.y + it.height - 35.0F * Settings.scale
                    )
                }
            }

            // Rest room
            val room = SafeSpire.room
            if (room is RestRoom) {
                if (!room.campfireUI.somethingSelected) {
                    SpireInternals.restRoomButtons(room.campfireUI).forEachIndexed { i, it ->
                        font.draw(
                            sb,
                            "${i + 1}",
                            it.hb.x + it.hb.width - 40.0F * Settings.scale,
                            it.hb.y + it.hb.height - 25.0F * Settings.scale
                        )
                    }
                }

                if (!SpireInternals.confirmButtonIsHidden(room.campfireUI.confirmButton)) {
                    room.campfireUI.confirmButton.hb.let {
                        font.draw(
                            sb,
                            "N",
                            it.x,
                            it.y,
                            it.width,
                            Align.center,
                            false
                        )
                    }
                }
            }
        }
    }
}

private fun renderHand(sb: SpriteBatch, font: BitmapFont) {
    if (SafeSpire.hand != null) {
        val size = SafeSpire.hand!!.size
        SafeSpire.hand!!.forEachIndexed { i, card ->
            if (i == 9) {
                font.draw(
                    sb,
                    "0",
                    card.hb.x + card.hb.width * ((i).toFloat() / size),
                    card.hb.y + card.hb.height + 30.0F + Settings.scale
                )
            } else {
                font.draw(
                    sb,
                    "${i + 1}",
                    card.hb.x + card.hb.width * ((i).toFloat() / size),
                    card.hb.y + card.hb.height + 30.0F + Settings.scale
                )
            }
        }
    }
}
