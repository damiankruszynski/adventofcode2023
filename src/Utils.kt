import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/main/resources/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

/**
 * Calculates the greatest common divisor (GCD) of two numbers.
 *
 * @param a The first number.
 * @param b The second number.
 * @return The GCD of the two numbers.
 */
tailrec fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)

/**
 * Calculates the least common multiple (LCM) of two numbers.
 *
 * @param a The first number.
 * @param b The second number.
 * @return The LCM of the two numbers.
 */
fun lcm(a: Long, b: Long): Long {
    return a / gcd(a, b) * b
}
