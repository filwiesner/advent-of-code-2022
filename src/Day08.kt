data class Tree(val x: Int, val y: Int, val height: Int)

fun makeForrest(input: List<String>): List<Tree> {
    val forrest = mutableListOf<Tree>()
    input.forEachIndexed { columnIndex, treeLine ->
        treeLine.forEachIndexed { rowIndex, height ->
            forrest.add(Tree(rowIndex, columnIndex, height.digitToInt()))
        }
    }
    return forrest
}

fun filterVisibleTreesInForest(forest: List<Tree>): Collection<Tree> {
    val rows = forest.rows()
    val columns = forest.columns()

    val treeLines = rows + rows.map(List<Tree>::asReversed) +
            columns + columns.map(List<Tree>::asReversed)

    return treeLines
        .flatMap(::filterVisibleTreesInTreeLine)
        .distinct()
}

fun filterVisibleTreesInTreeLine(line: List<Tree>): Collection<Tree> = buildList {
    var maxHeight = -1
    for (tree in line) {
        if (tree.height > maxHeight) {
            add(tree)
            maxHeight = tree.height
        }
    }
}

fun List<Tree>.rows(): List<List<Tree>> = buildList {
    val rowsMap = this@rows.groupBy { it.y }
    for (key in rowsMap.keys) add(key, rowsMap.getValue(key))
}

fun List<Tree>.columns(): List<List<Tree>> = buildList {
    val columnsMap = this@columns.groupBy { it.x }
    for (key in columnsMap.keys) add(key, columnsMap.getValue(key))
}

fun List<Tree>.filterVisibleTreesFromHeightCount(height: Int): Int {
    var count = 0
    for (tree in this) {
        count++
        if (tree.height >= height) break
    }
    return count
}

fun Tree.scenicScore(row: List<Tree>, column: List<Tree>): Int {
    val rowIndex = row.indexOf(this)
    val columnIndex = column.indexOf(this)

    val left = row.subList(0, rowIndex).asReversed().filterVisibleTreesFromHeightCount(height)
    val right = row.subList(rowIndex + 1, row.size).filterVisibleTreesFromHeightCount(height)
    val top = column.subList(0, columnIndex).asReversed().filterVisibleTreesFromHeightCount(height)
    val bottom = column.subList(columnIndex + 1, column.size).filterVisibleTreesFromHeightCount(height)

    return left * right * top * bottom
}


fun main() {
    fun part1(input: List<String>): Int {
        val forrest = makeForrest(input)
        val visibleTrees = filterVisibleTreesInForest(forrest)
        return visibleTrees.count()
    }

    fun part2(input: List<String>): Int {
        val forest = makeForrest(input)
        val columns = forest.columns()
        val rows = forest.rows()
        var maxScore = 0

        repeat(input.first().length) { x ->
            repeat(input.size) { y ->
                val tree = columns[x][y]
                val score = tree.scenicScore(rows[y], columns[x])
                if (score > maxScore) maxScore = score
            }
        }

        return maxScore
    }

    val testInput = readInputLines("Day08_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)

    val input = readInputLines("Day08")
    println(part1(input))
    println(part2(input))
}
