package ChatTheSpire.chat

import ChatTheSpire.control.VotingManager
import ChatTheSpire.possiblyCommand
import org.javacord.api.DiscordApi
import org.javacord.api.DiscordApiBuilder

private const val token = "Njg4MDk3NDUxNzUxNTA1OTIw.Xmvgmw.mdoiLpmE_1Mi82Hrf_GR6IOF5r0"

//object Discord {
//    private var jda: JDA? = null
//
//    fun listen() {
//        jda = JDABuilder(token)
//            .addEventListeners(MessageListener)
//            .build()
//    }
//}
//
//object MessageListener : ListenerAdapter() {
//    override fun onMessageReceived(event: MessageReceivedEvent) {
//        if (event.author.isBot) {
//            return
//        }
//
//        if (event.channel.name == "chat-the-spire") {
//            println(event.message)
//        }
//    }
//}

object Discord {

    var api: DiscordApi? = null

    fun listen() {
        api = DiscordApiBuilder().setToken(token)
            .login()
            .join()

        // Add a listener which answers with "Pong!" if someone writes "!ping"
        api?.addMessageCreateListener { event ->
            if (!event.messageAuthor.isBotUser) {
                event.channel.asServerChannel().ifPresent {
                    if (it.name == "chat-the-spire") {
                        val command = event.message.content
                        if (possiblyCommand(command)) {
                            VotingManager.vote(event.message.author.displayName, command)
                        }
                    }
                }
            }
        }
    }
}
