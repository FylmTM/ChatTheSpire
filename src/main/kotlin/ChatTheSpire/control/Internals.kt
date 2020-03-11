package ChatTheSpire.control

import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.Hitbox
import com.megacrit.cardcrawl.map.MapRoomNode
import com.megacrit.cardcrawl.screens.DungeonMapScreen
import com.megacrit.cardcrawl.ui.buttons.EndTurnButton
import com.megacrit.cardcrawl.ui.panels.PotionPopUp

fun <T> Class<T>.getAsPublicField(name: String) =
    this.getDeclaredField(name).also {
        it.isAccessible = true
    }!!

object Internals {

    private val potionUseField = PotionPopUp::class.java
        .getAsPublicField("hbTop")
    private val potionDestroyField = PotionPopUp::class.java
        .getAsPublicField("hbBot")
    private val endTurnField = EndTurnButton::class.java
        .getAsPublicField("hb")
    private val visibleMapNodesField = DungeonMapScreen::class.java
        .getAsPublicField("visibleMapNodes")

    val potionUseHitbox: Hitbox?
        get() = potionUseField.get(AbstractDungeon.topPanel.potionUi) as Hitbox

    val potionDestroyHitbox: Hitbox?
        get() = potionDestroyField.get(AbstractDungeon.topPanel.potionUi) as Hitbox

    val endTurnHitbox: Hitbox?
        get() = endTurnField.get(AbstractDungeon.overlayMenu.endTurnButton) as Hitbox

    val visibleMapNodes: ArrayList<MapRoomNode>
        get() = visibleMapNodesField.get(AbstractDungeon.dungeonMapScreen) as ArrayList<MapRoomNode>
}
