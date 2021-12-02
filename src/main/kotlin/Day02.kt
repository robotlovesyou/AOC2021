fun main() {
    val result = Reader.strings("input02.txt").fold(State()) {state, command -> state.apply(command)}
    println("part 1: ${result.f * result.d}")

    val resultPt2 = Reader.strings("input02.txt").fold(AimedState()) {state, command -> state.apply(command)}
    println("part2: ${resultPt2.f * resultPt2.d}")
}

fun parts(command: String): Pair<String, Int> {
    val parts = command.split(" ")
    val instruction = parts[0]
    val scale = parts[1].toInt()
    return Pair(instruction, scale)
}

data class State(val f: Int = 0, val d: Int = 0) {
    fun apply(command: String): State {
        val (instruction, scale) = parts(command)
        return when (instruction) {
            "forward" -> copy(f = f + scale)
            "down" -> copy(d = d + scale)
            else -> copy(d = d - scale)
        }
    }
}

data class AimedState(val f: Int = 0, val d: Int = 0, val a: Int = 0) {
    fun apply(command: String): AimedState {
        val (instruction, scale) = parts(command)
        return when (instruction) {
            "forward" -> copy(f = f + scale, d = d + (a * scale))
            "down" -> copy(a = a + scale)
            else -> copy(a = a - scale)
        }
    }
}