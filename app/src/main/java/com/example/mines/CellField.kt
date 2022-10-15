package com.example.mines

import Cell

class CellField(private val amountOfMines: Int) {

    private val sizeX = 7
    private val sizeY = 7
    private var isGenerated = false
    private var amountOfOpened = 0

    enum class GameState {
        CONTINUE, OVER, WIN
    }

    private val newField: Array<Array<Cell>> = Array(sizeX
    ) { Array(sizeY) { Cell() } }

    fun getCell(x: Int, y: Int): Cell {
        return newField[x][y]
    }


    private fun getNeighbours(i: Int, j: Int): MutableList<Cell> {
        val neighbours: MutableList<Cell> = mutableListOf()
        if ((i in 1 until (sizeX - 1)) &&
            (j in 1 until (sizeY - 1)))
        {
            neighbours += newField[i-1][j-1]
            neighbours += newField[i][j-1]
            neighbours += newField[i+1][j-1]
            neighbours += newField[i-1][j]
            neighbours += newField[i+1][j]
            neighbours += newField[i-1][j+1]
            neighbours += newField[i][j+1]
            neighbours += newField[i+1][j+1]
            return neighbours
        } else if ((i == 0) &&
            (j in 1 until (sizeY - 1)))
        {
            neighbours += newField[i][j-1]
            neighbours += newField[i+1][j-1]
            neighbours += newField[i+1][j]
            neighbours += newField[i][j+1]
            neighbours += newField[i+1][j+1]
            return neighbours
        } else if ((i in 1 until (sizeX - 1)) &&
            (j == 0))
        {
            neighbours += newField[i-1][j]
            neighbours += newField[i+1][j]
            neighbours += newField[i-1][j+1]
            neighbours += newField[i][j+1]
            neighbours += newField[i+1][j+1]
            return neighbours
        } else if ((i == sizeX - 1) &&
            (j in 1 until (sizeY - 1)))
        {
            neighbours += newField[i-1][j-1]
            neighbours += newField[i][j-1]
            neighbours += newField[i-1][j]
            neighbours += newField[i-1][j+1]
            neighbours += newField[i][j+1]
            return neighbours
        } else if ((i in 1 until (sizeX - 1)) &&
            (j == sizeY - 1))
        {
            neighbours += newField[i-1][j-1]
            neighbours += newField[i][j-1]
            neighbours += newField[i+1][j-1]
            neighbours += newField[i-1][j]
            neighbours += newField[i+1][j]
            return neighbours
        } else if ((i == 0) && (j == 0)) {
            neighbours += newField[i+1][j]
            neighbours += newField[i][j+1]
            neighbours += newField[i+1][j+1]
            return neighbours
        } else if ((i == sizeX - 1) && (j == sizeY - 1)) {
            neighbours += newField[i-1][j-1]
            neighbours += newField[i][j-1]
            neighbours += newField[i-1][j]
            return neighbours
        } else if ((i == 0) && (j == sizeY - 1)) {
            neighbours += newField[i][j-1]
            neighbours += newField[i+1][j-1]
            neighbours += newField[i+1][j]
            return neighbours
        } else if ((i == sizeX - 1) && (j == 0)) {
            neighbours += newField[i-1][j]
            neighbours += newField[i-1][j+1]
            neighbours += newField[i][j+1]
            return neighbours
        }
        return neighbours
    }

    private fun fillCellsWithNumbers() {
        for (i in 0 until sizeX) {
            for (j in 0 until sizeY) {
                if (newField[i][j].mine) {
                    getNeighbours(i, j).forEach {
                        it.amount++
                    }
                }
            }
        }
    }

    private fun generateCellsWithMines(startX: Int, startY: Int): IntArray {
        val cellsWithMines = IntArray(amountOfMines
        ) {-1}
        for (i in 0 until amountOfMines) {
            var n = (0 until sizeX * sizeY).random()
            while ((cellsWithMines.contains(n)) or
                (n == (startX - 1) * sizeX + (startY - 1)) or
                (n == (startX - 1) * sizeX + startY) or
                (n == (startX - 1) * sizeX + (startY + 1)) or
                (n == (startX) * sizeX + (startY - 1)) or
                (n == (startX) * sizeX + startY) or
                (n == (startX) * sizeX + (startY + 1)) or
                (n == (startX + 1) * sizeX + (startY - 1)) or
                (n == (startX + 1) * sizeX + startY) or
                (n == (startX + 1) * sizeX + (startY + 1))    )
            {
                n = (0 until sizeX * sizeY).random()
            }
            cellsWithMines[i] = n
        }
        return cellsWithMines
    }

    private fun openList(x: Int, y : Int,
                         ml: MutableList<Pair<Int, Int>>): GameState
    {
        if (newField[x][y].isOpened) {
            return GameState.CONTINUE
        }
        if (newField[x][y].mine) {
            newField[x][y].isOpened = true
            ml += Pair(x, y)
            amountOfOpened++
            return GameState.OVER
        } else {
            if (newField[x][y].amount > 0) {
                newField[x][y].isOpened = true
                ml += Pair(x, y)
                amountOfOpened++
            } else {
                newField[x][y].isOpened = true
                ml += Pair(x, y)
                amountOfOpened++
                getNeighbours(x, y).forEach {
                    openList(it.x, it.y, ml)
                }
            }
        }
        return GameState.CONTINUE
    }

    private fun generateField(startX: Int, startY: Int):
            MutableList<Pair<Int, Int>>
    {
        for (i in 0 until sizeX) {
            for (j in 0 until sizeY) {
                newField[i][j].x = i
                newField[i][j].y = j
            }
        }

        val cellsWithMines = generateCellsWithMines(startX, startY)
        cellsWithMines.forEach {
            newField[it / sizeX][it % sizeY].mine = true
        }

        fillCellsWithNumbers()
        val res : MutableList<Pair<Int, Int>> = mutableListOf()
        openList(startX, startY, res)
        return res
    }

    private fun gameCheck(): GameState {
        return if (amountOfOpened == sizeX * sizeY - amountOfMines) {
            GameState.WIN
        } else {
            GameState.CONTINUE
        }
    }

    fun open(x: Int, y: Int): Pair<GameState, MutableList<Pair<Int, Int>>> {
        if (!isGenerated) {
            isGenerated = true
            val res = generateField(x, y)
            return Pair(GameState.CONTINUE, res)
        }
        val res : MutableList<Pair<Int, Int>> = mutableListOf()
        val st = openList(x, y, res)
        return if (st == GameState.OVER) {
            Pair(GameState.OVER, res)
        } else {
            Pair(gameCheck(), res)
        }
    }

    fun clear() {
        isGenerated = false
        amountOfOpened = 0
        for (i in 0 until sizeX) {
            for (j in 0 until sizeY) {
                newField[i][j].mine = false
                newField[i][j].isOpened = false
                newField[i][j].amount = 0
            }
        }
    }

}
