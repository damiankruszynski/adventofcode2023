package day3

import println
import readInput

fun main() {

    fun checkIfThisLineOnTheSamePositionPlusOneIndexMinusOneIndexContainsSpecialChar(
        line: String?,
        startIndex: Int,
        endIndex: Int
    ): Boolean {
        if (line == null) return false
        val cutFrom = if (startIndex == 0) 0 else startIndex - 1
        val cutTo = if (endIndex == line.length - 1) line.length - 1 else endIndex + 2
        println("for line: $line cutForm: $cutFrom cutTo: $cutTo")
        val testedPart = line.substring(cutFrom, cutTo).replace(".", "a")
        println(
            "Tested part: $testedPart result: ${
                !testedPart.all {
                    it.isLetterOrDigit()
                }
            }"
        )
        return !testedPart.all {
            it.isLetterOrDigit()
        }
    }

    fun part1(input: List<String>): Int {
        val listOfValid = mutableListOf<Int>()
        val listOfInValid = mutableListOf<Int>()
        repeat(input.size) {
            val testedLine = input[it]
            val previousLine = input.getOrNull(it - 1)
            val nextLine = input.getOrNull(it + 1)
            var number = ""
            var startIndexOfNumber: Int? = null
            var endIndexOfNumber: Int? = null
            println("tested line: $testedLine")
            testedLine.forEachIndexed { index, c ->
                if (c.isDigit() && index != testedLine.length - 1) {
                    number += c
                    if (startIndexOfNumber == null) {
                        startIndexOfNumber = index
                    }
                } else {
                    val last = if (index == testedLine.length - 1 && c.isDigit()) c else ""
                    if (number == "") return@forEachIndexed
                    val foundNumber = (number + last).toInt()
                    number = ""
                    if (endIndexOfNumber == null) {
                        endIndexOfNumber = index - 1
                    }
                    println("foundNumber: $foundNumber")
                    if (
                        checkIfThisLineOnTheSamePositionPlusOneIndexMinusOneIndexContainsSpecialChar(
                            previousLine,
                            startIndexOfNumber!!,
                            endIndexOfNumber!!
                        ) ||
                        checkIfThisLineOnTheSamePositionPlusOneIndexMinusOneIndexContainsSpecialChar(
                            testedLine,
                            startIndexOfNumber!!,
                            endIndexOfNumber!!
                        ) ||
                        checkIfThisLineOnTheSamePositionPlusOneIndexMinusOneIndexContainsSpecialChar(
                            nextLine,
                            startIndexOfNumber!!,
                            endIndexOfNumber!!
                        )
                    ) {
                        listOfValid.add(foundNumber)
                        startIndexOfNumber = null
                        endIndexOfNumber = null
                    } else {
                        listOfInValid.add(foundNumber)
                        startIndexOfNumber = null
                        endIndexOfNumber = null
                    }

                }
            }

        }
        println("VALID: $listOfValid")
        println("INVALID: $listOfInValid")
        return listOfValid.sum()
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)

    val input = readInput("Day03")
    part1(input).println()
    check(part1(input) == 530495)

    /*  val testInput2 = readInput("Day03_test2")
      check(part2(testInput2) == 2286)

      part2(input).println()*/
}
