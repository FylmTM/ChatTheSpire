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
import com.megacrit.cardcrawl.helpers.Hitbox
import com.megacrit.cardcrawl.potions.PotionSlot
import com.megacrit.cardcrawl.rooms.AbstractRoom
import com.megacrit.cardcrawl.rooms.RestRoom
import com.megacrit.cardcrawl.rooms.ShopRoom
import com.megacrit.cardcrawl.rooms.TreasureRoom
import com.megacrit.cardcrawl.rooms.TreasureRoomBoss

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
    val room = SafeSpire.room

    renderState(sb, gameState)
    renderOverlay(sb, font)

    if (AbstractDungeon.isScreenUp) {
        renderScreens(sb, font)
    } else {
        renderCombatRoom(sb, font)
        renderCombatPanels(sb, font)
        renderEvents(sb, font, room)

        when (room) {
            is RestRoom -> {
                renderRestRoom(sb, font, room)
            }
            is TreasureRoom -> {
                renderTreasureRoom(sb, font, room)
            }
            is TreasureRoomBoss -> {
                renderTreasureRoomBoss(sb, font, room)
            }
            is ShopRoom -> {
                renderShopRoom(sb, font, room)
            }
        }
    }
}

fun renderState(sb: SpriteBatch, gameState: GameState.State) {
    FontHelper.renderFont(
        sb,
        FontHelper.tipBodyFont,
        gameState.debug,
        30.0F * Settings.scale,
        Settings.HEIGHT * 0.80F,
        Color.WHITE
    )
}

fun renderOverlay(sb: SpriteBatch, font: BitmapFont) {
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
        renderTextBelowCentered("D", AbstractDungeon.topPanel.deckHb, sb, font, yAlign = -5.0F)
    }

    // Overlay proceed
    if (!SpireInternals.proceedIsHidden) {
        SpireInternals.proceedHitbox?.let {
            renderTextBelowCentered("N", it, sb, font)
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

    // Overlay cancel
    if (!AbstractDungeon.overlayMenu.cancelButton.isHidden) {
        AbstractDungeon.overlayMenu.cancelButton.hb?.let {
            renderTextBelowCentered("B", it, sb, font)
        }
        SpireInternals.proceedHitbox
    }
}

private fun renderScreens(sb: SpriteBatch, font: BitmapFont) {
    when (GameState.currentScreen) {
        AbstractDungeon.CurrentScreen.MAP -> {
            SafeSpire.nextMapNodes.forEachIndexed { i, node ->
                renderTextBelowCentered("${i + 1}", node.hb, sb, font)
            }
            if (SafeSpire.isNextRoomBoss) {
                val hb = AbstractDungeon.dungeonMapScreen.map.bossHb
                renderTextBelowCentered("boss", hb, sb, font, yAlign = -10.0F)
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
                renderTextBelowCentered("${i + 1}", card.hb, sb, font, yAlign = -10.0F)
            }

            SpireInternals.cardRewardSkipButton?.let {
                if (!SpireInternals.skipButtonIsHidden(it)) {
                    renderTextBelowCentered("N", it.hb, sb, font)
                }
            }
            SpireInternals.cardRewardBowlButton?.let {
                if (!it.isHidden) {
                    renderTextBelowCentered("bowl", it.hb, sb, font)
                }
            }
        }
        AbstractDungeon.CurrentScreen.GRID -> {
            if (AbstractDungeon.gridSelectScreen?.confirmScreenUp == false) {
                AbstractDungeon.gridSelectScreen?.targetGroup?.group?.forEachIndexed { i, card ->
                    renderTextBelowCentered("${i + 1}", card.hb, sb, font, yAlign = 25.0F)
                }
            }

            if (!SpireInternals.gridSelectConfirmButtonIsHidden) {
                AbstractDungeon.gridSelectScreen.confirmButton.hb.let {
                    renderTextBelowCentered("N", it, sb, font)
                }
            }
        }
        AbstractDungeon.CurrentScreen.HAND_SELECT -> {
            if (!SpireInternals.cardSelectConfirmButtonIsHidden) {
                AbstractDungeon.handCardSelectScreen.button.hb.let {
                    renderTextBelowCentered("N", it, sb, font)
                }
            }
            renderHand(sb, font)
        }
        AbstractDungeon.CurrentScreen.BOSS_REWARD -> {
            AbstractDungeon.bossRelicScreen?.relics?.forEachIndexed { i, relic ->
                renderTextBelowCentered("${i + 1}", relic.hb, sb, font)
            }
            val cancelButton = SpireInternals.bossRelicCancelButton
            val confirmButton = AbstractDungeon.bossRelicScreen?.confirmButton

            if (cancelButton != null && !cancelButton.isHidden) {
                cancelButton.hb?.let {
                    renderTextBelowCentered("B", it, sb, font)
                }
            }

            if (confirmButton != null && !SpireInternals.confirmButtonIsHidden(confirmButton)) {
                confirmButton.hb?.let {
                    renderTextBelowCentered("N", it, sb, font)
                }
            }
        }
        AbstractDungeon.CurrentScreen.SHOP -> {
            val yAlign = 40.0F
            val shop = AbstractDungeon.shopScreen
            shop.coloredCards.forEachIndexed { i, card ->
                renderTextAboveCentered("1${i + 1}", card.hb, sb, font, yAlign = yAlign)
            }
            shop.colorlessCards.forEachIndexed { i, card ->
                renderTextAboveCentered("2${i + 1}", card.hb, sb, font, yAlign = yAlign)
            }
            SpireInternals.shopRelics.forEach { relic ->
                if (!relic.isPurchased) {
                    val slot = SpireInternals.storeRelicSlot(relic)
                    renderTextAboveCentered("3${slot + 1}", relic.relic.hb, sb, font, yAlign = yAlign)
                }
            }
            SpireInternals.shopPotions.forEach { potion ->
                if (!potion.isPurchased) {
                    val slot = SpireInternals.storePotionSlot(potion)
                    renderTextAboveCentered("4${slot + 1}", potion.potion.hb, sb, font, yAlign = yAlign)
                }
            }

            if (shop.purgeAvailable) {
                val width = 110.0F * Settings.scale;
                val height = 150.0F * Settings.scale;
                font.draw(
                    sb,
                    "R",
                    SpireInternals.shopPurgeCardX - width,
                    SpireInternals.shopPurgeCardY + height + (yAlign * Settings.scale),
                    width * 2,
                    Align.center,
                    false
                )
            }
        }
        else -> {
        }
    }
}

fun renderEvents(sb: SpriteBatch, font: BitmapFont, room: AbstractRoom?) {
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
    room?.event?.imageEventText?.optionList?.forEachIndexed { i, option ->
        font.draw(
            sb,
            "${i + 1}",
            option.hb.x - 15.0F * Settings.scale,
            option.hb.y + option.hb.height - 25.0F * Settings.scale
        )
    }
}

fun renderCombatPanels(sb: SpriteBatch, font: BitmapFont) {
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
    }
}

