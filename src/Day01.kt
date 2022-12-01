fun main() {

    fun readElves(input: List<String>): MutableMap<Int, Int> {
        var currentElf = 1
        val elves = mutableMapOf<Int, Int>()
        input.forEach {
            if (it.isBlank()) {
                currentElf++
            } else {
                elves.merge(currentElf, it.toInt()) { first, second -> first + second }
            }
        }
        return elves
    }

    fun part1(input: List<String>): Int {
        val elves = readElves(input)
        return elves.map { it.value }.max()
    }

    fun part2(input: List<String>): Int {
        return readElves(input)
            .map { it.value }
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
