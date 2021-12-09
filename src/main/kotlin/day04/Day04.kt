package day04

fun main() {
    val state = BoardReader.read(Reader.stringSeq("input04.txt"))
    println("part 1 = ${playToWin(state.header, state.boards)}")
    println("part 2 = ${playToLoose(state.header, state.boards)}")
}

fun playToWin(header: List<Int>, boards: List<Board>): Int {
    var b = boards
    for (n in header) {
        b = b.map { it.mark(n) }
        for (board in b) {
            if (board.wins()) {
                return board.score(n)
            }
        }
    }
    return 0
}

fun playToLoose(header: List<Int>, boards: List<Board>): Int {
    var b = boards
    for (n in header) {
        if (b.size > 1) {
            b = b.map { it.mark(n) }.filterNot { it.wins() }
        } else {
            b = b.map { it.mark(n) }
            if (b.first().wins()) {
                return b.first().score(n)
            }
        }
    }
    return 0
}

sealed interface Cell {
    val n: Int
}

data class Marked(override val n: Int) : Cell
data class Unmarked(override val n: Int) : Cell {
    fun mark(): Marked = Marked(n)
}

fun List<Cell>.wins(): Boolean = this.all { it is Marked }

data class Board(val cells: List<Cell>) {
    fun at(c: Int, r: Int) = cells[r * 5 + c]
    fun row(r: Int): List<Cell> = (0..4).map { at(it, r) }
    fun col(c: Int): List<Cell> = (0..4).map { at(c, it) }
    fun mark(n: Int): Board = Board(cells.map {
        when (it) {
            is Marked -> it
            is Unmarked -> if (it.n == n) {
                it.mark()
            } else {
                it
            }
        }
    })

    fun wins(): Boolean {
        for (row in (0..4).map { this.row(it) }) {
            if (row.wins()) {
                return true
            }
        }
        for (column in (0..4).map { this.col(it) }) {
            if (column.wins()) {
                return true
            }
        }
        return false
    }

    fun score(n: Int): Int = cells.fold(0) { tot, cell ->
        when (cell) {
            is Marked -> tot
            is Unmarked -> tot + cell.n
        }
    } * n
}

sealed interface BoardReaderState {
    val header: List<Int>;
    val boards: List<Board>
}

object Empty : BoardReaderState {
    override val header: List<Int> = listOf();
    override val boards: List<Board> = listOf()
}

data class Header(override val header: List<Int>) : BoardReaderState {
    override val boards: List<Board> = listOf()
}

data class Boards(override val header: List<Int>, override val boards: List<Board>) : BoardReaderState
data class PartialBoards(val lines: List<String>, override val header: List<Int>, override val boards: List<Board>) :
    BoardReaderState

object BoardReader {
    fun read(lines: Sequence<String>): BoardReaderState = lines.fold(Empty) { state: BoardReaderState, line: String ->
        when (state) {
            is Empty -> Header(line.split(",").map { it.toInt() })
            is Header -> if (line.isEmpty()) {
                state
            } else {
                PartialBoards(listOf(line), state.header, state.boards)
            }
            is Boards -> if (line.isEmpty()) {
                state
            } else {
                PartialBoards(listOf(line), state.header, state.boards)
            }
            is PartialBoards -> if (state.lines.size == 4) {
                val lx = state.lines + line
                val b = Board(lx.flatMap { it.split(" ").filterNot { it.isEmpty() }.map { n -> Unmarked(n.toInt()) } })
                Boards(state.header, state.boards + b)
            } else {
                PartialBoards(state.lines + line, state.header, state.boards)
            }
        }
    }
}