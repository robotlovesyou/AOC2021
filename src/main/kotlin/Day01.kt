fun main() {
    val totalDrops = Reader.ints("input01.txt").let {
        it.fold(Drops(last = it.first(), drops = 0)) { acc, current ->
            val drops = if (current > acc.last) {
                acc.drops + 1
            } else {
                acc.drops
            }
            Drops(last = current, drops)
        }
    }.drops
    println("total drops = $totalDrops")

    val windowed = Reader.ints("input01.txt").let {
        val window = Window(a = it[0], b = it[1], c = it[2])
        it.drop(3).fold(window) { acc, current ->
            val increases = if (acc.b + acc.c + current > acc.sum()) {
                acc.increases + 1
            } else {
                acc.increases
            }
            Window(a = acc.b, b = acc.c, c = current, increases)
        }
    }.increases

    println("increases = $windowed")

}

data class Drops(val last: Int, val drops: Int)
data class Window(val a: Int, val b: Int, val c: Int, val increases: Int = 0) {
    fun sum(): Int = a + b + c
}