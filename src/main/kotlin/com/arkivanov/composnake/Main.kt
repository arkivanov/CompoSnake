package com.arkivanov.composnake

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

// These make the cells an attractive size.
private val CELL_SIZE = 24.dp

@OptIn(ExperimentalComposeUiApi::class)
fun main() = application {
    val game: Game = DefaultGame()
    val focusRequester = FocusRequester()

    Window(
        state = WindowState(size = DpSize.Unspecified),
        onCloseRequest = ::exitApplication,
        title = "CompoSnake"
    ) {
        MaterialTheme {
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
                while (isActive) {
                    delay(400L)
                    game.step()
                }
            }

            Box(
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .focusTarget()
                    .onKeyEvent {
                        when (it.key) {
                            Key.DirectionLeft -> game.setDirection(Direction.LEFT).let { true }
                            Key.DirectionUp -> game.setDirection(Direction.UP).let { true }
                            Key.DirectionRight -> game.setDirection(Direction.RIGHT).let { true }
                            Key.DirectionDown -> game.setDirection(Direction.DOWN).let { true }
                            else -> false
                        }
                    }
            )

            Box(
                contentAlignment = Alignment.Center
            ) {
                Board(game.board.value)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Board(board: Board) {
    if (board.snake == null) {
        Text(
            text = "Game over!",
            style = MaterialTheme.typography.h3
        )
        return
    }

    Column(
        modifier = Modifier.padding(vertical = 32.dp)
    ) {
        board.grid.forEachFast { row ->
            Row(
                modifier = Modifier.padding(horizontal = 32.dp)
            ) {
                row.forEachFast { cell ->
                    when (cell) {
                        board.food -> FoodCell()
                        in board.snake.points -> SnakeCell(isHead = cell == board.snake.head)
                        else -> EmptyCell()
                    }
                }
            }
        }
    }
}

@Composable
private fun FoodCell() {
    RadioButton(
        modifier = Modifier.size(CELL_SIZE),
        selected = true,
        onClick = {},
        colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colors.primary)
    )
}

@Composable
private fun EmptyCell() {
    Checkbox(
        modifier = Modifier.size(CELL_SIZE),
        checked = false,
        onCheckedChange = {}
    )
}

@Composable
private fun SnakeCell(isHead: Boolean) {
    Checkbox(
        checked = true,
        modifier = Modifier.size(CELL_SIZE),
        colors = CheckboxDefaults.colors(
            checkedColor = if (isHead) MaterialTheme.colors.primary else MaterialTheme.colors.secondary
        ),
        onCheckedChange = {}
    )
}
