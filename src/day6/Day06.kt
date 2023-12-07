package day6


import println
import readInput

fun main() {

    fun getRecord(input: List<String>): Record {
        val regex = "\\d+".toRegex()
        val timeString = input.first().substringAfter("Time:")
        val distanceString = input.last().substringAfter("Distance:")
        var times: String = ""
        var distance: String = ""
        var match = regex.find(timeString)
        while (!match?.value.isNullOrBlank()) {
            times += (match?.value?.trim())
            match = match?.next()
        }
        match = regex.find(distanceString)
        while (!match?.value.isNullOrBlank()) {
            distance += (match?.value?.trim())
            match = match?.next()
        }
        return Record(times.toLong(), distance.toLong())
    }

    fun getAllRecords(input: List<String>): List<Record> {
        val regex = "\\d+".toRegex()
        val timeString = input.first().substringAfter("Time:")
        val distanceString = input.last().substringAfter("Distance:")
        val times = mutableListOf<Long?>()
        val distance = mutableListOf<Long?>()
        var match = regex.find(timeString)
        while (!match?.value.isNullOrBlank()) {
            times.add(match?.value?.trim()?.toLong())
            match = match?.next()
        }
        match = regex.find(distanceString)
        while (!match?.value.isNullOrBlank()) {
            distance.add(match?.value?.trim()?.toLong())
            match = match?.next()
        }
        return distance.filterNotNull().mapIndexed { index, it ->
            Record(times[index]!!, it)
        }
    }


    fun part1(input: List<String>): Int {
        val records = getAllRecords(input)
        println(records)
        val possibleWinList = records.map { record ->
            println(record)
            val possibleWin = (0..record.time).toList().map { pushButton ->
                val timeLeft = record.time - pushButton
                val distance = pushButton * timeLeft
                if (distance <= record.distance || pushButton == 0L || pushButton == record.time) {
                    println("For pushButton: $pushButton  timeLeft: $timeLeft distance is $distance")
                    null
                } else {
                    println("For pushButton: $pushButton  timeLeft: $timeLeft distance is $distance")
                    distance
                }

            }
            possibleWin.filterNotNull()
        }
        println(possibleWinList)
        println(possibleWinList.map { it.count() })
        return possibleWinList.map { it.count() }.reduce { acc, element -> acc * element }
    }

    fun part2(input: List<String>): Int {
        val record = getRecord(input)
        println(record)
        val possibleWin = (0..record.time).toList().mapNotNull { pushButton ->
            val timeLeft = record.time - pushButton
            val distance = pushButton * timeLeft
            if (distance <= record.distance || pushButton == 0L || pushButton == record.time) {
                println("For pushButton: $pushButton  timeLeft: $timeLeft distance is $distance")
                null
            } else {
                println("For pushButton: $pushButton  timeLeft: $timeLeft distance is $distance")
                distance
            }

        }
        return possibleWin.count()
    }


    /*   val testInput = readInput("Day06_test")
       part1(testInput).println()
       check(part1(testInput) == 288)
      */


    val input = readInput("Day06")
    //part1(input).println()
    //check(part1(input) == 23847)

    //val testInput2 = readInput("Day06_test2")
    //part2(testInput2).println()
    //check(part2(testInput2) == 71503)


    part2(input).println()
    //check(part2(input) == 6472060)*/
}
