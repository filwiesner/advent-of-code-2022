import MatchResult.*

enum class HandSign(
    val opponentValue: String,
    val myValue: String,
) {
    Rock("A", "X"),
    Paper("B", "Y"),
    Scissors("C", "Z");

    val score = ordinal + 1

    val weakness: HandSign
        get() = when (this) {
            Rock -> Paper
            Paper -> Scissors
            Scissors -> Rock
        }

    companion object {
        private val values = buildMap {
            for (sign in values()) {
                put(sign.opponentValue, sign)
                put(sign.myValue, sign)
            }
        }

        fun parse(value: String): HandSign = values[value] ?: error("Unknown value $value")
    }
}

enum class MatchResult(val score: Int, val value: String) {
    Victory(6, "Z"),
    Draw(3, "Y"),
    Lost(0, "X");

    companion object {
        private val values = buildMap {
            for (result in values()) {
                put(result.value, result)
            }
        }

        fun parse(value: String): MatchResult = values[value] ?: error("Unknown value $value")
    }
}

infix fun HandSign.versus(opponentSign: HandSign): Int {
    val result = when (opponentSign) {
        this -> Draw
        weakness -> Lost
        else -> Victory
    }

    return result.score + this.score
}

fun main() {
    fun part1(input: List<String>): Int {
        var score = 0
        for (line in input) {
            val (opponentValue, myValue) = line.split(" ")
            score += HandSign.parse(myValue) versus HandSign.parse(opponentValue)
        }

        return score
    }

    fun part2(input: List<String>): Int {
        var score = 0
        for (line in input) {
            val (opponentValue, resultValue) = line.split(" ")
            val result = MatchResult.parse(resultValue)
            val opponentSign = HandSign.parse(opponentValue)
            val mySign = when (result) {
                Victory -> opponentSign.weakness
                Draw -> opponentSign
                Lost -> opponentSign.weakness.weakness // get weakness of opponents weakness to complete the circle
            }

            score += mySign versus opponentSign
        }

        return score
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInputLines("Day02_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInputLines("Day02")
    println(part1(input))
    println(part2(input))
}
