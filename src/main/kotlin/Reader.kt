object Reader {

    private fun lines(filename: String): Sequence<String> =
        this::class.java.getResourceAsStream(filename)!!.bufferedReader().lineSequence()

    fun ints(filename: String): List<Int> = lines(filename).map { it.toInt() }.toList()
}