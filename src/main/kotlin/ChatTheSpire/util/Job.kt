package ChatTheSpire.util

import java.util.concurrent.Executors

object Job {

    private val executor = Executors.newFixedThreadPool(10)

    fun execute(runnable: () -> Unit) {
        executor.execute(runnable)
    }
}
