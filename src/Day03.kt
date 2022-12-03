fun main() {

    fun part1(input: List<String>): Int {
        return input.map { it.splitInHalf() }
            .map { it.first.toSet().intersect(it.second.toSet()) }
            .flatten()
            .sumOf { prioritiesMap[it]!! }
    }

    fun part2(input: List<String>): Int {
        return input
            .chunked(3)
            .map { it[0].toSet().intersect(it[1].toSet()).intersect(it[2].toSet()) }
            .flatten()
            .sumOf { prioritiesMap[it]!! }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}

private val prioritiesMap = ('a'..'z').zip(1..26).toMap() + ('A'..'Z').zip(27 .. 52).toMap()
private fun String.splitInHalf() = substring(0, length/2) to substring(length/2)
