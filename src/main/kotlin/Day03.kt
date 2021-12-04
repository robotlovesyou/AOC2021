val masks = listOf(1u, 2u, 4u, 8u, 16u, 32u, 64u, 128u, 256u, 512u, 1024u, 2048u)

fun main() {
    val readings = Reader.ints("input03.txt", 2).map { it.toUInt() }
    val c = counts(readings)
    val g = gamma(readings.size.toUInt(), c)
    val e = g.inv().and(4095u)
    println("part 1 = ${g * e}")

    val o2Reading = o2(readings, 11)
    val co2Reading = co2(readings, 11)

    println("part 2 = ${o2Reading * co2Reading}")
}

fun counts(l: List<UInt>): List<UInt> =
    l.fold(listOf(0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u)) { counts, reading ->
        counts.mapIndexed { i, count ->
            if (masks[i].and(reading) > 0u) {
                count + 1u
            } else {
                count
            }
        }
    }

fun count(l: List<UInt>, i: Int): UInt = l.fold(0u) { count, reading ->
    if (masks[i].and(reading) > 0u) {
        count + 1u
    } else {
        count
    }
}

fun gamma(size: UInt, l: List<UInt>): UInt = l.foldIndexed(0u) { i, acc, count ->
    if (count >= size - count) {
        acc + (1u).shl(i)
    } else {
        acc
    }
}

fun filter(l: List<UInt>, i: Int, tf: (c: UInt, s: UInt, i: Int) -> (UInt) -> Boolean): UInt {
    val c = count(l, i)
    val t = tf(c, l.size.toUInt(), i)
    val filtered = l.filter(t)
    return if (filtered.size == 1) {
        filtered.first()
    } else {
        filter(filtered, i - 1, tf)
    }
}

fun o2(l: List<UInt>, i: Int): UInt = filter(l, i) { c, s, ix ->
    { it: UInt ->
        if (c >= s - c) {
            it.and(masks[ix]) > 0u
        } else {
            it.and(masks[ix]) == 0u
        }
    }
}

fun co2(l: List<UInt>, i: Int): UInt = filter(l, i) { c, s, ix ->
    { it: UInt ->
        if (c < s - c) {
            it.and(masks[ix]) > 0u
        } else {
            it.and(masks[ix]) == 0u
        }
    }
}

