object Day10 {
    const val xKey = "x"

    sealed interface Instruction {
        fun Processor.onTick(): Boolean

        class AddX(private val number: Int) : Instruction {
            private var initialTick = true

            override fun Processor.onTick(): Boolean =
                if (initialTick) {
                    initialTick = false
                    false
                } else {
                    val currentValue = register.getValue(xKey)
                    register[xKey] = currentValue + number
                    true
                }

        }

        object NoOp : Instruction {
            override fun Processor.onTick(): Boolean = true
        }
    }

    class Processor {
        val register = mutableMapOf("x" to 1)
        private val scheduledInstructions = ArrayDeque<Instruction>()

        fun scheduleInstructions(instructions: List<Instruction>) {
            for (ins in instructions) scheduledInstructions.addLast(ins)
        }

        fun tick() {
            if (scheduledInstructions.isEmpty()) return
            with(scheduledInstructions.first()) {
                if (onTick()) scheduledInstructions.removeFirst()
            }
        }
    }

    fun parseInstructions(input: List<String>) = input.map { line ->
        val words = line.split(" ")
        when (words[0]) {
            "noop" -> Instruction.NoOp
            else -> Instruction.AddX(words[1].toInt())
        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val cpu = Day10.Processor()
        cpu.scheduleInstructions(Day10.parseInstructions(input))

        val cycleMarks = listOf(20, 60, 100, 140, 180, 220)
        var strength = 0
        for (cycle in 1..220) {
            if (cycle in cycleMarks) {
                val registerValue = cpu.register.getValue(Day10.xKey)
                strength += cycle * registerValue
            }
            cpu.tick()
        }

        return strength
    }

    fun part2(input: List<String>): String {
        val cpu = Day10.Processor()
        cpu.scheduleInstructions(Day10.parseInstructions(input))

        return buildString {
            for (cycle in 0..239) {
                val registerValue = cpu.register.getValue(Day10.xKey)
                val spritePosition = cycle % 40
                val spriteRange = spritePosition - 1..spritePosition + 1

                if (spritePosition == 0 && cycle > 1) appendLine()
                append(if (registerValue in spriteRange) "#" else ".")

                cpu.tick()
            }
        }
    }

    val testInput = readInputLines("Day10_test")
    check(part1(testInput) == 13140)
    check(
        part2(testInput) == """
        ##..##..##..##..##..##..##..##..##..##..
        ###...###...###...###...###...###...###.
        ####....####....####....####....####....
        #####.....#####.....#####.....#####.....
        ######......######......######......####
        #######.......#######.......#######.....
    """.trimIndent()
    )

    val input = readInputLines("Day10")
    println(part1(input))
    println(part2(input))
}
