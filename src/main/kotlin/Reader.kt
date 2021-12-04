import java.io.BufferedReader

object Reader {

    private fun buffered(filename: String): BufferedReader =
        this::class.java.getResourceAsStream(filename)!!.bufferedReader()

    fun ints(filename: String): List<Int> = ints(filename, 10)

    fun ints(filename: String, radix: Int) = buffered(filename).useLines {
        it.map { it.toInt(radix) }.toList()
    }

    fun strings(filename: String): List<String> = buffered(filename).readLines()
    fun stringSeq(filename: String): Sequence<String> = buffered(filename).lineSequence()

}