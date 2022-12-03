fun main() {

    fun part1(input: List<String>): Int {
        return input.map { it.splitInHalf() }
            .map { commonCharacters(it.first, it.second) }
            .flatten()
            .sumOf { it.priority }
    }

    fun part2(input: List<String>): Int {
        return input
            .chunked(3)
            .map { commonCharacters(*it.toTypedArray()) }
            .flatten()
            .sumOf { it.priority }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}

private fun String.splitInHalf() = substring(0, length/2) to substring(length/2)
private val Char.priority
    get() = if (isLowerCase()) this - 'a' + 1 else this - 'A' + 27
private fun commonCharacters(vararg texts: String): Set<Char> =
    texts.drop(1)
        .fold(texts.first().toSet()) {
                acc, text -> acc.intersect(text.toSet())
        }
