package ChatTheSpire

import ChatTheSpire.control.ControlPanel
import tornadofx.*

fun initializeControlPanel() {
    Thread {
        launch<ControlPanel>()
    }.start()
}
