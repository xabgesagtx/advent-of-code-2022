fun main() {

    fun part1(input: List<String>): Int {
        return input.map { it.split(",") }
            .map { (first, second) -> first.toIntSet() to second.toIntSet() }
            .count { it.first.containsAll(it.second) || it.second.containsAll(it.first) }
    }

    fun part2(input: List<String>): Int {
        return input.map { it.split(",") }
            .map { (first, second) -> first.toIntSet() to second.toIntSet() }
            .count { (it.first intersect it.second).isNotEmpty() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}

private fun String.toIntSet(): Set<Int> {
    val (start, end) = this.split("-")
    return (start.toInt() .. end.toInt()).toSet()
}
