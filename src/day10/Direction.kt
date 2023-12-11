package day10

enum class Direction {
    LEFT,
    RIGHT,
    UP,
    DOWN,
    END;

    fun oposite(): Direction {
       return when(this){
            LEFT -> RIGHT
            RIGHT -> LEFT
            UP -> DOWN
            DOWN -> UP
            END -> END
       }
    }
}
