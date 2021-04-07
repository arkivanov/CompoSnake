package com.arkivanov.composnake

data class Board(
    val snake: Snake?,
    val grid: List<List<Point>>,
    val cells: Set<Point>,
    val direction: Direction,
    val food: Point?
)
