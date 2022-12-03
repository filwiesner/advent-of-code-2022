fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = readInput("Day00_test")
    check(part1(testInput) == 0)
//    check(part2(testInput) == 0)

    val input = readInput("Day00")
    println(part1(input))
//    println(part2(input))
}
