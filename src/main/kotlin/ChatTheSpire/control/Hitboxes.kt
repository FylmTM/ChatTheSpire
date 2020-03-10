package ChatTheSpire.control

import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.Hitbox
import com.megacrit.cardcrawl.ui.buttons.EndTurnButton
import com.megacrit.cardcrawl.ui.panels.PotionPopUp

fun <T> Class<T>.getAsPublicField(name: String) =
    this.getDeclaredField(name).also {
        it.isAccessible = true
    }!!

object Hitboxes {

    private val potionUseField = PotionPopUp::class.java
        .getAsPublicField("hbTop")
    private val potionDestroyField = PotionPopUp::class.java
        .getAsPublicField("hbBot")
    private val endTurnField = EndTurnButton::class.java
        .getAsPublicField("hb")

    val potionUse: Hitbox?
        get() = potionUseField.get(AbstractDungeon.topPanel.potionUi) as Hitbox

    val potionDestroy: Hitbox?
        get() = potionDestroyField.get(AbstractDungeon.topPanel.potionUi) as Hitbox

    val endTurn: Hitbox?
        get() = endTurnField.get(AbstractDungeon.overlayMenu.endTurnButton) as Hitbox
}
