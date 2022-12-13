import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.math.hypot

/**
 * Reads lines from the given input txt file.
 */
fun readInputLines(name: String) = File("src", "$name.txt")
    .readLines()

fun readInput(name: String) = File("src", "$name.txt")
    .readText()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

operator fun IntRange.contains(other: IntRange): Boolean = other.first in this && other.last in this

fun String.columns(): Sequence<String> = sequence {
    val rows = lines()
    repeat(rows.maxOf(String::length)) { index ->
        val column = buildString {
            for (row in rows) {
                val letter = row.getOrNull(index) ?: continue
                if (!letter.isWhitespace()) append(letter)
            }
        }

        yield(column)
    }
}

enum class Direction(val offset: IntOffset) {
    Up(IntOffset(0, 1)),
    Right(IntOffset(1, 0)),
    Down(IntOffset(0, -1)),
    Left(IntOffset(-1, 0)),
}

data class IntOffset(val x: Int, val y: Int)

operator fun IntOffset.plus(offset: IntOffset) = IntOffset(x + offset.x, y + offset.y)
operator fun IntOffset.minus(offset: IntOffset) = IntOffset(x - offset.x, y - offset.y)
fun IntOffset.distanceTo(offset: IntOffset): Int =
    hypot(offset.x - x.toFloat(), offset.y - y.toFloat()).toInt()

operator fun IntOffset.plus(direction: Direction) = this + direction.offset

fun List<Direction>.calculatePosition() = fold(IntOffset(0, 0)) { acc, dir -> acc + dir.offset }