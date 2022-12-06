fun main() {

    fun checkForMarker(input: String, distinctCharacters: Int): Int {
        return input
            .windowed(distinctCharacters)
            .withIndex()
            .first { window -> window.value.toSet().size == distinctCharacters }
            .index + distinctCharacters
    }

    fun part1(input: String): Int {
        return checkForMarker(input, 4)
    }

    fun part2(input: String): Int {
        return checkForMarker(input, 14)
    }

    // test if implementation meets criteria from the description, like:
    check(part1("mjqjpqmgbljsphdztnvjfqwrcgsmlb") == 7)
    check(part1("bvwbjplbgvbhsrlpgdmjqwftvncz") == 5)
    check(part2("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg") == 29)

    val input = readInput("Day06").first()
    println(part1(input))
    println(part2(input))
}
