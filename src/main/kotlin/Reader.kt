import java.io.BufferedReader

object Reader {

    private fun buffered(filename: String): BufferedReader =
        this::class.java.getResourceAsStream(filename)!!.bufferedReader()

    fun ints(filename: String): List<Int> = buffered(filename).useLines {
        it.map { it.toInt() }.toList()
    }

}