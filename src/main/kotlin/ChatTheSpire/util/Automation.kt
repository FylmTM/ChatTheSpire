package ChatTheSpire.util

import com.badlogic.gdx.Gdx
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.helpers.Hitbox
import java.awt.Robot
import java.awt.event.InputEvent

object Automation {

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

    fun rest() {
        Gdx.input.setCursorPosition(10, 35)
    }

    fun keyPress(key: Int) {
        bot.keyPress(key)
        quickSleep()
        bot.keyRelease(key)
        quickSleep()
    }

    fun hover(hitbox: Hitbox) {
        Gdx.input.setCursorPosition(hitbox.cX.toInt(), Settings.HEIGHT - hitbox.cY.toInt())
        quickSleep()
    }

    fun hover(x: Int, y: Int) {
        Gdx.input.setCursorPosition(x, Settings.HEIGHT - y)
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

    fun scrollTo(hb: Hitbox) {
        for (i in 0..50) {
            val y = (Settings.HEIGHT - hb.cY.toInt())

            when {
                // up
                y < 150 * Settings.scale -> {
                    bot.mouseWheel(-10)
                    quickSleep()
                }
                // down
                y > Settings.HEIGHT - 100 * Settings.scale -> {
                    bot.mouseWheel(10)
                    quickSleep()
                }
                // visible
                else -> {
                    return
                }
            }
        }
        shortSleep()
    }
}
