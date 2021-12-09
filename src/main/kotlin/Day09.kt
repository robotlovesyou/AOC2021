fun main() {
    val cavebed = Cavebed(CavebedReader.read("input09.txt"))
    println("part 1 = ${cavebed.sumRisks()}")
}

class Cavebed(readings: List<List<UShort>>) {
    private val map = initMap(readings)

    private fun initMap(readings: List<List<UShort>>): List<List<UShort>> {
        val capped = readings.map { listOf(UShort.MAX_VALUE) + it + listOf(UShort.MAX_VALUE)}
        return listOf(List(capped.first().size) {UShort.MAX_VALUE}) + capped + listOf(List(capped.first().size) {UShort.MAX_VALUE})
    }

    private fun isLow(x: Int, y: Int): Boolean {
        val point = map[y][x]
        return point < map[y-1][x] && point < map[y+1][x] && point < map[y][x-1] && point < map[y][x+1]
    }

    fun sumRisks(): UInt {
        var sum = 0U
        for (y in 1 until map.size-1) {
            for (x in 1 until map[y].size-1) {
                if (isLow(x, y)) {
                    sum += map[y][x] + 1U
                }
            }
        }
        return sum
    }
}

object CavebedReader {
    fun read(filename: String): List<List<UShort>> =
        Reader.stringSeq(filename).map { line ->
            line
                .toList()
                .map { it.toString().toUShort() }
        }
            .toList()
}