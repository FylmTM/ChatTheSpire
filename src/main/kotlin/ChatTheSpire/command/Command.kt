package ChatTheSpire.command

import ChatTheSpire.control.Control

interface Command {

    fun perform(parameters: List<Int>): Boolean =
        execute(parameters, doAction = true)

    fun canPerform(parameters: List<Int>): Boolean =
        execute(parameters, doAction = false)

    fun execute(parameters: List<Int>, doAction: Boolean): Boolean
}
