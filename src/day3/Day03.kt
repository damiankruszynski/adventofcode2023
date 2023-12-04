package day3

import println
import readInput

fun main() {

    fun getListOfPartsFromEngine(input: List<String>): List<PartInEngine> {
        val regex = "\\d+".toRegex()
        val list = mutableListOf<PartInEngine>()
        input.mapIndexed { index, it ->
            var match = regex.find(it)
            while (!match?.value.isNullOrBlank()) {
                list.add(PartInEngine(index, match!!.range, match.value.toInt()))
                match = match.next()
            }
        }
        return list
    }

    fun getSpecialCharacters(input: List<String>): List<SpecialCharactersInEngine> {
        val regex = "\\D".toRegex()
        val list = mutableListOf<SpecialCharactersInEngine>()
        input.map { it.replace(".", "0") }.mapIndexed { index, it ->
            var match = regex.find(it)
            while (!match?.value.isNullOrBlank()) {
                list.add(SpecialCharactersInEngine(index, match!!.range.first, match!!.value))
                match = match!!.next()
            }
        }
        return list
    }

    fun isValidPart(
        partInEngine: PartInEngine,
        listOfSpecialCharactersInEngine: List<SpecialCharactersInEngine>,
        lineLength: Int,
        inputSize: Int
    ): Boolean {
        val rangeToCheckStart = if (partInEngine.rangeIndex.first == 0) 0 else partInEngine.rangeIndex.first - 1
        val rangeToCheckLast =
            if (partInEngine.rangeIndex.last == lineLength) lineLength else partInEngine.rangeIndex.last + 1
        val rangeToCheck = rangeToCheckStart..rangeToCheckLast
        println(rangeToCheck)
        val previousLine = if (partInEngine.lineIndex != 0) {
            listOfSpecialCharactersInEngine.any {
                (it.lineIndex == partInEngine.lineIndex - 1) && rangeToCheck.contains(it.index)
            }
        } else
            false
        val nextLine = if (partInEngine.lineIndex != inputSize) {
            listOfSpecialCharactersInEngine.any {
                (it.lineIndex == partInEngine.lineIndex + 1) && rangeToCheck.contains(it.index)
            }
        } else
            false
        val theSameLine = listOfSpecialCharactersInEngine.any {
            (it.lineIndex == partInEngine.lineIndex) && rangeToCheck.contains(it.index)
        }
        return previousLine || nextLine || theSameLine
    }

    fun partsCloseToSpecialChar(
        partsInEngine: List<PartInEngine>,
        specialCharactersInEngine: SpecialCharactersInEngine,
        lineLength: Int,
        inputSize: Int
    ): List<PartInEngine> {
        return partsInEngine.filter {
            it.lineIndex == specialCharactersInEngine.lineIndex - 1 ||
                    it.lineIndex == specialCharactersInEngine.lineIndex + 1 ||
                    it.lineIndex == specialCharactersInEngine.lineIndex
        }.filter {
            val rangeToCheckStart = if (it.rangeIndex.first == 0) 0 else it.rangeIndex.first - 1
            val rangeToCheckLast =
                if (it.rangeIndex.last == lineLength) lineLength else it.rangeIndex.last + 1
            val rangeToCheck = rangeToCheckStart..rangeToCheckLast
            rangeToCheck.contains(specialCharactersInEngine.index)
        }
    }

    fun part1(input: List<String>): Int {
        val list = getListOfPartsFromEngine(input)
        println(list)
        val listSpecial = getSpecialCharacters(input)
        println(listSpecial)
        return list.sumOf {
            if (isValidPart(it, listSpecial, input.first().length, input.size)) {
                it.value
            } else 0
        }
    }

    fun part2(input: List<String>): Long {
        val list = getListOfPartsFromEngine(input)
        println(list)
        val listSpecial = getSpecialCharacters(input)
        println(listSpecial)
        val partsCloseToSpecialChar = listSpecial.map {
            partsCloseToSpecialChar(list, it, input.first().length, input.size)
        }.filter { it.size == 2 }.sumOf { it.first().value.toLong() * it.last().value.toLong() }
        return partsCloseToSpecialChar
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)

    val input = readInput("Day03")
    part1(input).println()
    check(part1(input) == 530495)

    val testInput2 = readInput("Day03_test2")
    check(part2(testInput2) == 467835L)

    part2(input).println()
}
