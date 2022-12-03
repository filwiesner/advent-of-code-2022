val smallLetters = buildList {
    var character = 'a'.also(::add)
    while (character != 'z') add(++character)
}
val allLetters = smallLetters + smallLetters.map(Char::uppercaseChar)
fun main() {
    fun part1(input: List<String>): Int {
        var sum = 0
        for (line in input) {
            val firstCompartment = line.subSequence(0, line.length / 2).asIterable()
            val secondCompartment = line.subSequence(line.length / 2, line.length).asIterable()
            val sharedItem = (firstCompartment intersect secondCompartment.toSet()).first()

            sum += allLetters.indexOf(sharedItem) + 1 // +1 because priorities are indexed from 1
        }

        return sum
    }

    fun part2(input: List<String>): Int {
        var sum = 0
        for (group in input.chunked(3)) {
            val (first, second, third) = group.map(String::asIterable).map(Iterable<Char>::toSet)
            val sharedItem = (first intersect second intersect third).first()
            sum += allLetters.indexOf(sharedItem) + 1
        }

        return sum
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
