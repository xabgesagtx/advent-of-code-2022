fun main() {

    fun part1(input: List<String>): String {
        val stacks = getInitialStackValues(input)
        getMoveLines(input).forEach { line ->
            stacks.doMove(line)
        }
        println(stacks)
        return stacks.entries.sortedBy { it.key }.map { it.value.last() }.joinToString("")
    }

    fun part2(input: List<String>): String {
        val stacks = getInitialStackValues(input)
        getMoveLines(input).forEach { line ->
            stacks.doMove9001(line)
        }
        println(stacks)
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


private fun getNumberOfStacks(input: List<String>): Int {
    val stackNumberLine = input.first { line -> line.trim().startsWith("1") }
    return stackNumberLine.trim().split(" ").filter { it.isNotBlank() }.map { it.toInt() }.max()
}

private fun getStackValue(line: String, stack: Int): Char? {
    val n = stack - 1
    return line[n* 4 + 1].takeIf { it.isLetter() }
}

private fun getInitialStackLines(input: List<String>): List<String> {
    return input.takeWhile { line -> !line.trim().startsWith("1")  }.reversed()
}

private fun getInitialStackValues(input: List<String>): Map<Int, MutableList<Char>> {
    val numberOfStacks = getNumberOfStacks(input)
    val stackLines = getInitialStackLines(input)
    return (1..numberOfStacks).fold(mutableMapOf()) {
        stacks, currentStack ->
            val initalStack = ArrayList<Char>()
            stackLines.forEach {
                line -> getStackValue(line, currentStack)?.let { initalStack.add(it) }
            }
            stacks[currentStack] = initalStack
            stacks
    }
}

private fun getMoveLines(input: List<String>): List<String> {
    return input.filter { it.startsWith("move") }
}

private fun Map<Int, MutableList<Char>>.doMove(command: String) {
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