package day5


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
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
        val firstTraffic = almanac.conversionRecipes.firstOrNull {
            it.type == recipeType && (it.sourceRangeStart..<it.sourceRangeStart + it.rangeLength).contains(lookingFor)
        }
        return if (firstTraffic != null) {
            val indexOf = lookingFor - firstTraffic.sourceRangeStart
            firstTraffic.destinationRangeStart + indexOf
        } else
            lookingFor
    }

    fun getValueForReverse(almanac: Almanac, lookingFor: Long, recipeType: RecipeType): Long {
        val firstTraffic = almanac.conversionRecipes.firstOrNull {
            it.type == recipeType && (it.destinationRangeStart..<it.destinationRangeStart + it.rangeLength).contains(
                lookingFor
            )
        }
        return if (firstTraffic != null) {
            val indexOf = lookingFor - firstTraffic.destinationRangeStart
            firstTraffic.sourceRangeStart + indexOf
        } else
            lookingFor
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
        val almanac = mapToAlmanac(input)
        val seedsRange =
            almanac.seeds.mapIndexed { index, it -> if (index % 2 == 0) it..<it + almanac.seeds[index + 1] else null }
                .filterNotNull()
        return runBlocking(Dispatchers.IO) {
            coroutineScope {
                seedsRange.map { seed ->
                    async {
                        coroutineScope {
                            seed.minOf {
                                val seedToSoil = getValueFor(almanac, it, RecipeType.SEED_TO_SOIL)
                                val soilToFertilizer = getValueFor(almanac, seedToSoil, RecipeType.SOIL_TO_FERTILIZER)
                                val fertilizerToWater =
                                    getValueFor(almanac, soilToFertilizer, RecipeType.FERTILIZER_TO_WATER)
                                val waterToLight = getValueFor(almanac, fertilizerToWater, RecipeType.WATER_TO_LIGHT)
                                val lightToTemperature =
                                    getValueFor(almanac, waterToLight, RecipeType.LIGHT_TO_TEMPERATURE)
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
                    }
                }.awaitAll().minOf { it }
            }
        }
    }

    fun part2Reverse(input: List<String>): Long {
        val almanac = mapToAlmanac(input)
        val seedsRange =
            almanac.seeds.mapIndexed { index, it -> if (index % 2 == 0) it..<it + almanac.seeds[index + 1] else null }
                .filterNotNull()
        val locationListRange = almanac.conversionRecipes.filter { it.type == RecipeType.HUMIDITY_TO_LOCATION }
            .map { it.destinationRangeStart..<it.destinationRangeStart + it.rangeLength }
            .sortedBy { it.first }
        var lowestLocation = Long.MAX_VALUE
        locationListRange.find { locationRange ->
            locationRange.first { location ->
                val humidity = getValueForReverse(almanac, location, RecipeType.HUMIDITY_TO_LOCATION)
                val temperature = getValueForReverse(almanac, humidity, RecipeType.TEMPERATURE_TO_HUMIDITY)
                val light = getValueForReverse(almanac, temperature, RecipeType.LIGHT_TO_TEMPERATURE)
                val water = getValueForReverse(almanac, light, RecipeType.WATER_TO_LIGHT)
                val fertilizer = getValueForReverse(almanac, water, RecipeType.FERTILIZER_TO_WATER)
                val soil = getValueForReverse(almanac, fertilizer, RecipeType.SOIL_TO_FERTILIZER)
                val seed = getValueForReverse(almanac, soil, RecipeType.SEED_TO_SOIL)
                println(
                    "For Location: $location ${
                        listOf(
                            location,
                            humidity,
                            temperature,
                            light,
                            water,
                            fertilizer,
                            soil,
                            seed
                        )
                    }"
                )
                lowestLocation = location
                seedsRange.any { it.contains(seed) }
            } != 0L
        }
        return lowestLocation
    }


    val testInput = readInput("Day05_test")
    part1(testInput).println()
    check(part1(testInput) == 35L)


    val input = readInput("Day05")
    part1(input).println()
    // check(part1(input) == 23847)

    val testInput2 = readInput("Day05_test2")
    part2(testInput2).println()
    check(part2(testInput2) == 46L)


    part2Reverse(input).println()
    //check(part2(input) == 6472060)
}
