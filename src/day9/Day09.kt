package day9

import println
import readInput
import kotlin.math.abs

fun main() {

    fun mapToIntList(input: List<String>): List<List<Int>> {
        return input.map {
            it.split(" ").map { it.toInt() }
        }

    }

    fun calculateVariationsUntil0(list: List<Int>): List<List<Int>> {
        val result = mutableListOf(list)
        while (result.last().any { it != 0 }) {
            result.add(
                result.last().mapIndexed { index, it ->
                    val next = if (index < result.last().size - 1) result.last()[index + 1] else 0
                    next - it
                }.take(result.last().size -1)
            )
        }
        return result
    }


    fun changeList(lastLine: List<Int>, nextLines: List<List<Int>>) : List<Int>{
        val result = lastLine.last() + nextLines.sumOf { it.last() }
        return lastLine + listOf(result)
    }

    fun changeListReversed(current: MutableList<Int>, previous: List<Int>) : List<Int>{
        if(current.all { it == 0 }) {
            current.add(0)
            return current
        }
        val result = current.first() - previous.first()
        current.add(0, result)
        return current
    }


    fun part1(input: List<String>): Int {
        val list = mapToIntList(input)
        return list.sumOf {
            val result = calculateVariationsUntil0(it)
            result.reversed().forEach {
                println(it)
            }
            val new = result.reversed().mapIndexed { index, last ->
                changeList(last, result.takeLast(index))

            }
            println("NEW:")
            new.reversed().forEach {
                println(it)
            }
            println("For sum : ${new.last().last()}")
            new.last().last()
        }
    }

    fun part2(input: List<String>): Int {
        val list = mapToIntList(input)
        val sum =  list.sumOf {
            val result = calculateVariationsUntil0(it).map { res -> res.toMutableList() }.reversed()
             println("Until 0:")
            result.forEach { each ->
                println(each)
            }
             val new = result.mapIndexed { index, current ->
                 if(index != 0){
                     changeListReversed(current, result[index -1])
                 }
                 current
             }
             println("NEW:")
             new.forEach {
                 println(it)
             }
             new.last().first()
        }
        return sum
    }
   // val testInput = readInput("Day09_test")
    //check(part1(testInput) == 114)

      val input = readInput("Day09")
      // part1(input).println()

       val testInput2 = readInput("Day09_test2")
       check(part2(testInput2) == 2)

       part2(input).println()
}
