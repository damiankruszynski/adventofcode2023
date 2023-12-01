package day1

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int {
        return input.map {
            val fromLeft = it.find { char -> char.isDigit() } // szuka pierwszej cyfry od lewej strony
            val fromRight = it.findLast { char -> char.isDigit() } // szuka ostatniej cyfry od lewej
            "$fromLeft$fromRight"
        }.map {
            it.toInt()
        }.also {
            println(it)
        }.sum()
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)

    val input = readInput("Day01")
    part1(input).println()


    /*part2(input).println()*/
}
