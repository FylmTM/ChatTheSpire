package ChatTheSpire.command

interface Command {

    fun perform(parameters: List<Int>): Boolean

    fun canPerform(parameters: List<Int>): Boolean
}
