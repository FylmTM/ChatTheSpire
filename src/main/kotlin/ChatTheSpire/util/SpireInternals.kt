package ChatTheSpire.util

import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.Hitbox
import com.megacrit.cardcrawl.map.MapRoomNode
import com.megacrit.cardcrawl.screens.CardRewardScreen
import com.megacrit.cardcrawl.screens.DungeonMapScreen
import com.megacrit.cardcrawl.ui.buttons.EndTurnButton
import com.megacrit.cardcrawl.ui.buttons.ProceedButton
import com.megacrit.cardcrawl.ui.buttons.SkipCardButton
import com.megacrit.cardcrawl.ui.panels.DiscardPilePanel
import com.megacrit.cardcrawl.ui.panels.DrawPilePanel
import com.megacrit.cardcrawl.ui.panels.ExhaustPanel
import com.megacrit.cardcrawl.ui.panels.PotionPopUp
import com.megacrit.cardcrawl.ui.panels.TopPanel

fun <T> Class<T>.getAsPublicField(name: String) =
    this.getDeclaredField(name).also {
        it.isAccessible = true
    }!!

object SpireInternals {

    private val potionUseHitboxField = PotionPopUp::class.java
        .getAsPublicField("hbTop")
    private val potionDestroyHitboxField = PotionPopUp::class.java
        .getAsPublicField("hbBot")
    private val endTurnHitboxField = EndTurnButton::class.java
        .getAsPublicField("hb")
    private val proceedHitboxField = ProceedButton::class.java
        .getAsPublicField("hb")
    private val proceedIsHiddenField = ProceedButton::class.java
        .getAsPublicField("isHidden")
    private val cardRewardSkipButtonField = CardRewardScreen::class.java
        .getAsPublicField("skipButton")
    private val skipCardButtonIsHiddenField = SkipCardButton::class.java
        .getAsPublicField("isHidden")
    private val visibleMapNodesField = DungeonMapScreen::class.java
        .getAsPublicField("visibleMapNodes")
    private val deckButtonDisabledField = TopPanel::class.java
        .getAsPublicField("deckButtonDisabled")
    private val drawPilePanelHitboxField = DrawPilePanel::class.java
        .getAsPublicField("hb")
    private val discardPilePanelHitboxField = DiscardPilePanel::class.java
        .getAsPublicField("hb")
    private val exhaustPilePanelHitboxField = ExhaustPanel::class.java
        .getAsPublicField("hb")

    val potionUseHitbox: Hitbox?
        get() = potionUseHitboxField.get(AbstractDungeon.topPanel.potionUi) as Hitbox?

    val potionDestroyHitbox: Hitbox?
        get() = potionDestroyHitboxField.get(AbstractDungeon.topPanel.potionUi) as Hitbox?

    val endTurnHitbox: Hitbox?
        get() = endTurnHitboxField.get(AbstractDungeon.overlayMenu?.endTurnButton) as Hitbox?

    val proceedHitbox: Hitbox?
        get() = proceedHitboxField.get(AbstractDungeon.overlayMenu?.proceedButton) as Hitbox?

    val proceedIsHidden: Boolean
        get() = proceedIsHiddenField.getBoolean(AbstractDungeon.overlayMenu.proceedButton)

    val cardRewardSkipButton: SkipCardButton?
        get() = cardRewardSkipButtonField.get(AbstractDungeon.cardRewardScreen) as SkipCardButton

    fun skipButtonIsHidden(button: SkipCardButton): Boolean {
        return skipCardButtonIsHiddenField.getBoolean(button)
    }

    val deckButtonDisabled: Boolean
        get() = deckButtonDisabledField.getBoolean(AbstractDungeon.topPanel)

    val drawPilePanelHitbox: Hitbox?
        get() = drawPilePanelHitboxField.get(AbstractDungeon.overlayMenu.combatDeckPanel) as Hitbox?

    val discardPilePanelHitbox: Hitbox?
        get() = discardPilePanelHitboxField.get(AbstractDungeon.overlayMenu.discardPilePanel) as Hitbox?

    val exhaustPilePanelHitbox: Hitbox?
        get() = exhaustPilePanelHitboxField.get(AbstractDungeon.overlayMenu.exhaustPanel) as Hitbox?

    @Suppress("UNCHECKED_CAST")
    val visibleMapNodes: ArrayList<MapRoomNode>
        get() = visibleMapNodesField.get(AbstractDungeon.dungeonMapScreen) as ArrayList<MapRoomNode>
}
