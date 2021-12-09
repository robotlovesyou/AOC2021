package day05

import kotlin.math.absoluteValue

fun main() {
    val hOrVLines = LineReader.read("input05.txt").filter { it.hOrV() }
    val field = Field(hOrVLines.topLeft(), hOrVLines.bottomRight())
    hOrVLines.fold(field) {acc, l ->
        acc.applyLine(l)
        acc
    }
    println("part 1 = ${field.points.filter {it > 1}.size}")

    val allLines = LineReader.read("input05.txt")
    val allField = Field(allLines.topLeft(), allLines.bottomRight())
    allLines.fold(allField) { acc, l ->
        acc.applyLine(l)
        acc
    }

    println("part 2 = ${allField.points.filter { it > 1 }.size}")
}

fun List<Line>.topLeft(): Point = this.fold(Point(Int.MAX_VALUE, Int.MAX_VALUE)) { acc, line ->
    Point(
        line.end.x.coerceAtMost(line.start.x.coerceAtMost(acc.x)),
        line.end.y.coerceAtMost(line.start.y.coerceAtMost(acc.y))
    )
}

fun List<Line>.bottomRight(): Point = this.fold(Point(Int.MIN_VALUE, Int.MIN_VALUE)) { acc, line ->
    Point(
        line.start.x.coerceAtLeast(line.end.x.coerceAtLeast(acc.x)),
        line.start.y.coerceAtLeast(line.end.y.coerceAtLeast(acc.y))
    )
}

object LineReader {
    fun read(filename: String): List<Line> = Reader.stringSeq(filename).map {
        Line.from(it)
    }.toList()
}

data class Point(val x: Int, val y: Int) {
    companion object {
        fun from(strCoords: String): Point = strCoords.let {
            val (x, y) = it.split(",")
            Point(x.toInt(), y.toInt())
        }
    }

    fun unitVectorTo(other: Point): Pair<Int, Int> {
        val dx = other.x - this.x
        val dy = other.y - this.y
        return Pair(unitVec(dx), unitVec(dy))
    }

    private fun unitVec(d: Int): Int = if (d == 0) {d} else {d/d.absoluteValue}
}

data class Line(val start: Point, val end: Point) {
    companion object {
        fun from(strLine: String): Line = strLine.let {
            val (start, end) = strLine.split(" -> ")
            Line(Point.from(start), Point.from(end))
        }
    }

    private fun horizontal(): Boolean = start.y == end.y
    private fun vertical(): Boolean = start.x == end.x
    fun hOrV(): Boolean = horizontal() || vertical()
}

class Field(private val topLeft: Point, bottomRight: Point) {
    private val width = bottomRight.x - (topLeft.x - 1)
    private val height = bottomRight.y - (topLeft.y - 1)
    val points: MutableList<Int> = MutableList(width * height) { 0 }

    private fun indexFor(p: Point): Int = (p.y - topLeft.y) * width + (p.x - topLeft.x)

    private fun incrPoint(p: Point) {
        points[indexFor(p)] += 1
    }

    fun applyLine(l: Line) {
        val (ix, iy) = l.start.unitVectorTo(l.end)
        var p = l.start
        while (true) {
            incrPoint(p)
            if (p == l.end) {
                break
            }
            p = Point(x=p.x + ix, y=p.y + iy)
        }
    }
}