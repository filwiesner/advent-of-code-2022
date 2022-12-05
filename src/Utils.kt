import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

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
    var index = 0
    val rows = lines()
    val maxRowLength = rows.maxOf(String::length)
    while (index < maxRowLength) {
        val column = buildString {
            for (row in rows) {
                val letter = row.getOrNull(index) ?: continue
                if (!letter.isWhitespace()) append(letter)
            }
        }

        yield(column)
        index += 1
    }
}
