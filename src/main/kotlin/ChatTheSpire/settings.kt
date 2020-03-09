package ChatTheSpire

import ChatTheSpire.util.TextureLoader
import basemod.BaseMod
import basemod.ModPanel
import org.apache.logging.log4j.LogManager

private val logger = LogManager.getLogger(ChatTheSpire::class.java.name)

fun initializeSettings() {
    logger.info("Initialize ChatTheSpire settings...")
    val badgeTexture = TextureLoader.getTexture("ChatTheSpireResources/images/Badge.png")
    val settingsPanel = ModPanel()
    BaseMod.registerModBadge(
        badgeTexture,
        "Chat The Spire",
        "MrLoqo",
        "Chat The Spire",
        settingsPanel
    )
    logger.info("ChatTheSpire settings initialized.")
}
