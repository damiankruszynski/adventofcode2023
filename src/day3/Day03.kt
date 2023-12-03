package day3

import println
import readInput

fun main() {



    fun part1(input: List<String>): Int {
       return 0
    }


    fun part2(input: List<String>): Int {
       return 0
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 8)

    val input = readInput("Day03")
    part1(input).println()

    val testInput2 = readInput("Day03_test2")
    check(part2(testInput2) == 2286)

    part2(input).println()
}
