package com.arkivanov.composnake

class RandomPointGenerator {

    private val available = HashSet<Point>()

    fun occupy(point: Point) {
        available -= point
    }

    fun free(point: Point) {
        available += point
    }

    fun generate(): Point = available.random()
}
