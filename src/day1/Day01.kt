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

    fun mapWordsToStringNumbers(list: List<String>): List<String> {
        // zamiana słownie zapisanej liczby na cyfrę, do każdej z cyfr zostanie
        // dodana ostatnia i pierwsza litera z jej nazwy np dla one -> o1e
        // zostało to zrobiono ponieważ z tych liter może korzystać inna cyfra "słowo"
        return list.asSequence().map {
            it.replace("one", "o1e")
        }.map {
            it.replace("two", "t2o")
        }.map {
            it.replace("three", "t3e")
        }.map {
            it.replace("four", "f4r")
        }.map {
            it.replace("five", "f5e")
        }.map {
            it.replace("six", "s6x")
        }.map {
            it.replace("seven", "s7n")
        }.map {
            it.replace("eight", "e8t")
        }.map {
            it.replace("nine", "n9e")
        }.toList()
    }

    fun part2(input: List<String>): Int {
        val listWithNumbers = mapWordsToStringNumbers(input)
        println(listWithNumbers)
        // skoro mam już sytuację jak w part1 to wrzucama w tą samą funkcję :)
        return part1(listWithNumbers)
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)

    val input = readInput("Day01")
    part1(input).println()

    val testInput2 = readInput("Day01_test2")
    check(part2(testInput2) == 281)

    part2(input).println()
}
