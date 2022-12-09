import kotlin.math.absoluteValue

fun main() {

    val day = "Day09"

    fun part1(input: List<String>): Int {
        val locations = mutableSetOf(0 to 0)
        var head = 0 to 0
        var tail = 0 to 0
        for (command in input) {
            head.move(command).forEach { newHeadPosition ->
                head = newHeadPosition
                tail = tail.follow(head)
                locations.add(tail)
            }
        }
        return locations.size
    }

    fun part2(input: List<String>): Int {
        val locations = mutableSetOf(0 to 0)
        var head = 0 to 0
        var tails = List(9) { 0 to 0 }
        for (command in input) {
            head.move(command).forEach { newHeadPosition ->
                head = newHeadPosition
                var toFollow = head
                tails = tails.map { tail ->
                    val newTailPos = tail.follow(toFollow)
                    toFollow = newTailPos
                    newTailPos
                }
                locations.add(tails.last())
            }
        }
        return locations.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("${day}_test")
    println(part1(testInput))
    check(part1(testInput) == 13)
    val testInput2 = readInput("${day}_test2")
    println(part2(testInput2))
    check(part2(testInput2) == 36)

    val input = readInput(day)
    println(part1(input))
    println(part2(input))
}

private fun Pair<Int, Int>.move(command: String): List<Pair<Int, Int>> {
    val (direction, steps) = command.split(" ")
    return when (direction) {
        "R" -> List(steps.toInt()) { first + it + 1 to second }
        "L" -> List(steps.toInt()) { first - it - 1 to second }
        "U" -> List(steps.toInt()) { first to second + it + 1 }
        "D" -> List(steps.toInt()) { first to second - it - 1 }
        else -> error("Unexpected command: $command")
    }
}

private fun Pair<Int, Int>.follow(head: Pair<Int, Int>): Pair<Int, Int> {
    val incFirst = if (first > head.first) -1 else 1
    val incSecond = if (second > head.second) -1 else 1
    return when {
        isTouching(head) -> this
        isInSameRow(head) -> first + incFirst to second
        isInSameColumn(head) -> first to second + incSecond
        else -> first + incFirst to second + incSecond
    }
}

private fun Pair<Int, Int>.isTouching(other: Pair<Int, Int>): Boolean {
    val xDiff = (this.first - other.first).absoluteValue
    val yDiff = (this.second - other.second).absoluteValue
    return xDiff < 2 && yDiff < 2
}

private fun Pair<Int, Int>.isInSameRow(other: Pair<Int, Int>): Boolean {
    return second == other.second
}

private fun Pair<Int, Int>.isInSameColumn(other: Pair<Int, Int>): Boolean {
    return first == other.first
}