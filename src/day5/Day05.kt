package day5


import println
import readInput

fun main() {

    fun mapToAlmanac(input: List<String>): Almanac {
        val seeds = input.first().replace("seeds: ", "").trim().split(" ").map { it.toLong() }
        val preparedInput =
            input.map { it.replace("-", "_") }.map { it.replace(" map:", "") }.toString()
        val conversionRecipeList = RecipeType.entries.map { recipeType ->
            val text =
                preparedInput.substringAfter(recipeType.name.lowercase()).substringBefore(" , ").replace(",", "")
                    .replace("]", "").trim()
            val numbersList = text.split(" ").map { it.toLong() }.chunked(3)
            numbersList.map {
                ConversionRecipe(destinationRangeStart = it[0], it[1], it[2], recipeType)
            }
        }.flatten()
        return Almanac(seeds, conversionRecipeList)
    }

    fun getValueFor(almanac: Almanac, lookingFor: Long, recipeType: RecipeType): Long {
        val list = almanac.conversionRecipes.filter { it.type == recipeType }.map {
            val sourceRange = (it.sourceRangeStart..<it.sourceRangeStart + it.rangeLength)
            val indexOf = (sourceRange).indexOf(lookingFor)
            println("Recipe Type:$recipeType looking for $lookingFor on index: $indexOf")
            if (indexOf == -1) {
                lookingFor
            } else {
                (it.destinationRangeStart..<it.destinationRangeStart + it.rangeLength).elementAtOrElse(indexOf) { lookingFor }

            }
        }
        return list.find { it != lookingFor } ?: list.first()
    }

    fun part1(input: List<String>): Long {
        val almanac = mapToAlmanac(input)

        return almanac.seeds.minOf { seed ->
            val seedToSoil = getValueFor(almanac, seed, RecipeType.SEED_TO_SOIL)
            val soilToFertilizer = getValueFor(almanac, seedToSoil, RecipeType.SOIL_TO_FERTILIZER)
            val fertilizerToWater = getValueFor(almanac, soilToFertilizer, RecipeType.FERTILIZER_TO_WATER)
            val waterToLight = getValueFor(almanac, fertilizerToWater, RecipeType.WATER_TO_LIGHT)
            val lightToTemperature = getValueFor(almanac, waterToLight, RecipeType.LIGHT_TO_TEMPERATURE)
            val temperatureToHumidity =
                getValueFor(almanac, lightToTemperature, RecipeType.TEMPERATURE_TO_HUMIDITY)
            val humidityToLocation =
                getValueFor(almanac, temperatureToHumidity, RecipeType.HUMIDITY_TO_LOCATION)
            println(
                listOf(
                    seedToSoil,
                    soilToFertilizer,
                    fertilizerToWater,
                    waterToLight,
                    lightToTemperature,
                    temperatureToHumidity,
                    humidityToLocation
                )
            )
            humidityToLocation
        }

    }

    fun part2(input: List<String>): Long {
        return 0
    }

    val testInput = readInput("Day05_test")
    part1(testInput).println()
    check(part1(testInput) == 35L)


    val input = readInput("Day05")
    part1(input).println()
    // check(part1(input) == 23847)
    /*
        val testInput2 = readInput("Day04_test2")
        check(part2(testInput2) == 30)


        part2(input).prLongln()
        check(part2(input) == 8570000)*/
}
