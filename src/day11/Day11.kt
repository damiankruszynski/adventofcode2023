package day11

import ANSI_BLUE
import ANSI_RED
import ANSI_YELLOW
import println
import readInput
import kotlin.math.abs

fun main() {

    fun printResult(map: List<List<GalaxyPlace>>) {
        println()
        map.forEach {
            it.forEach { galaxyPlace ->
                if (galaxyPlace.sign == '#') {
                    print(ANSI_YELLOW + galaxyPlace.sign)
                } else {
                    print(ANSI_BLUE + galaxyPlace.sign)
                }
            }
            println()
        }

    }

    fun generatePairs(range: Int): List<Pair<Int, Int>> {
        val pairs = mutableListOf<Pair<Int, Int>>()

        for (i in 1 until range) {
            for (j in i + 1..range) {
                pairs.add(Pair(i, j))
            }
        }

        return pairs
    }

    fun fixPosition(universe: Universe): Universe {
        val colCount = universe.map.first().size - 1
        val rowCount = universe.map.size - 1
        for (colIndex in 0..colCount) {
            for (rowIndex in 0..rowCount) {
                universe.map[rowIndex][colIndex].position = Position(rowIndex, colIndex)
            }
        }
        return universe
    }

    fun extendedUniverse(input: List<String>, extendedEmptyBy: Int = 1): Universe {
        val charArray2D = input.map { it.toCharArray().toList() }.toTypedArray()
        var galaxyNumber = 0
        val universe = Universe(charArray2D.mapIndexed { indexRow, it ->
            it.mapIndexed { indexCol, char ->
                if (char == '#') {
                    galaxyNumber += 1
                }
                GalaxyPlace(
                    sign = char,
                    position = Position(indexRow, indexCol),
                    galaxyNumber = if (char == '#') galaxyNumber else null
                )
            }
        })
        val mutableListForRow = mutableListOf<List<GalaxyPlace>>()
        universe.map.forEachIndexed { index, rowMap ->
            if (rowMap.all { place -> place.sign == '.' }) {
                repeat(extendedEmptyBy + 1) {
                    //println("Add row for index $index at the ${mutableListForRow.count()}")
                    mutableListForRow.add(rowMap)
                }
            } else {
                mutableListForRow.add(rowMap)
            }
        }
        val newUniverse = universe.copy(map = mutableListForRow.toList())
        val colCount = newUniverse.map.first().size - 1
        val rowCount = newUniverse.map.size - 1
        val addSignOnColIndex = mutableListOf<Int>()
        for (colIndex in 0..colCount) {
            var column = ""
            for (rowIndex in 0..rowCount) {
                column += newUniverse.map[rowIndex][colIndex].sign
            }
            if (column.all { it == '.' }) {
                addSignOnColIndex.add(colIndex)
            }
        }
        //println("Row count after extended: ${newUniverse.map.count()}")
       // println("Change on column index: $addSignOnColIndex")
        val newMap = newUniverse.map.map { it.toMutableList() }.mapIndexed { rowIndex, list ->
            var offset = 0
           // println("Column before change: ${newUniverse.map[rowIndex].map { it.sign }}")
            addSignOnColIndex.forEach { colIndex ->
                repeat(extendedEmptyBy) {
                    list.add(
                        colIndex + offset, GalaxyPlace(
                            sign = '.',
                            position = Position(0, 0),
                            galaxyNumber = null
                        )
                    )
                    offset += 1
                    //println("Add char for column ${colIndex + offset} for row $rowIndex")
                }
            }
            //println("Column After change = ${list.map { it.sign }}")
            list
        }
        //println("Column count after extended: ${newMap.first().count()}")
        return fixPosition(newUniverse.copy(map = newMap))
    }


    fun printResultPath(map: List<List<GalaxyPlace>>, pathMap: MutableList<Position>) {
        println()
        map.forEach {
            it.forEach { galaxyPlace ->
                if (pathMap.contains(galaxyPlace.position)) {
                    print(ANSI_RED + galaxyPlace.sign)
                } else if (galaxyPlace.sign == '#') {
                    print(ANSI_YELLOW + galaxyPlace.galaxyNumber)
                } else {
                    print(ANSI_BLUE + galaxyPlace.sign)
                }
            }
            println()
        }
    }

    fun part1(input: List<String>): Int {
        val newUniverse = extendedUniverse(input)
        printResult(newUniverse.map)
        val highestGalactic = newUniverse.map.flatten().maxBy { it.galaxyNumber ?: 0 }.galaxyNumber!!
        val pairs = generatePairs(highestGalactic)
        val sum = pairs.sumOf { pair ->
            val pathMap = mutableListOf<Position>()
            val positionForDest = newUniverse.map.flatten().find { it.galaxyNumber == pair.second }!!.position
            val positionForStart = newUniverse.map.flatten().find { it.galaxyNumber == pair.first }!!.position
            var currentOn = positionForStart
            while (currentOn.col != positionForDest.col) {
                currentOn = if (currentOn.col < positionForDest.col) {
                    Position(currentOn.row, currentOn.col + 1)
                } else {
                    Position(currentOn.row, currentOn.col - 1)
                }
                pathMap.add(currentOn)
            }
            while (currentOn.row != positionForDest.row) {
                currentOn = if (currentOn.row < positionForDest.row) {
                    Position(currentOn.row + 1, currentOn.col)
                } else {
                    Position(currentOn.row - 1, currentOn.col)
                }
                pathMap.add(currentOn)
            }
            // printResultPath(newUniverse.map, pathMap)
            pathMap.count()
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        val newUniverse = extendedUniverse(input, 1000000 -1)
        println("Start calculate")
       // printResult(newUniverse.map)
        val highestGalactic = newUniverse.map.flatten().maxBy { it.galaxyNumber ?: 0 }.galaxyNumber!!
        val pairs = generatePairs(highestGalactic)
        val sum = pairs.sumOf { pair ->
            val pathMap = mutableListOf<Position>()
            val positionForDest = newUniverse.map.flatten().find { it.galaxyNumber == pair.second }!!.position
            val positionForStart = newUniverse.map.flatten().find { it.galaxyNumber == pair.first }!!.position
            var currentOn = positionForStart
            while (currentOn.col != positionForDest.col) {
                currentOn = if (currentOn.col < positionForDest.col) {
                    Position(currentOn.row, currentOn.col + 1)
                } else {
                    Position(currentOn.row, currentOn.col - 1)
                }
                pathMap.add(currentOn)
            }
            while (currentOn.row != positionForDest.row) {
                currentOn = if (currentOn.row < positionForDest.row) {
                    Position(currentOn.row + 1, currentOn.col)
                } else {
                    Position(currentOn.row - 1, currentOn.col)
                }
                pathMap.add(currentOn)
            }
             //printResultPath(newUniverse.map, pathMap)
            pathMap.count()
        }
        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    //check(part1(testInput) == 374)

     val input = readInput("Day11")
    //check(part1(input) == 9681886)

    //  val testInput2 = readInput("Day01_test2")
   // println(part2(testInput))
     //check(part2(testInput) == 8410)

     part2(input).println()
}
