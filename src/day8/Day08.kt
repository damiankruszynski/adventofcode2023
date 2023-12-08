package day8


import lcm
import println
import readInput

fun main() {
    fun stringToDigitInt(s: String): Int {
        var result = 0
        for (char in s) {
            if (char.isLetter()) {
                result = result * 26 + (char - 'A' + 10)
            } else {
                // Handle non-letter characters if needed
            }
        }
        return result
    }

    fun mapToNetworksMaps(input: List<String>): NetworkMaps {
        val steeps = InstructionMap(input.first().map { it.toString() }.toList())
        val map = input.subList(2, input.size).map {
            val key = it.substringBefore("=").trim()
            val regex = "[A-Z0-9]{3}".toRegex()
            var match = regex.find(it.substringAfter("=").trim())
            var left = ""
            var right = ""
            left += (match?.value?.trim())
            match = match?.next()
            right += (match?.value?.trim())
            NetworkMap(key, left, right)
        }
        return NetworkMaps(steeps, map)

    }


    fun part1(input: List<String>): Int {
        val network = mapToNetworksMaps(input)
        println("map contains ${network.map.count()}")
        val networkMap = network.map
        val start = "AAA"
        var result = ""
        val firstStep = networkMap.find { it.key == start }!!
        var whereGoNext = ""
        var currentOn: NetworkMap = firstStep
        var steeps = 0
        while (result != "ZZZ") {
            network.instructionMap.direction.forEach {
                whereGoNext = if (it == "L") currentOn.left else currentOn.right
                println("Where go next $whereGoNext")
                currentOn = networkMap.find { it.key == whereGoNext }!!
                println("Current on ${currentOn.key}")
                result = currentOn.key
                steeps++
                if (result == "ZZZ") return@forEach
            }
        }
        return steeps
    }


    fun part2(input: List<String>): Long {
        val network = mapToNetworksMaps(input)
        val maps = network.map.associate {
            it.key to (it.left to it.right)
        }
        return maps.keys.filter { it.endsWith("A") }.map { map ->
            var currentMap = map
            var instructionIndex = 0
            var step = 0L
            while (currentMap.last() != 'Z') {
                println("START currentMap: $currentMap")
                instructionIndex =
                    if (instructionIndex == network.instructionMap.direction.size) 0 else instructionIndex
                val direction = network.instructionMap.direction[instructionIndex]
                instructionIndex += 1
                println("DIRECTION: $direction")
                currentMap = if (direction == "L") maps[currentMap]!!.first else maps[currentMap]!!.second
                println("After change currentMap: $currentMap")
                step++
            }
            step

        }.also {
            println("Steeps count: ${it.size}")
            println("Steeps value: $it")
        }.reduce(::lcm)
    }


    // val testInput = readInput("Day08_test")
    // part1(testInput).println()
    // check(part1(testInput) == 6)


    val input = readInput("Day08")
    //part1(input).println()
    //  check(part1(input) == 17141)

    //  val testInput2 = readInput("Day08_test2")
    // part2(testInput2).println()
    //check(part2(testInput2) == 6L)
    part2(input).println()


    //check(part2(input) == 6472060)*/
}
