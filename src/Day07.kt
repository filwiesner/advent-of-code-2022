sealed interface SystemItem {
    val name: String
    val size: Int
}

data class Directory(
    override val name: String,
    val parent: Directory?,
) : SystemItem {
    override val size: Int get() = contents.sumOf(SystemItem::size)
    val contents: MutableList<SystemItem> = mutableListOf()

    val nestedContents: List<SystemItem>
        get() = contents.flatMap { item ->
            when (item) {
                is Directory -> listOf(item) + item.nestedContents
                is File -> listOf(item)
            }
        }
}

data class File(
    override val name: String,
    override val size: Int
) : SystemItem


fun buildFileStructure(input: List<String>): Directory {
    val root = Directory("/", null)
    var currentDirectory = root
    val currentContentList = mutableListOf<String>()

    fun handleEndOfContent() {
        if (currentContentList.isNotEmpty()) {
            currentDirectory.fillWithItems(currentContentList)
            currentContentList.clear()
        }
    }

    for (line in input) {
        if (!line.startsWith("$")) {
            currentContentList.add(line)
            continue
        }

        // Is command
        handleEndOfContent()

        if (line == "$ ls") continue // ignore ls commnands

        val dirName = line.split(" ")[2]
        currentDirectory = if (dirName == "..") {
            currentDirectory.parent ?: error("Attempt to go above root dir")
        } else {
            currentDirectory.contents
                .filterIsInstance<Directory>()
                .first { it.name == dirName }
        }
    }

    handleEndOfContent()
    return root
}

fun Directory.fillWithItems(items: List<String>) {
    for (item in items) {
        if (item.startsWith("dir")) {
            val (_, dirName) = item.split(" ")
            contents.add(Directory(dirName, this))
        } else {
            val (size, fileName) = item.split(" ")
            contents.add(File(fileName, size.toInt()))
        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val root = buildFileStructure(input.drop(1))
        val relevantDirectories = root.nestedContents
            .filterIsInstance<Directory>()
            .filter { it.size <= 100000 }

        return relevantDirectories.sumOf { it.size }
    }

    fun part2(input: List<String>): Int {
        val root = buildFileStructure(input.drop(1))
        val freeSpace = 70_000_000 - root.size
        val spaceNeeded = 30_000_000 - freeSpace

        return root.nestedContents.filterIsInstance<Directory>()
            .filter { it.size >= spaceNeeded }
            .minByOrNull { it.size }
            ?.size ?: 0
    }

    val testInput = readInputLines("Day07_test")
    check(part1(testInput) == 95437)
    check(part2(testInput) == 24933642)

    val input = readInputLines("Day07")
    println(part1(input))
    println(part2(input))
}
