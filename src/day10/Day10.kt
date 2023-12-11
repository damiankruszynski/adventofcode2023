package day10

import println
import readInput
import java.awt.Point

fun main() {


    fun mapToPipes(input: List<String>): List<List<Pipe>> {
        val charArray2D = input.map { it.toCharArray().toList() }.toTypedArray()
        return charArray2D.mapIndexed { indexRow, it ->
            it.mapIndexed { indexCol, char ->
                Pipe(
                    char.toString(),
                    position = Position(indexRow, indexCol)
                )
            }
        }

    }


    fun getPipeOrNull(pipes: List<List<Pipe>>, position: Position, direction: Direction): Pipe? {
        return try {
            when (direction) {
                Direction.LEFT -> pipes[position.row][position.col - 1]
                Direction.RIGHT -> pipes[position.row][position.col + 1]
                Direction.UP -> pipes[position.row - 1][position.col]
                Direction.DOWN -> pipes[position.row + 1][position.col]
                Direction.END -> null
            }
        } catch (e: Exception) {
            return null
        }
    }

    fun mapExitPipes(inDirection: Direction, currentPipe: Pipe, pipes: List<List<Pipe>>): Pair<Pipe?, Direction> {
        val exitOnDirection = when (inDirection) {
            Direction.LEFT -> {
                when (currentPipe.char) {
                    "-" -> Direction.LEFT
                    "L" -> Direction.UP
                    "F" -> Direction.DOWN
                    else -> Direction.END
                }
            }

            Direction.RIGHT -> {
                when (currentPipe.char) {
                    "-" -> Direction.RIGHT
                    "J" -> Direction.UP
                    "7" -> Direction.DOWN
                    else -> Direction.END
                }
            }

            Direction.DOWN -> when (currentPipe.char) {
                "|" -> Direction.DOWN
                "L" -> Direction.RIGHT
                "J" -> Direction.LEFT
                else -> Direction.END
            }

            Direction.UP -> when (currentPipe.char) {
                "|" -> Direction.UP
                "7" -> Direction.LEFT
                "F" -> Direction.RIGHT
                else -> Direction.END
            }

            Direction.END -> Direction.END
        }
        return Pair(getPipeOrNull(pipes, currentPipe.position, exitOnDirection), exitOnDirection)
    }

    fun calculateFromPoint(pipes: List<List<Pipe>>, startDirection: Direction, startPoint: Pipe): Long {
        val nextPipe = getPipeOrNull(pipes, startPoint.position, startDirection)
        var counter = 0L
        println("For start direction: $startDirection first pipe: $nextPipe")
        return if (nextPipe != null) {
            counter += 1
            var exitOn = mapExitPipes(startDirection, nextPipe, pipes)
            println("Exit on pipe: $exitOn")
            while (exitOn.first != null && exitOn.first!!.char != "." && exitOn.first!!.char != "S") {
                println("Exit on pipe: $exitOn")
                counter += 1
                exitOn = mapExitPipes(exitOn.second, exitOn.first!!, pipes)
                if (exitOn.first!!.char == "S") {
                    println("Exit on S - it is loop -> $counter")
                    counter /= 2
                }
            }
            counter + 1
        } else {
            0L
        }
    }

    fun getLoop(pipes: List<List<Pipe>>, startDirection: Direction, startPoint: Pipe): List<Pipe> {
        val nextPipe = getPipeOrNull(pipes, startPoint.position, startDirection)
        val loopList = mutableListOf<Pipe>()
        var itIsLoop: Boolean = false
        println("For start direction: $startDirection first pipe: $nextPipe")
        return if (nextPipe != null) {
            loopList.add(startPoint)
            var exitOn = mapExitPipes(startDirection, nextPipe, pipes)
            println("Exit on pipe: $exitOn")
            while (exitOn.first != null && exitOn.first!!.char != "." && exitOn.first!!.char != "S") {
                println("Exit on pipe: $exitOn")
                loopList.add(exitOn.first!!)
                exitOn = mapExitPipes(exitOn.second, exitOn.first!!, pipes)
                if (exitOn.first!!.char == "S") {
                    itIsLoop = true
                }
            }
            if (itIsLoop) {
                loopList.add(exitOn.first!!)
                return loopList
            } else {
                return emptyList()
            }

        } else {
            emptyList()
        }
    }

    //https://en.wikipedia.org/wiki/Even%E2%80%93odd_rule Even–odd rule ( but false when point is path part)
    fun isPointInPath(x: Int, y: Int, poly: List<Pair<Int, Int>>): Boolean {
        val num = poly.size
        var j = num - 1
        var c = false
        for (i in 0 until num) {
            if (x == poly[i].first && y == poly[i].second) {
                return false
            }
            if ((poly[i].second > y) != (poly[j].second > y)) {
                val slope = (x - poly[i].first) * (poly[j].second - poly[i].second) -
                        (poly[j].first - poly[i].first) * (y - poly[i].second)
                if (slope == 0) {
                    return true
                }
                if ((slope < 0) != (poly[j].second < poly[i].second)) {
                    c = !c
                }
            }
            j = i
        }
        return c
    }

    fun part1(input: List<String>): Long {
        val pipes = mapToPipes(input)
        val start = pipes.flatten().find { it.char == "S" }!!
        val result = Direction.entries.map {
            calculateFromPoint(pipes, it, start)
        }.maxOf { it }
        return result
    }


    fun part2(input: List<String>): Int {
        val pipes = mapToPipes(input)
        val start = pipes.flatten().find { it.char == "S" }!!
        val path = Direction.entries.map {
            getLoop(pipes, it, start)
        }.first { it.isNotEmpty() }.map { Pair(it.position.row, it.position.col) }
        val pipesInLoop = pipes.flatten().filter { isPointInPath(it.position.row, it.position.col, path) }
        return pipesInLoop.count()
    }


    // test if implementation meets criteria from the description, like:
    // val testInput = readInput("Day10_test")
    //println(part1(testInput))
    // check(part1(testInput) == 8L)

    val input = readInput("Day10")
    // part1(input).println()

    val testInput2 = readInput("Day10_test2")
    check(part2(testInput2) == 10)

    println(part2(input))
}
