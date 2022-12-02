import Result.*
import Sign.*

fun main() {

    fun part1(input: List<String>): Int {
        return input.map {
            it.split(" ")
        }.map { it[0].asSign() to it[1].asSign() }
            .sumOf { score(it.first, it.second) }
    }

    fun part2(input: List<String>): Int {
        return input.map {
            it.split(" ")
        }.map { it[0].asSign() to it[1].asResult() }
            .sumOf { score(it.first, it.second) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}

private fun score(first: Sign, second: Sign): Int =
    when {
        first == second -> DRAW.points + second.points
        first.beats(second) -> LOSS.points + second.points
        else -> WIN.points + second.points
    }

private fun score(sign: Sign, result: Result) = result.matchingSign(sign).points + result.points

private val winMap = mapOf(
    ROCK to SCISSORS,
    PAPER to ROCK,
    SCISSORS to PAPER
)

private val lossMap = winMap.entries.associate { it.value to it.key }

enum class Sign(val points: Int) {
    ROCK(1), PAPER(2), SCISSORS(3);

    fun beats(other: Sign): Boolean {
        return winMap[this] == other
    }
}

enum class Result(val points: Int) {
    LOSS(0), WIN(6), DRAW(3);

    fun matchingSign(other: Sign): Sign {
        return when(this) {
            DRAW -> other
            LOSS -> winMap[other]!!
            WIN -> lossMap[other]!!
        }
    }
}

fun String.asSign(): Sign {
    return when (this) {
        "A", "X" -> ROCK
        "B", "Y" -> PAPER
        "C", "Z" -> SCISSORS
        else -> throw IllegalArgumentException()
    }
}

fun String.asResult(): Result {
    return when (this) {
        "X" -> LOSS
        "Y" -> DRAW
        "Z" -> WIN
        else -> throw IllegalArgumentException()
    }
}
