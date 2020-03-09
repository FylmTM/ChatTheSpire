package ChatTheSpire.util

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Texture.TextureFilter
import com.badlogic.gdx.utils.GdxRuntimeException
import org.apache.logging.log4j.LogManager
import java.util.HashMap

private val logger = LogManager.getLogger(TextureLoader::class.java.name)

/**
 * Thank you Blank The Evil!
 * Welcome to the utilities package. This package is for small utilities that make our life easier.
 * You honestly don't need to bother with this unless you want to know how we're loading the textures.
 */
object TextureLoader {
    private val textures = HashMap<String, Texture>()

    /**
     * @param textureString - String path to the texture you want to load relative to resources,
     * Example: "theDefaultResources/images/ui/missing_texture.png"
     * @return **com.badlogic.gdx.graphics.Texture** - The texture from the path provided
     */
    fun getTexture(textureString: String): Texture {
        return textures[textureString]
            ?: try {
                loadTexture(textureString)
            } catch (e: GdxRuntimeException) {
                logger.error("Could not find texture: $textureString")
                getTexture("ChatTheSpire/images/ui/missing_texture.png")
            }
    }

    /**
     * Creates an instance of the texture, applies a linear filter to it, and places it in the HashMap
     *
     * @param textureString - String path to the texture you want to load relative to resources,
     * Example: "img/ui/missingtexture.png"
     */
    private fun loadTexture(textureString: String): Texture {
        logger.info("Loading Texture: $textureString")
        val texture = Texture(textureString)
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear)
        textures[textureString] = texture
        return texture
    }
}
