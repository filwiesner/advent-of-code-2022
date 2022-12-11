private const val Lcd = 9699690L

class Monkey(
    val name: String, // for debugging
    var items: List<Long>,
    private val inspectionTransformation: (item: Long) -> Long,
    private val getTargetIndex: (item: Long) -> Int
) {
    var inspectionCounter = 0L
        private set

    fun inspect(divideByThree: Boolean) {
        inspectionCounter += items.size.toLong()
        items = items.map { item ->
            val finalNumber = inspectionTransformation(item)
            if (divideByThree) finalNumber / 3L
            else finalNumber % Lcd
        }
    }

    fun throwItems(monkeys: List<Monkey>) {
        items.forEach { item -> monkeys[getTargetIndex(item)].receiveItem(item) }
        items = emptyList()
    }

    private fun receiveItem(item: Long) {
        items += item
    }

    override fun toString(): String = "$name with items: $items"
}

fun parseMonkey(
    name: String,
    lines: List<String>
): Monkey {
    val (itemsLine, operationLine, testLine, condition1Line, condition2Line) = lines.drop(1)

    val items = itemsLine.replace(",", "").split(" ").mapNotNull(String::toLongOrNull)
    val (firstNum, operator, secondNum) = operationLine.split(" ").takeLast(3)
    val divisibleBy = testLine.split(" ").last().toLong()
    val trueTarget = condition1Line.split(" ").last().toInt()
    val falseTarget = condition2Line.split(" ").last().toInt()

    return Monkey(
        name = name,
        items = items,
        inspectionTransformation = { item ->
            val first = if (firstNum == "old") item else firstNum.toLong()
            val second = if (secondNum == "old") item else secondNum.toLong()

            if (operator == "*") first * second else first + second
        },
        getTargetIndex = { item -> if ((item % divisibleBy) == 0L) trueTarget else falseTarget }
    )
}

fun parseInput(input: List<String>) = input
    .filter(String::isNotBlank)
    .chunked(6)
    .mapIndexed { index, lines -> parseMonkey("Monkey $index", lines) }

fun main() {
    fun part1(input: List<String>): Int {
        val monkeys = parseInput(input)

        repeat(20) {
//            println("=== Round $it ===")
//            monkeys.forEach(::println)
//            println()

            for (monkey in monkeys) {
                monkey.inspect(true)
                monkey.throwItems(monkeys)
            }
        }

        val (first, second) = monkeys.sortedByDescending(Monkey::inspectionCounter).take(2)
        return (first.inspectionCounter * second.inspectionCounter).toInt()
    }

    fun part2(input: List<String>): Long {
        val monkeys = parseInput(input)

        repeat(10_000) {
//            println("=== Round $it ===")
//            monkeys.forEach(::println)
//            println()

            for (monkey in monkeys) {
                monkey.inspect(false)
                monkey.throwItems(monkeys)
            }
        }

        val (first, second) = monkeys.sortedByDescending(Monkey::inspectionCounter).take(2)
        return first.inspectionCounter * second.inspectionCounter
    }

    val testInput = readInputLines("Day11_test")
    check(part1(testInput) == 10605)
//    check(part2(testInput) == 2713310158L)

    val input = readInputLines("Day11")
    println(part1(input))
    println(part2(input))
}