package day7

data class HandCard(
    val card: String
) {


    fun whatContains(): CamelRules {
        return if (contains5()) CamelRules.CONTAINS_5
        else if (contains4()) CamelRules.CONTAINS_4
        else if (containsFull()) CamelRules.FULL_HOUSE
        else if (contains3()) CamelRules.CONTAINS_3
        else if (containsTwoPair()) CamelRules.CONTAINS_2_PAIR
        else if (containsPair()) CamelRules.CONTAINS_1_PAIR
        else CamelRules.CONTAINS_NOTHING
    }

    fun containsFull(): Boolean {
        return card.any { check ->
            card.count { it == check } == 3
        } && card.any { check ->
            card.count { it == check } == 2
        }
    }

    fun contains5(): Boolean {
        return card.all { it == card.first() }
    }

    fun contains4(): Boolean {
        return card.any { check ->
            card.count { it == check } == 4
        }
    }

    fun contains3(): Boolean {
        return card.any { check ->
            card.count { it == check } == 3
        }
    }

    fun containsTwoPair(): Boolean {
        return card.toList().distinct().count() == 3
    }

    fun containsPair(): Boolean {
        return card.toList().distinct().count() == 4
    }

}
