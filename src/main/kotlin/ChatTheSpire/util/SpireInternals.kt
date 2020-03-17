package ChatTheSpire.util

import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.Hitbox
import com.megacrit.cardcrawl.map.MapRoomNode
import com.megacrit.cardcrawl.rewards.chests.AbstractChest
import com.megacrit.cardcrawl.rooms.CampfireUI
import com.megacrit.cardcrawl.screens.CardRewardScreen
import com.megacrit.cardcrawl.screens.DungeonMapScreen
import com.megacrit.cardcrawl.screens.mainMenu.MenuCancelButton
import com.megacrit.cardcrawl.screens.select.BossRelicSelectScreen
import com.megacrit.cardcrawl.ui.buttons.CardSelectConfirmButton
import com.megacrit.cardcrawl.ui.buttons.ConfirmButton
import com.megacrit.cardcrawl.ui.buttons.EndTurnButton
import com.megacrit.cardcrawl.ui.buttons.GridSelectConfirmButton
import com.megacrit.cardcrawl.ui.buttons.ProceedButton
import com.megacrit.cardcrawl.ui.buttons.SingingBowlButton
import com.megacrit.cardcrawl.ui.buttons.SkipCardButton
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption
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
    private val cardRewardBowlButtonField = CardRewardScreen::class.java
        .getAsPublicField("bowlButton")
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
    private val restRoomButtonsField = CampfireUI::class.java
        .getAsPublicField("buttons")
    private val confirmButtonIsHiddenField = ConfirmButton::class.java
        .getAsPublicField("isHidden")
    private val gridSelectConfirmButtonIsHiddenField = GridSelectConfirmButton::class.java
        .getAsPublicField("isHidden")
    private val cardSelectConfirmButtonIsHiddenField = CardSelectConfirmButton::class.java
        .getAsPublicField("isHidden")
    private val chestHitboxField = AbstractChest::class.java
        .getAsPublicField("hb")
    private val bossRelicCancelButtonField = BossRelicSelectScreen::class.java
        .getAsPublicField("cancelButton")

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
        get() = cardRewardSkipButtonField.get(AbstractDungeon.cardRewardScreen) as SkipCardButton?

    val cardRewardBowlButton: SingingBowlButton?
        get() = cardRewardBowlButtonField.get(AbstractDungeon.cardRewardScreen) as SingingBowlButton?

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

    @Suppress("UNCHECKED_CAST")
    fun restRoomButtons(campfireUI: CampfireUI): ArrayList<AbstractCampfireOption> =
        restRoomButtonsField.get(campfireUI) as ArrayList<AbstractCampfireOption>

    fun confirmButtonIsHidden(button: ConfirmButton): Boolean = confirmButtonIsHiddenField.getBoolean(button)

    val gridSelectConfirmButtonIsHidden: Boolean
        get() = gridSelectConfirmButtonIsHiddenField.getBoolean(AbstractDungeon.gridSelectScreen.confirmButton)

    val cardSelectConfirmButtonIsHidden: Boolean
        get() = cardSelectConfirmButtonIsHiddenField.getBoolean(AbstractDungeon.handCardSelectScreen.button)

    fun chestHitbox(chest: AbstractChest): Hitbox? =
        chestHitboxField.get(chest) as Hitbox?

    val bossRelicCancelButton: MenuCancelButton?
        get() = bossRelicCancelButtonField.get(AbstractDungeon.bossRelicScreen) as MenuCancelButton?
}
