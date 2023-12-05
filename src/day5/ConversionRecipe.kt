package day5

data class ConversionRecipe(
    val destinationRangeStart: Long,
    val sourceRangeStart: Long,
    val rangeLength : Long,
    val type : RecipeType
)
