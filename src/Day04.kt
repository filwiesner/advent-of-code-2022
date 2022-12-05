fun String.parseAssignmentRanges() = split(",")
    .map { section ->
        val (startNumber, endNumber) = section.split("-")
        (startNumber.toInt()..endNumber.toInt())
    }.let { (first, second) -> first to second }

fun main() {
    fun part1(input: List<String>): Int {
        var count = 0

        for (line in input) {
            val (first, second) = line.parseAssignmentRanges()
            if (first in second || second in first) count++
        }

        return count
    }

    fun part2(input: List<String>): Int {
        var count = 0

        for (line in input) {
            val (first, second) = line.parseAssignmentRanges()
            if ((first.toSet() intersect second.toSet()).isNotEmpty()) count++
        }

        return count
    }

    val testInput = readInputLines("Day04_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInputLines("Day04")
    println(part1(input))
    println(part2(input))
}

