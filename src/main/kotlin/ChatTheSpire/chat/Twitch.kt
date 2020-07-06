package ChatTheSpire.chat

import ChatTheSpire.control.VotingManager
import ChatTheSpire.possiblyCommand
import com.gikk.twirk.TwirkBuilder
import com.gikk.twirk.events.TwirkListener
import com.gikk.twirk.types.twitchMessage.TwitchMessage
import com.gikk.twirk.types.users.TwitchUser
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import java.io.IOException

// WIP: Extract to external configuration
private const val token = "CHANGEME"

object Twitch {
    fun listen() {
        val twirk = TwirkBuilder("#mrloqo", "mrloqo", token)
            .setVerboseMode(true)
            .build()

        twirk.addIrcListener(object : TwirkListener {
            override fun onDisconnect() {
                try {
                    if (!twirk.connect())  {
                        twirk.close()
                    }
                } catch (e: IOException) {
                    twirk.close()
                } catch (e: InterruptedException) {
                    // ignore
                }
            }

            override fun onPrivMsg(sender: TwitchUser, message: TwitchMessage) {
                if (AbstractDungeon.isPlayerInDungeon()) {
                    val command = message.content
                    if (possiblyCommand(command)) {
                        VotingManager.vote(sender.displayName, command)
                    }
                }
            }
        })
        twirk.connect()
        Runtime.getRuntime().addShutdownHook(Thread {
            twirk.close()
        })
    }
}

fun main() {
    Twitch.listen()
}
