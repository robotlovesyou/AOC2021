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

    fun left(other: Point) = if (this.x <= other.x) {this} else {other}
    fun right(other: Point) = if (this.x > other.x) {this} else {other}
    fun top(other: Point) = if (this.y <= other.y) {this} else {other}
    fun bottom(other: Point) = if (this.y > other.y) {this} else {other}
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
            val a = Point.from(start)
            val b = Point.from(end)
            Line(Point.from(start), Point.from(end))
        }
    }

    fun horizontal(): Boolean = start.y == end.y
    fun vertical(): Boolean = start.x == end.x
    fun hOrV(): Boolean = horizontal() || vertical()
}

class Field(val topLeft: Point, val bottomRight: Point) {
    val width = bottomRight.x - (topLeft.x - 1)
    val height = bottomRight.y - (topLeft.y - 1)
    val points: MutableList<Int> = MutableList(width * height) { 0 }

    fun indexFor(p: Point): Int = (p.y - topLeft.y) * width + (p.x - topLeft.x)

    fun incrPoint(p: Point): Unit {
        points[indexFor(p)] += 1
    }

    fun at(p: Point): Int = points[indexFor(p)]

    fun applyLine(l: Line): Unit {
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