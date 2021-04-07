package com.arkivanov.composnake

fun Direction.invert(): Direction =
    when (this) {
        Direction.LEFT -> Direction.RIGHT
        Direction.UP -> Direction.DOWN
        Direction.RIGHT -> Direction.LEFT
        Direction.DOWN -> Direction.UP
    }
