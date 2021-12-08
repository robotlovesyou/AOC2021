fun main() {
    val input = DisplayReader.readInput("input08.txt")

    println("part 1 = ${input.countKnown()}")

    println("part 2 = ${input.map {it.decode()}.sum()}")
}

fun List<String>.remove(p: (String) -> Boolean): Pair<String, List<String>> {
    val item = this.filter(p).first()
    return item to this.filterNot(p)
}

fun String.sorted(): String = this.toList().sorted().joinToString("")

data class Input(val signals: List<String>, val dislay: List<String>) {
    private var unknown: List<String> = signals.map{it}

    companion object {
        fun from(s: String): Input {
            val (ins, disps) = s.split(" | ")
            return Input(ins.split(" "), disps.split(" "))
        }
    }

    private fun decodeDigit(n: UShort, p: (String) -> Boolean): Digit {
        val (repr, remain) = unknown.remove(p)
        unknown = remain
        return Digit(n, repr.sorted())
    }


    fun decode(): UInt {
        val one = decodeDigit(1U) { it.length == 2 }
        val seven = decodeDigit(7U) { it.length == 3 }
        val four = decodeDigit(4U) { it.length == 4 }
        val eight = decodeDigit(8U) { it.length == 7 }
        val three = decodeDigit(3U) { it.length == 5 && it.toSet().intersect(one.toSet()).size == 2 }
        val two = decodeDigit(2U) {
            it.length == 5 && it.toSet().subtract(four.toSet()).subtract(three.toSet()).size == 1
        }
        val five = decodeDigit(5U) { it.length == 5 }
        val six = decodeDigit(6U) { it.toSet().intersect(one.toSet()).size == 1 }
        val nine = decodeDigit(9U) { it.toSet().subtract(four.toSet()).size == 2 }
        val zero = decodeDigit(0U) {true}
        val repMap: Map<String, Digit> =
            listOf(one, two, three, four, five, six, seven, eight, nine, zero).fold(mutableMapOf()) { m, d ->
                m[d.repr] = d
                m
            }
        return this.dislay.fold(0U) { acc, repr -> acc * 10U + repMap[repr.sorted()]!!.n }
    }
}

data class Digit(val n: UShort, val repr: String) {
    fun toSet(): Set<Char> = repr.toSet()
}

object DisplayReader {
    fun readInput(filename: String): List<Input> = Reader.stringSeq(filename).map { Input.from(it) }.toList()
}

fun List<Input>.countKnown(): Int = this.asSequence().map {
    it.dislay.fold(0) { sum, d ->
        when (d.length) {
            2, 3, 4, 7 -> sum + 1
            else -> sum
        }
    }
}.sum()