package ChatTheSpire.chat

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

private const val token = "Njg4MDk3NDUxNzUxNTA1OTIw.Xmvgmw.mdoiLpmE_1Mi82Hrf_GR6IOF5r0"

object Discord {
    private var jda: JDA? = null

    fun listen() {
        jda = JDABuilder(token)
            .addEventListeners(MessageListener)
            .build()
    }
}

object MessageListener : ListenerAdapter() {
    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (event.author.isBot) {
            return
        }

        if (event.channel.name == "chat-the-spire") {
            println(event.message)
        }
    }
}
