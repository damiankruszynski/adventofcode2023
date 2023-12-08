package day8


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
        val networkMap = network.map
        val start = "AAA"
        var result = ""
        val firstStep = networkMap.find { it.key == start }!!
        var whereGoNext = ""
        var currentOn: NetworkMap = firstStep
        var steeps = 0
        while (result != "ZZZ") {
            network.instructionMap.steps.forEach {
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


    fun part2(input: List<String>): Int {
        val network = mapToNetworksMaps(input)
        val networkMap = network.map
        val start = 'A'
        val firstSteps = networkMap.filter { it.key.last() == start }
        var whereGoNext: List<String> = mutableListOf()
        var currentOn: List<NetworkMap> = firstSteps
        var result: List<String> = currentOn.map { it.key }
        var steeps: Int = 0
        while (result.any { it.last() != 'Z' }) {
            network.instructionMap.steps.forEach { step ->
                whereGoNext = if (step == "L") currentOn.map { it.left } else currentOn.map { it.right }
                println("Where go next $whereGoNext")
                currentOn = networkMap.filter { whereGoNext.contains(it.key) }
                println("Current on ${currentOn.map { it.key }}")
                result = currentOn.map { it.key }
                steeps++
                if (result.all { char -> char.last() == 'Z' }) return@forEach
            }
        }
        return steeps
    }


    tailrec fun processSteps(
        currentOn: List<NetworkMap>,
        steeps: Int,
        network: NetworkMaps,
        stepIndex: Int
    ): Int {
        val result = currentOn.map { it.key }
        if (result.all { it.last() == 'Z' }) return steeps

        val step = network.instructionMap.steps[stepIndex]
        val nextNodes = currentOn.flatMap { node ->
            listOfNotNull(node.left.takeIf { step == "L" }, node.right.takeIf { step == "R" })
                .flatMap { whereGoNext ->
                    network.map.filter { it.key == whereGoNext }
                }
        }

        return processSteps(nextNodes, steeps + 1, network, (stepIndex + 1) % network.instructionMap.steps.size)
    }

    fun part2_U(input: List<String>): Int {
        val network = mapToNetworksMaps(input)
        val start = 'A'
        val firstSteps = network.map.filter { it.key.last() == start }
        return processSteps(firstSteps, 0, network, 0)
    }


    val testInput = readInput("Day08_test")
    part1(testInput).println()
    check(part1(testInput) == 6)


    val input = readInput("Day08")
    part1(input).println()
    check(part1(input) == 17141)

    val testInput2 = readInput("Day08_test2")
    // part2(testInput2).println()
    //check(part2(testInput2) == 6)
    //part2_U(input).println()


    //check(part2(input) == 6472060)*/
}
