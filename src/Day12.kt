fun List<IntOffset>.getNextSteps(
    heightMap: Map<IntOffset, Char>,
    wayUp: Boolean
): List<List<IntOffset>> {
    val route = this
    val currentPosition = route.last()
    val currentHeight = heightMap[currentPosition]!!
    val possibleDirections = Direction.values()
        .map { currentPosition + it }
        .filterNot(route::contains)
        .filter(heightMap.keys::contains)

    val nextSteps = buildList {
        for (point in possibleDirections) {
            val pointHeight = heightMap[point]!!
            if (wayUp && pointHeight - 1 > currentHeight) continue
            if (!wayUp && pointHeight < currentHeight - 1) continue

            add(route + point)
        }
    }

    return nextSteps
}


private fun parseHeightMap(input: List<String>): Map<IntOffset, Char> = buildMap {
    input.forEachIndexed { y, row ->
        row.forEachIndexed { x, heightMark ->
            this[IntOffset(x, y)] = heightMark
        }
    }
}

private fun findShortestPaths(
    map: Map<IntOffset, Char>,
    wayUp: Boolean,
    startPosition: IntOffset,
    isEndPosition: (IntOffset) -> Boolean,
): Map<IntOffset, List<IntOffset>> {
    val bestPathMap = mutableMapOf(startPosition to listOf(startPosition))
    var changed: Int

    do {
        changed = 0
        for (point in bestPathMap.keys.toList()) {
            if (isEndPosition(point))
                continue

            val nextSteps = bestPathMap[point]?.getNextSteps(map, wayUp)
            if (nextSteps.isNullOrEmpty()) continue

            nextSteps.forEach { path ->
                val bestPathSoFar = bestPathMap[path.last()]
                fun savePath() {
                    bestPathMap[path.last()] = path
                    changed++
                }

                if (bestPathSoFar == null) savePath()
                else if (path.size < bestPathSoFar.size) savePath()
            }
        }
    } while (changed > 0)

    return bestPathMap
}

fun main() {
    fun part1(input: List<String>): Int {
        val map = parseHeightMap(input)
        val startPosition = map.entries.first { it.value == 'S' }.key
        val endPosition = map.entries.first { it.value == 'E' }.key
        val normalizedMap = map.mapValues {
            when (it.value) {
                'S' -> 'a'
                'E' -> 'z'
                else -> it.value
            }
        }

        val bestPathMap = findShortestPaths(
            map = normalizedMap,
            wayUp = true,
            startPosition = startPosition,
            isEndPosition = { it == endPosition },
        )
        val shortestPath = bestPathMap[endPosition] ?: error("No path to end found")
        return shortestPath.size - 1 // don't count first point
    }

    fun part2(input: List<String>): Int {
        val map = parseHeightMap(input)
        val startPosition = map.entries.first { it.value == 'E' }.key
        val normalizedMap = map.mapValues {
            when (it.value) {
                'S' -> 'a'
                'E' -> 'z'
                else -> it.value
            }
        }

        val bestPathMap = findShortestPaths(
            map = normalizedMap,
            wayUp = false,
            startPosition = startPosition,
            isEndPosition = { normalizedMap[it] == 'a' },
        )
        val shortestPath = bestPathMap.values
            .filter { normalizedMap[it.last()] == 'a' }
            .minBy(List<IntOffset>::size)
        return shortestPath.size - 1 // don't count first point
    }

    val testInput = readInputLines("Day12_test")
    check(part1(testInput) == 31)
    check(part2(testInput) == 29)

    val input = readInputLines("Day12")
    println(part1(input))
    println(part2(input))
}
