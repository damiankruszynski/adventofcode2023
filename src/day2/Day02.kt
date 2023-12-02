package day2

import println
import readInput

fun main() {

    fun mapToSetOfCube(line: String): SetOfCube {
        val color = line.filter { it.isLetter() }.trim()
        val number = line.filter { it.isDigit() }.trim().toInt()
        return SetOfCube(number, CubeColor.valueOf(color))
    }

    fun mapToGame(line: String): Game {
        val id = line.substringBefore(":").replace("Game", "").trim().toInt()
        var lineSubstr = line.substringAfter(":").uppercase()
        val setOfCubeLists = mutableListOf<List<SetOfCube>>()
        while (lineSubstr.contains(";")) {
            val setOfCube = lineSubstr.substringBefore(";").trim().split(",")
            setOfCubeLists.add(setOfCube.map {
                mapToSetOfCube(it)
            })
            lineSubstr = lineSubstr.substringAfter(";").trim()
        }
        val last = lineSubstr.split(",")
        setOfCubeLists.add(last.map {
            mapToSetOfCube(it)
        })
        return Game(id, setOfCubeLists)

    }

    fun checkIfGameIsValid(game: Game): Boolean {
        val maxRED = 12
        val maxGREEN = 13
        val maxBLUE = 14
        return game.setOfCubes.flatten().all {
            when (it.color) {
                CubeColor.RED -> it.count <= maxRED
                CubeColor.BLUE -> it.count <= maxBLUE
                CubeColor.GREEN -> it.count <= maxGREEN
            }
        }
    }

    fun part1(input: List<String>): Int {
        return input.map {
            mapToGame(it)
        }.filter {
            checkIfGameIsValid(it)
        }.sumOf {
            it.id
        }
    }

    fun getMaxByColorFromGame(game: Game, color: CubeColor): Int {
        return game.setOfCubes.flatten().filter { it.color == color }.maxByOrNull { it.count }?.count ?: 1
    }

    fun part2(input: List<String>): Int {
        val games =  input.map {
            mapToGame(it)
        }
        return games.sumOf {
            val red = getMaxByColorFromGame(it, CubeColor.RED)
            val blue = getMaxByColorFromGame(it, CubeColor.BLUE)
            val green = getMaxByColorFromGame(it, CubeColor.GREEN)
            red * blue * green
        }
    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)

    val input = readInput("Day02")
    part1(input).println()

    val testInput2 = readInput("Day02_test2")
    check(part2(testInput2) == 2286)

    part2(input).println()
}
