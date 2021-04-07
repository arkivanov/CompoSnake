package com.arkivanov.composnake

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

class DefaultGame : Game {

    private val _board: MutableState<Board>
    private val randomPointGenerator: RandomPointGenerator

    init {
        val width = 16
        val height = 16

        val cy = height / 2

        val snake =
            setOf(
                Point(x = 0, y = cy),
                Point(x = 1, y = cy),
                Point(x = 2, y = cy),
                Point(x = 3, y = cy),
                Point(x = 4, y = cy)
            )

        val grid =
            List(height) { y ->
                List(width) { x ->
                    Point(x = x, y = y)
                }
            }

        randomPointGenerator = RandomPointGenerator()

        grid.forEach { row ->
            row.forEach(randomPointGenerator::free)
        }

        snake.forEach(randomPointGenerator::occupy)

        _board =
            mutableStateOf(
                Board(
                    snake = Snake(points = snake, head = snake.last()),
                    grid = grid,
                    cells = grid.flatten().toSet(),
                    direction = Direction.RIGHT,
                    food = randomPointGenerator.generate()
                )
            )
    }

    override val board: State<Board> = _board

    override fun step() {
        update {
            val newSnake = snake?.step(direction = direction, food = food, cells = cells)
            snake?.points?.forEach(randomPointGenerator::free)
            newSnake?.points?.forEach(randomPointGenerator::occupy)

            copy(
                snake = newSnake,
                food = when {
                    newSnake == null -> null
                    newSnake.head == food -> randomPointGenerator.generate()
                    else -> food
                }
            )
        }
    }

    // TODO: Consider persistent collections
    private fun Snake.step(direction: Direction, food: Point?, cells: Set<Point>): Snake? {
        val newPoints = LinkedHashSet<Point>(points)
        val newHead = points.last().step(direction)

        if ((newHead in newPoints) || (newHead !in cells)) {
            return null
        }

        newPoints += newHead

        if (newHead != food) {
            newPoints -= points.first()
        }

        return copy(points = newPoints, head = newHead)
    }

    private fun Point.step(direction: Direction): Point =
        when (direction) {
            Direction.LEFT -> copy(x = x - 1)
            Direction.UP -> copy(y = y - 1)
            Direction.RIGHT -> copy(x = x + 1)
            Direction.DOWN -> copy(y = y + 1)
        }

    override fun setDirection(direction: Direction) {
        update {
            copy(
                direction = if (direction != this.direction.invert()) direction else this.direction
            )
        }
    }

    private inline fun update(func: Board.() -> Board) {
        _board.value = _board.value.func()
    }
}
