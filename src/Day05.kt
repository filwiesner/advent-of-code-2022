data class Instruction(
    val quantity: Int,
    val sourceStackId: Int,
    val targetStackId: Int
)

fun String.asInstruction(): Instruction {
    val (quantity, source, target) = split(" ")
        .filter { text -> text.all(Char::isDigit) }
        .map(String::toInt)
    return Instruction(quantity, source, target)
}

class CrateStack(
    columnInput: String
) {
    private val crates = ArrayDeque<Char>()
        .apply { columnInput.trim().forEach(::add) }

    fun pop(): Char = crates.removeFirst()
    fun push(char: Char) = crates.addFirst(char)
}

fun initStacks(input: String): Map<Int, CrateStack> {
    val sanitizedColumns = input.columns()
        .filter { column ->
            // remove blank lines if '[' and ']' are considered blank characters
            column.replace(Regex("[\\[\\]]"), " ").isNotBlank()
        }

    return buildMap {
        sanitizedColumns.forEachIndexed { index, column ->
            val id = index + 1
            val stackValues = column.dropLast(1) // drop last character (stack ID)
            set(id, CrateStack(stackValues))
        }
    }
}

fun Map<Int, CrateStack>.applyCrateMover9000Instruction(instruction: Instruction) {
    val source = get(instruction.sourceStackId) ?: error("Stack with id ${instruction.sourceStackId} does not exist")
    val target = get(instruction.targetStackId) ?: error("Stack with id ${instruction.targetStackId} does not exist")
    repeat(instruction.quantity) {
        target.push(source.pop())
    }
}

fun Map<Int, CrateStack>.applyCrateMover9001Instruction(instruction: Instruction) {
    val source = get(instruction.sourceStackId) ?: error("Stack with id ${instruction.sourceStackId} does not exist")
    val target = get(instruction.targetStackId) ?: error("Stack with id ${instruction.targetStackId} does not exist")

    val crates = buildString {
        repeat(instruction.quantity) {
            append(source.pop())
        }
    }.reversed()
    crates.forEach(target::push)
}

fun main() {
    fun prepare(input: String): Pair<Map<Int, CrateStack>, List<Instruction>> {
        val (initialStateInput, instructionsInput) = input.lines()
            .let { lines ->
                val separatorIndex = lines.indexOfFirst(String::isBlank)
                lines.subList(0, separatorIndex) to lines.subList(separatorIndex + 1, lines.size)
            }

        val stacks = initStacks(initialStateInput.joinToString("\n"))
        val instructions = instructionsInput.map(String::asInstruction)

        return stacks to instructions
    }

    fun part1(input: String): String {
        val (stacks, instructions) = prepare(input)
        instructions.forEach(stacks::applyCrateMover9000Instruction)

        return buildString {
            for (stack in stacks.values) append(stack.pop())
        }
    }

    fun part2(input: String): String {
        val (stacks, instructions) = prepare(input)
        instructions.forEach(stacks::applyCrateMover9001Instruction)

        return buildString {
            for (stack in stacks.values) append(stack.pop())
        }
    }

    val testInput = readInput("Day05_test")
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
