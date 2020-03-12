package ChatTheSpire

import basemod.BaseMod
import basemod.interfaces.PostInitializeSubscriber
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer
import org.apache.logging.log4j.LogManager

private val logger = LogManager.getLogger(ChatTheSpire::class.java.name)

@SpireInitializer
class ChatTheSpire : PostInitializeSubscriber {

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
        initializeSettings()
        initializeConsoleCommands()
        initializeControlPanel()
    }
}
