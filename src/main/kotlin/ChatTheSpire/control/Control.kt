package ChatTheSpire.control

import com.badlogic.gdx.Gdx
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.helpers.Hitbox
import java.awt.Robot
import java.awt.event.InputEvent

object Control {

    private val bot = Robot()

    fun sleep(millis: Long) {
        Thread.sleep(millis)
    }

    fun quickSleep() {
        sleep(100)
    }

    fun shortSleep() {
        sleep(250)
    }

    fun normalSleep() {
        sleep(500)
    }

    fun longSleep() {
        sleep(1000)
    }

    fun veryLongSleep() {
        sleep(5000)
    }

    fun hover(hitbox: Hitbox) {
        Gdx.input.setCursorPosition(hitbox.cX.toInt(), Settings.HEIGHT - hitbox.cY.toInt())
        quickSleep()
    }

    fun click() {
        bot.mousePress(InputEvent.BUTTON1_DOWN_MASK)
        quickSleep()
        bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK)
        quickSleep()
    }

    fun click(hitbox: Hitbox) {
        hover(hitbox)
        click()
    }
}
