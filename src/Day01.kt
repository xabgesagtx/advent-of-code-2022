fun main() {

    fun readCalories(input: List<String>): List<Int> {
        return input.fold(mutableListOf(mutableListOf<Int>())) {
            acc, line ->
                if (line.isBlank()) {
                    acc.add(mutableListOf())
                } else {
                    acc.last().add(line.toInt())
                }
                acc
        }
            .filter { it.isNotEmpty() }
            .map { it.sum() }
    }

    fun part1(input: List<String>): Int {
        return readCalories(input).max()
    }

    fun part2(input: List<String>): Int {
        return readCalories(input)
            .sortedDescending()
            .take(3)
            .sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
