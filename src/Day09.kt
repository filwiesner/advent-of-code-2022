import kotlin.math.absoluteValue

val String.asDirection get() = Direction.values().first { it.name.first().toString() == this }

class Knot(
    startOffset: IntOffset,
    private val childKnot: Knot? = null
) {
    private var location: IntOffset = startOffset
    private val visitedPositions = mutableListOf(location)

    val visited get() = visitedPositions.toList().distinct()
    val ropeTail: Knot get() = childKnot?.ropeTail ?: this

    fun parentUpdated(parentLocation: IntOffset) {
        if (location.distanceTo(parentLocation) <= 1) return // ignore changes where parent is still touching

        val offset = parentLocation - location
        val targetOffset = when {
            offset.x == 0 -> {
                IntOffset(0, offset.y / offset.y.absoluteValue)
            }

            offset.y == 0 -> {
                IntOffset(offset.x / offset.x.absoluteValue, 0)
            }

            else -> {
                val horizontalDirection = if (offset.x > 0) Direction.Right else Direction.Left
                val verticalDirection = if (offset.y > 0) Direction.Up else Direction.Down
                horizontalDirection.offset + verticalDirection.offset
            }
        }

        location += targetOffset
        visitedPositions.add(location)
        childKnot?.parentUpdated(location)
    }
}

fun parseInstructions(input: List<String>): List<Direction> = input
    .flatMap { line ->
        val (dir, count) = line.split(" ")
        val direction = dir.asDirection
        List(count.toInt()) { direction }
    }

fun buildRope(length: Int, initialOffset: IntOffset = IntOffset(0, 0)): Knot {
    var lastKnot = Knot(initialOffset, null)
    repeat(length - 1) { // -1 because we already have the last knot
        val newKnot = Knot(initialOffset, lastKnot)
        lastKnot = newKnot
    }
    return lastKnot
}

fun main() {
    fun part1(input: List<String>): Int {
        val instructions = parseInstructions(input)
        var headLocation = IntOffset(0, 0)
        val tail = Knot(headLocation)

        for (instruction in instructions) {
            headLocation += instruction.offset
            tail.parentUpdated(headLocation)
        }

        return tail.visited.size
    }

    fun part2(input: List<String>): Int {
        val instructions = parseInstructions(input)
        var headLocation = IntOffset(0, 0)
        val firsKnot = buildRope(9)

        for (instruction in instructions) {
            headLocation += instruction.offset
            firsKnot.parentUpdated(headLocation)
        }

        return firsKnot.ropeTail.visited.size
    }

    val testInput = readInputLines("Day09_test")
    val test2Input = readInputLines("Day09_test2")
    check(part1(testInput) == 13)
    check(part2(test2Input) == 36)

    val input = readInputLines("Day09")
    println(part1(input))
    println(part2(input))
}
