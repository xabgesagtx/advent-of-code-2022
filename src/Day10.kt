fun main() {

    val day = "Day10"

    fun calcCycleValues(input: List<String>): List<Pair<Int, Int>> {
        var x = 1
        var cycle = 1
        val cycleValues = input.flatMap {
            if (it == "noop") {
                listOf(++cycle to x)
            } else {
                val firstCycleValue = ++cycle to x
                x += it.incValue()
                listOf(firstCycleValue, ++cycle to x)
            }
        }
        return listOf(1 to 1) + cycleValues
    }

    fun combinedSignalStrengthAt(input: List<String>, cycles: Set<Int>): Int {
        return calcCycleValues(input).filter { it.first in cycles }
            .sumOf { it.first * it.second }
    }

    fun part1(input: List<String>): Int {
        return combinedSignalStrengthAt(input, setOf(20, 60, 100, 140, 180, 220))
    }

    fun part2(input: List<String>): List<String> {
        return calcCycleValues(input)
            .dropLast(1)
            .chunked(40)
            .map { line ->
                line.map { it.second }
                    .mapIndexed { index, cycleValue -> calcSpriteAt(index, cycleValue) }
                    .joinToString("")
            }
    }

    println(
        calcCycleValues(
            listOf(
                "noop",
                "addx 3",
                "addx -5"
            )
        )
    )
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("${day}_test")
    check(part1(testInput) == 13140)
    part2(testInput).forEach { println(it) }
    check(
        part2(testInput) == listOf(
            "##..##..##..##..##..##..##..##..##..##..",
            "###...###...###...###...###...###...###.",
            "####....####....####....####....####....",
            "#####.....#####.....#####.....#####.....",
            "######......######......######......####",
            "#######.......#######.......#######....."
        )
    )

    val input = readInput(day)
    println(part1(input))
    println()
    println()
    println("RESULT:")
    println()
    part2(input).forEach { println(it) }
}

private fun calcSpriteAt(pos: Int, cycleValue: Int): Char {
    return spriteFor(cycleValue)[pos]
}

private fun spriteFor(cycleValue: Int): List<Char> {
    val spritePos = (cycleValue .. cycleValue + 2).toSet()
    return (1..40).map { if (it in spritePos) '#' else '.' }
}

private fun String.incValue() = split(" ").firstNotNullOf { it.toIntOrNull() }