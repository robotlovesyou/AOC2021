fun main() {
    val result = run(model(Reader.csInts("input06.txt")), 80)
    println("part 1 = ${result.values.sum()}")

    val result2 = run(model(Reader.csInts("input06.txt")), 256)
    println("part 2 = ${result2.values.sum()}")
}

fun model(readings: List<Int>): MutableMap<Int, Long> = readings.fold(mutableMapOf()) { map, reading ->
    map[reading] = map.getOrDefault(reading, 0) + 1
    map
}

fun run(start: MutableMap<Int, Long>, days: Int): MutableMap<Int, Long> = (1..days).fold(start) { state, _ ->
    val next: MutableMap<Int, Long> = state.keys.fold(mutableMapOf()) { m, k ->
        when (k) {
            0 -> m.apply {
                this[8] = state[0]!!
                this[6] = this.getOrDefault(6, 0) + state[0]!!
            }
            else -> m.apply { this[k - 1] = this.getOrDefault(k-1, 0) + state[k]!! }
        }
    }
    next
}

