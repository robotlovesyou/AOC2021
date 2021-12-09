package day07

import kotlin.math.absoluteValue

fun main() {
    val positions = Reader.csInts("input07.txt").map { it.toLong() }

    val part1 = cheapest(positions) { target ->
        { start ->
            (target - start).absoluteValue
        }
    }

    println("part 1 = $part1")

    val part2 = cheapest(positions) { target ->
        { start ->
            val d = (target - start).absoluteValue
            (d * (d + 1)) / 2
        }
    }

    println("part 2 = $part2")
}

fun cheapest(positions: List<Long>, cost: (Long) -> (Long) -> Long): Long =
    (positions.minOf { it }..positions.maxOf { it }).toList().fold(0L to Long.MAX_VALUE) { (bp, sv), n ->
        val v = positions.sumOf(cost(n))
        if (v < sv) {
            n to v
        } else {
            bp to sv
        }
    }.second