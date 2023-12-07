package day7


import day7.CamelRules.CONTAINS_1_PAIR
import day7.CamelRules.CONTAINS_2_PAIR
import day7.CamelRules.CONTAINS_3
import day7.CamelRules.CONTAINS_4
import day7.CamelRules.CONTAINS_5
import day7.CamelRules.CONTAINS_NOTHING
import day7.CamelRules.FULL_HOUSE
import println
import readInput

fun main() {

    val possibleCard = listOf<String>(
        "A", "K", "Q", "J", "T", "9", "8", "7", "6", "5", "4", "3", "2", "J"
    )


    fun toHandCardList(input: List<String>): List<CamelCardValue> {
        return input.map {
            CamelCardValue(HandCard(it.substringBefore(" ")), it.substringAfter(" ").toInt())
        }
    }

    fun cardStrengthResult(chosenChar: Char, compareToChar: Char): Boolean? {
        val first = possibleCard.indexOf(chosenChar.toString())
        val second = possibleCard.indexOf(compareToChar.toString())
        return if (first < second)
            true
        else if (first == second)
            null
        else
            false
    }

    fun isStrongerByRules(choosenHandCard: HandCard, compareToHandCard: HandCard): Boolean {
        val choosen = choosenHandCard.whatContains()
        val compareTo = compareToHandCard.whatContains()
        val result = when (choosen) {
            CONTAINS_5 -> compareTo != CONTAINS_5
            CONTAINS_4 -> compareTo != CONTAINS_5 && compareTo != CONTAINS_4
            FULL_HOUSE -> compareTo != CONTAINS_5 && compareTo != CONTAINS_4 && compareTo != FULL_HOUSE
            CONTAINS_3 -> compareTo != CONTAINS_5 && compareTo != CONTAINS_4 && compareTo != FULL_HOUSE && compareTo != CONTAINS_3
            CONTAINS_2_PAIR -> compareTo != CONTAINS_5 && compareTo != CONTAINS_4 && compareTo != CONTAINS_3 && compareTo != CONTAINS_2_PAIR && compareTo != FULL_HOUSE
            CONTAINS_1_PAIR -> compareTo == CONTAINS_NOTHING
            CONTAINS_NOTHING -> false
        }
        println("Chosen card: ${choosenHandCard.card} have $choosen CompareTo: ${compareToHandCard.card} have $compareTo Chosen is win: $result")
        return result
    }


    fun isStrongerHandCard(choosen: HandCard, compareTo: HandCard): Boolean {
        println("Choosen: $choosen CompareTo: $compareTo")
        println("Choosen contains: ${choosen.whatContains()} compareTo contains: ${compareTo.whatContains()}")
        return if (choosen.whatContains() == compareTo.whatContains()) {
            var result: Boolean? = null
            choosen.card.forEachIndexed { index, it ->
                result = cardStrengthResult(it, compareTo.card[index])
                println("cardStrengthResult for $it, ${compareTo.card[index]} is $result")
                if (result != null) return result!!
            }
            true
        } else {
            isStrongerByRules(choosen, compareTo)
        }
    }

    fun replaceJokerByBestChar(card: String): String {
        if (HandCard(card).contains5()) return "AAAAA"
        val map = card.filter { it != 'J' }.toList().associateWith { char -> card.count { it == char } }
        val recordsWith2 = map.values.count { it == 2 }
        var charToChooseForReplace: Char? = null
        charToChooseForReplace = if (recordsWith2 == 2) {
            val entries = map.entries.filter { it.value == 2 }
            val first = entries.first()
            val last = entries.last()
            if (cardStrengthResult(first.key, last.key)!!) {
                first.key
            } else
                last.key
        } else {
            map.maxBy { it.value }.key
        }
        // println("Char to replacement for card: $card is $charToChooseForReplace After replace: ${card.replace('J', charToChooseForReplace)}")
        return card.replace('J', charToChooseForReplace)
    }

    fun isStrongerHandCardPart2(choosen: HandCard, compareTo: HandCard): Boolean {
        val choosenAfterReplaceJoker =
            if (choosen.card.contains('J')) HandCard(replaceJokerByBestChar(choosen.card)) else choosen
        val compareToAfterReplaceJoker =
            if (compareTo.card.contains('J')) HandCard(replaceJokerByBestChar(compareTo.card)) else compareTo
        return if (choosenAfterReplaceJoker.whatContains() == compareToAfterReplaceJoker.whatContains()) {
            var result: Boolean? = null
            choosen.card.forEachIndexed { index, it ->
                result = cardStrengthResult(it, compareTo.card[index])
                if (result != null) {
                    println("cardStrengthResult for ${choosen.card} compare to ${compareTo.card} for $it, ${compareTo.card[index]} is $result")
                    return result!!
                }
            }
            true
        } else {
            isStrongerByRules(choosenAfterReplaceJoker, compareToAfterReplaceJoker)
        }
    }


    fun part1(input: List<String>): Int {
        val handCardList = toHandCardList(input)
        return handCardList.map { choosen ->
            val howMuchWins = handCardList.filter { it != choosen }.filter {
                isStrongerHandCard(choosen.card, it.card)
            }.count()
            (choosen to howMuchWins)
        }.sortedBy {
            it.second
        }.mapIndexed { index, pair ->
            println("${pair.first.card.card} = ${pair.first.value} * ${(index + 1)}")
            pair.first.value * (index + 1)
        }.sum()
    }


    fun part2(input: List<String>): Int {
        val handCardList = toHandCardList(input)
        return handCardList.map { choosen ->
            val howMuchWins = handCardList.filter { it != choosen }.filter {
                isStrongerHandCardPart2(choosen.card, it.card)
            }.count()
            (choosen to howMuchWins)
        }.sortedBy {
            it.second
        }.mapIndexed { index, pair ->
            println("${pair.first.card.card} = ${pair.first.value} * ${(index + 1)}")
            pair.first.value * (index + 1)
        }.sum()
    }


    /*  val testInput = readInput("Day07_test")
      part1(testInput).println()
      check(part1(testInput) == 2800)*/

    val input = readInput("Day07")
    // part1(input).println()

    /*al testInput2 = readInput("Day07_test2")
    part2(testInput2).println()
    check(part2(testInput2) == 5905)*/

    val testInput3 = readInput("Day07_test_test2")
    part2(testInput3).println()
    //(input).println()
    //check(part2(input) == 6472060)*/
}
