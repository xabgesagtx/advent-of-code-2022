import Operator.PLUS
import Operator.TIMES

fun main() {

    val day = "Day11"

    fun readMonkeys(input: List<String>): Map<Int, Monkey> {
        return input.filter { it.isNotBlank() }
            .chunked(6)
            .mapIndexed { index, lines ->  lines.toMonkey(index) }
            .associateBy { it.number }
    }

    fun Map<Int,Monkey>.simulateRound(worryLevelDivider: Long) {
        for(i in 0 until size) {
            val monkey = getValue(i)
            while (monkey.items.isNotEmpty()) {
                monkey.inspectedItems++
                var item = monkey.items.removeFirst()
                item = monkey.operation.perform(item)
                item /= worryLevelDivider
                if (item % monkey.testDivider == 0L) {
                    getValue(monkey.firstTarget).items.add(item)
                } else {
                    getValue(monkey.secondTarget).items.add(item)
                }
            }
        }
    }

    fun part1(input: List<String>): Long {
        val monkeys = readMonkeys(input)
        repeat(20) {
            monkeys.simulateRound(3L)
        }
        val (firstHighest, secondHighest) =  monkeys.values.map { it.inspectedItems }.sortedDescending().take(2)
        return firstHighest * secondHighest
    }

    fun part2(input: List<String>): Long {
        val monkeys = readMonkeys(input)
        repeat(10000) {
            monkeys.simulateRound(1L)
        }
        val (firstHighest, secondHighest) =  monkeys.values.map { it.inspectedItems }.sortedDescending().take(2)
        return firstHighest * secondHighest
    }
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("${day}_test")
    readMonkeys(testInput).values.forEach { println(it) }
    println("Test part 1:")
    println(part1(testInput))
    check(part1(testInput) == 10605L)
    println()
    println("Test part 2:")
    println(part2(testInput))
    check(part2(testInput) == 2713310158L)

    val input = readInput(day)
    println()
    println("Part 1")
    println(part1(input))
    println()
    println("Part 2")
    println(part2(input))
}

private fun List<String>.toMonkey(index: Int): Monkey {
    val (startingItemsLine, operationLine, testLine, trueCaseLine, falseCaseLine) = this.drop(1)
    val startingItems =
        startingItemsLine.substringAfter("Starting items:").split(",").mapNotNull { it.trim().toLongOrNull() }
    val (operatorString, operandString) = operationLine.substringAfter("Operation: new = old ").split(" ")
    val operator = when (operatorString) {
        "*" -> TIMES
        "+" -> PLUS
        else -> error("Unexpected operator: $operatorString")
    }
    val fixedOperand = operandString.toLongOrNull()
    val operation = Operation(operator, fixedOperand)
    val testDivider = testLine.substringAfter("Test: divisible by ").toLong()
    val firstTarget = trueCaseLine.substringAfter("If true: throw to monkey ").toInt()
    val secondTarget = falseCaseLine.substringAfter("If false: throw to monkey ").toInt()

    return Monkey(index, ArrayDeque(startingItems), operation, testDivider, firstTarget, secondTarget)
}

private enum class Operator {
    PLUS, TIMES
}

private data class Operation(val operator: Operator, val fixedOperand: Long?) {
    fun perform(oldValue: Long): Long {
        val operand = fixedOperand ?: oldValue
        return when (operator) {
            PLUS -> oldValue + operand
            TIMES -> oldValue * operand
        }
    }
}

private data class Monkey(
    val number: Int,
    val items: ArrayDeque<Long>,
    val operation: Operation,
    val testDivider: Long,
    val firstTarget: Int,
    val secondTarget: Int,
    var inspectedItems: Long = 0
)
