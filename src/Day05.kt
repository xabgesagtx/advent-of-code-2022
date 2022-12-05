fun main() {

    fun part1(input: List<String>): String {
        val stacks = input.initialStackValues
        input.moveLines.forEach { line ->
            stacks.doMove9000(line)
        }
        return stacks.entries.sortedBy { it.key }.map { it.value.last() }.joinToString("")
    }

    fun part2(input: List<String>): String {
        val stacks = input.initialStackValues
        input.moveLines.forEach { line ->
            stacks.doMove9001(line)
        }
        return stacks.entries.sortedBy { it.key }.map { it.value.last() }.joinToString("")
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}

private val List<String>.numberOfStacks: Int
    get() {
        val stackNumberLine = first { line -> line.trim().startsWith("1") }
        return stackNumberLine.trim().split(" ").filter { it.isNotBlank() }.map { it.toInt() }.max()
    }

private fun String.valueForStack(stack: Int): Char? {
    val n = stack - 1
    return this[n * 4 + 1].takeIf { it.isLetter() }
}

private val List<String>.initialStackLines: List<String>
    get() = takeWhile { line -> !line.trim().startsWith("1") }.reversed()

private val List<String>.initialStackValues: Map<Int, MutableList<Char>>
    get() {
        val numberOfStacks = numberOfStacks
        val stackLines = initialStackLines
        return (1..numberOfStacks).associateWith { currentStack ->
            stackLines.mapNotNull { line ->
                line.valueForStack(currentStack)
            }.toMutableList()
        }
    }

private val List<String>.moveLines: List<String>
    get() {
        return filter { it.startsWith("move") }
    }

private fun Map<Int, MutableList<Char>>.doMove9000(command: String) {
    val (numberOfItems, from, to) = command.split(" ").mapNotNull { it.toIntOrNull() }
    repeat(numberOfItems) { this[to]!!.add(this[from]!!.removeLast()) }
}

private fun Map<Int, MutableList<Char>>.doMove9001(command: String) {
    val (numberOfItems, from, to) = command.split(" ").mapNotNull { it.toIntOrNull() }
    val items = (0 until numberOfItems).map {
        this[from]!!.removeLast()
    }.reversed()
    this[to]!!.addAll(items)
}