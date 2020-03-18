package ChatTheSpire.chat

import ChatTheSpire.commands
import ChatTheSpire.control.VotingManager
import ChatTheSpire.possiblyCommand
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import java.util.concurrent.Executors
import kotlin.random.Random

object LoadSimulator {
    private val executor = Executors.newSingleThreadExecutor()

    fun listen() {
        executor.execute {
            try {
                for (i in 0..Long.MAX_VALUE) {
                    val command = if (Random.nextBoolean()) {
                        val prefix = commands.random().command.prefix

                        if (Random.nextBoolean()) {
                            prefix
                        } else {
                            if (Random.nextBoolean()) {
                                "$prefix ${Random.nextInt(10)}"
                            } else {
                                "$prefix ${Random.nextInt(10)} ${Random.nextInt(10)}"
                            }
                        }
                    } else {
                        "some random gibberish"
                    }

                    if (AbstractDungeon.isPlayerInDungeon()) {
                        if (possiblyCommand(command)) {
                            VotingManager.vote("user$i", command)
                        }
                    }

                    Thread.sleep(5)
                }
            } catch (e: InterruptedException) {
                // ignore
            }
        }

        Runtime.getRuntime().addShutdownHook(Thread {
            executor.shutdownNow()
        })
    }
}
