package com.arkivanov.composnake

import androidx.compose.runtime.State

interface Game {

    val board: State<Board>

    fun step()

    fun setDirection(direction: Direction)
}

