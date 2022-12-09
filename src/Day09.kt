import kotlin.math.absoluteValue

fun main() {

    val day = "Day09"

    fun simulateRopeMovement(input: List<String>, numberOfTailElements: Int): Int {
        return buildSet {
            add(0 to 0)
            var rope = List(numberOfTailElements + 1) { 0 to 0 }
            for (command in input) {
                rope.first().move(command).forEach { newHeadPosition ->
                    rope = rope.drop(1).fold(listOf(newHeadPosition)) { updatedRope, tailElement ->
                        updatedRope + tailElement.follow(updatedRope.last())
                    }
                    add(rope.last())
                }
            }
        }.size
    }

    fun part1(input: List<String>): Int {
        return simulateRopeMovement(input, 1)
    }

    fun part2(input: List<String>): Int {
        return simulateRopeMovement(input, 9)
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
    val incFirst = (head.first - first).coerceIn(-1..1)
    val incSecond = (head.second - second).coerceIn(-1..1)
    incFirst.coerceIn(-1..1)
    return when {
        isTouching(head) -> this
        else -> first + incFirst to second + incSecond
    }
}

private fun Pair<Int, Int>.isTouching(other: Pair<Int, Int>): Boolean {
    val xDiff = (this.first - other.first).absoluteValue
    val yDiff = (this.second - other.second).absoluteValue
    return xDiff < 2 && yDiff < 2
}
