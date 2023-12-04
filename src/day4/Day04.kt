package day4

import println
import readInput

fun main() {

    fun mapToCard(line: String): Card {
        val id = line.substringBefore(":").replace("Card", "").trim().toInt()
        val regex = "\\d+".toRegex()
        val winNumbersString = line.substringBefore("|").substringAfter(":")
        val winNumbers = mutableListOf<Int?>()
        val choseNumbersString = line.substringAfter("|")
        val choseNumbers = mutableListOf<Int?>()
        var match = regex.find(winNumbersString)
        while (!match?.value.isNullOrBlank()) {
            winNumbers.add(match?.value?.trim()?.toInt())
            match = match?.next()
        }
        match = regex.find(choseNumbersString)
        while (!match?.value.isNullOrBlank()) {
            choseNumbers.add(match?.value?.trim()?.toInt())
            match = match?.next()
        }
        return Card(id, winNumbers.filterNotNull(), choseNumbers.filterNotNull())
    }

    fun part1(input: List<String>): Int {
        return input.map {
            mapToCard(it)
        }.sumOf { card ->
            when (val count = card.chooseNumbers.intersect(card.winningNumbers.toSet()).size) {
                1 -> 1
                0 -> 0
                else -> {
                    var sum = 1
                    repeat(count - 1) {
                        sum *= 2
                    }
                    sum
                }
            }
        }
    }

    fun part2(input: List<String>): Int {
        val cardList = input.map {
            mapToCard(it)
        }
        val cardWithCopies = cardList.associateWith { 0 }.toMutableMap()
        cardList.map { card ->
            val copiesOfOriginal = if (cardWithCopies[card] == 0) 1 else cardWithCopies[card]!!.plus(1)
            val count = card.chooseNumbers.intersect(card.winningNumbers.toSet()).size
            repeat(copiesOfOriginal) {
                val addCopiesFor = cardList.slice(card.id..<card.id + count)
                addCopiesFor.forEach {
                    cardWithCopies.computeIfPresent(it) { _, copy -> copy + 1 }
                }
            }
            card
        }
        return cardWithCopies.entries.sumOf { it.value } + cardList.size
    }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)


    val input = readInput("Day04")
    part1(input).println()
    check(part1(input) == 23847)

    val testInput2 = readInput("Day04_test2")
    check(part2(testInput2) == 30)


    part2(input).println()
    check(part2(input) == 8570000)
}
