package ChatTheSpire

import ChatTheSpire.chat.Discord
import basemod.BaseMod
import basemod.interfaces.PostInitializeSubscriber
import basemod.interfaces.PostRenderSubscriber
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import org.apache.logging.log4j.LogManager

private val logger = LogManager.getLogger(ChatTheSpire::class.java.name)

@SpireInitializer
class ChatTheSpire : PostInitializeSubscriber, PostRenderSubscriber {

    private var hintFont: BitmapFont? = null

    companion object {
        @Suppress("unused")
        @JvmStatic
        fun initialize() {
            logger.info("Initialize ChatTheSpire...")
            @Suppress("UNUSED_VARIABLE")
            val chatTheSpire = ChatTheSpire()
            logger.info("ChatTheSpire initialized.")
        }
    }

    init {
        logger.info("Subscribe to BaseMod hooks")
        BaseMod.subscribe(this)
        logger.info("Done subscribing")
    }

    override fun receivePostInitialize() {
        val generator = FreeTypeFontGenerator(Gdx.files.internal("font/Kreon-Regular.ttf"));
        val parameter = FreeTypeFontGenerator.FreeTypeFontParameter()
        parameter.size = (40.0F * Settings.scale).toInt()
        hintFont = generator.generateFont(parameter)

        initializeSettings()
        initializeConsoleCommands()
        initializeControlPanel()

        Discord.listen()
    }

    override fun receivePostRender(sb: SpriteBatch) {
        if (hintFont == null) {
            return
        }

        GameState.currentScreen = AbstractDungeon.screen
        renderHints(sb, hintFont!!)
    }
}
