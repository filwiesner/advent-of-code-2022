fun String.indexOfNonRepeatingCharacters(sequenceLength: Int): Int {
    val text = this
    buildString {
        for (char in text) {
            append(char)

            val lastSequence = substring((length - sequenceLength).coerceAtLeast(0))
            if (lastSequence.toList().distinct().size == sequenceLength)
                return length
        }
    }

    return -1
}


fun main() {
    fun part1(input: String): Int = input.indexOfNonRepeatingCharacters(4)
    fun part2(input: String): Int = input.indexOfNonRepeatingCharacters(14)

    val testInput = readInput("Day06_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 19)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}
