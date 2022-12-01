fun getElfCalories(input: List<String>) = buildList {
    var sum = 0
    for (line in input) {
        if (line.isBlank()) {
            add(sum)
            sum = 0
        } else sum += line.toInt()
    }

    add(sum)
}

fun main() {
    fun part1(input: List<String>): Int =
        getElfCalories(input).max()

    fun part2(input: List<String>): Int =
        getElfCalories(input)
            .sortedDescending()
            .take(3)
            .sum()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
