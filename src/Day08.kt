fun main() {

    fun part1(input: List<String>): Int {
        val treeValues = input.map { line -> line.toList().map { it.digitToInt() } }

        val lastXIndex = treeValues[0].lastIndex
        val lastYIndex = treeValues.lastIndex

        return treeValues.indices.flatMap { y ->
            treeValues[y].indices.mapNotNull { x ->
                val currentValue = treeValues[y][x]
                when {
                    x == 0 || y == 0 || x == lastXIndex || y == lastYIndex -> x to y
                    treeValues.maxFromLeft(x, y) < currentValue -> x to y
                    treeValues.maxFromRight(x, y) < currentValue -> x to y
                    treeValues.maxFromTop(x, y) < currentValue -> x to y
                    treeValues.maxFromBottom(x, y) < currentValue -> x to y
                    else -> null
                }
            }
        }.distinct().count()
    }

    fun part2(input: List<String>): Int {
        val treeValues = input.map { line -> line.toList().map { it.digitToInt() } }
        return treeValues.mapIndexed { y, line ->
            List(line.size) { x -> treeValues.scenicScore(x, y) }.max()
        }.max()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    println(part1(testInput))
    check(part1(testInput) == 21)
    println(part2(testInput))
    check(part2(testInput) == 8)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}

private fun List<List<Int>>.maxFromLeft(x: Int, y: Int) = this[y].subList(0, x).max()
private fun List<List<Int>>.maxFromRight(x: Int, y: Int) = this[y].subList(x + 1, this[y].lastIndex + 1).max()
private fun List<List<Int>>.maxFromTop(x: Int, y: Int): Int {
    val list = this.map { it[x] }
    return list.subList(0, y).max()
}

private fun List<List<Int>>.maxFromBottom(x: Int, y: Int): Int {
    val list = this.map { it[x] }
    return list.subList(y + 1, lastIndex + 1).max()
}

private fun List<List<Int>>.scenicScore(x: Int, y: Int): Int {
    return if (x == 0 || y == 0 || y == this.lastIndex || x == this[y].lastIndex) {
        0
    } else {
        scenicScoreLeft(x, y) * scenicScoreRight(x, y) * scenicScoreTop(x, y) * scenicScoreBottom(x, y)
    }
}

private fun List<List<Int>>.scenicScoreBottom(x: Int, y: Int): Int {
    val currentValue = this[y][x]
    val list = this.map { it[x] }.subList(y + 1, lastIndex + 1)
    return (list.takeWhile { it < currentValue }.count() + 1).coerceAtMost(list.size)
}

private fun List<List<Int>>.scenicScoreTop(x: Int, y: Int): Int {
    val currentValue = this[y][x]
    val list = this.map { it[x] }.subList(0, y).reversed()
    return (list.takeWhile { it < currentValue }.count() + 1).coerceAtMost(list.size)
}

private fun List<List<Int>>.scenicScoreLeft(x: Int, y: Int): Int {
    val currentValue = this[y][x]
    val list = this[y].subList(0, x).reversed()
    return (list.takeWhile { it < currentValue }.count() + 1).coerceAtMost(list.size)
}

private fun List<List<Int>>.scenicScoreRight(x: Int, y: Int): Int {
    val currentValue = this[y][x]
    val list = this[y].subList(x + 1, this[y].lastIndex + 1)
    return (list.takeWhile { it < currentValue }.count() + 1).coerceAtMost(list.size)
}