fun renderCombatRoom(sb: SpriteBatch, font: BitmapFont) {
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
}

fun renderRestRoom(sb: SpriteBatch, font: BitmapFont, room: RestRoom) {
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
            renderTextBelowCentered("N", it, sb, font)
        }
    }
}

fun renderTreasureRoom(sb: SpriteBatch, font: BitmapFont, room: TreasureRoom) {
    val chest = room.chest
    SpireInternals.chestHitbox(chest)?.let {
        renderTextBelowCentered("T", it, sb, font)
    }
}

fun renderTreasureRoomBoss(sb: SpriteBatch, font: BitmapFont, room: TreasureRoomBoss) {
    val chest = room.chest
    SpireInternals.chestHitbox(chest)?.let {
        renderTextBelowCentered("T", it, sb, font, yAlign = -30.0F)
    }
}

fun renderShopRoom(sb: SpriteBatch, font: BitmapFont, room: ShopRoom) {
    room.merchant?.hb?.let {
        renderTextBelowCentered("M", it, sb, font)
    }
}

private fun renderHand(sb: SpriteBatch, font: BitmapFont) {
    val hand = SafeSpire.hand
    if (hand != null) {
        val size = hand.size
        hand.forEachIndexed { i, card ->
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

fun renderTextBelowCentered(text: String, hb: Hitbox, sb: SpriteBatch, font: BitmapFont, yAlign: Float = 0.0F) {
    font.draw(
        sb,
        text,
        hb.x,
        hb.y + (yAlign * Settings.scale),
        hb.width,
        Align.center,
        false
    )
}

fun renderTextAboveCentered(text: String, hb: Hitbox, sb: SpriteBatch, font: BitmapFont, yAlign: Float = 0.0F) {
    font.draw(
        sb,
        text,
        hb.x,
        hb.y + hb.height + (yAlign * Settings.scale),
        hb.width,
        Align.center,
        false
    )
}
