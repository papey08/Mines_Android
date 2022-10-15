package com.example.mines

import Cell
import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class GameActivity : Activity() {

    private fun fillDict(dict: IntArray) {
        dict[0] = R.id.cell_0_0
        dict[1] = R.id.cell_0_1
        dict[2] = R.id.cell_0_2
        dict[3] = R.id.cell_0_3
        dict[4] = R.id.cell_0_4
        dict[5] = R.id.cell_0_5
        dict[6] = R.id.cell_0_6
        dict[7] = R.id.cell_1_0
        dict[8] = R.id.cell_1_1
        dict[9] = R.id.cell_1_2
        dict[10] = R.id.cell_1_3
        dict[11] = R.id.cell_1_4
        dict[12] = R.id.cell_1_5
        dict[13] = R.id.cell_1_6
        dict[14] = R.id.cell_2_0
        dict[15] = R.id.cell_2_1
        dict[16] = R.id.cell_2_2
        dict[17] = R.id.cell_2_3
        dict[18] = R.id.cell_2_4
        dict[19] = R.id.cell_2_5
        dict[20] = R.id.cell_2_6
        dict[21] = R.id.cell_3_0
        dict[22] = R.id.cell_3_1
        dict[23] = R.id.cell_3_2
        dict[24] = R.id.cell_3_3
        dict[25] = R.id.cell_3_4
        dict[26] = R.id.cell_3_5
        dict[27] = R.id.cell_3_6
        dict[28] = R.id.cell_4_0
        dict[29] = R.id.cell_4_1
        dict[30] = R.id.cell_4_2
        dict[31] = R.id.cell_4_3
        dict[32] = R.id.cell_4_4
        dict[33] = R.id.cell_4_5
        dict[34] = R.id.cell_4_6
        dict[35] = R.id.cell_5_0
        dict[36] = R.id.cell_5_1
        dict[37] = R.id.cell_5_2
        dict[38] = R.id.cell_5_3
        dict[39] = R.id.cell_5_4
        dict[40] = R.id.cell_5_5
        dict[41] = R.id.cell_5_6
        dict[42] = R.id.cell_6_0
        dict[43] = R.id.cell_6_1
        dict[44] = R.id.cell_6_2
        dict[45] = R.id.cell_6_3
        dict[46] = R.id.cell_6_4
        dict[47] = R.id.cell_6_5
        dict[48] = R.id.cell_6_6
    }

    private fun updateButton(cell: Cell, button: Button) {
        if (cell.mine) {
            button.setBackgroundColor(Color.parseColor("#F44336"))
        } else if (cell.amount > 0) {
            button.text = cell.amount.toString()
            button.setBackgroundColor(Color.parseColor("#BCBBB7"))
        } else {
            button.setBackgroundColor(Color.parseColor("#BCBBB7"))
        }
    }

    private fun setDefaultButtons(sX: Int, sY: Int, idDict: IntArray) {
        for (i in 0 until sX) {
            for (j in 0 until sY) {
                val cellButton = findViewById<Button>(idDict[i * sX + j])
                cellButton.text = ""
                cellButton.setBackgroundColor(Color.parseColor("#FFA69F82"))
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun beginOfGame(sX: Int, sY: Int, idDict: IntArray,
                            field: CellField)
    {
        for (i in 0 until sX) {
            for (j in 0 until sY) {
                val cellButton = findViewById<Button>(idDict[i * sX + j])
                cellButton.setOnClickListener {
                    val (state, toChange) = field.open(i, j)
                    toChange.forEach {
                        val cellToChange =
                            findViewById<Button>(idDict[it.first * sX +
                                    it.second])

                        updateButton(field.getCell(it.first, it.second),
                            cellToChange)
                    }
                    if (state == CellField.GameState.WIN) {
                        val sign = findViewById<TextView>(R.id.textView2)
                        sign.text = "YOU WIN!!"
                        endOfGame(sX, sY, idDict, field)
                    } else if (state == CellField.GameState.OVER) {
                        val sign = findViewById<TextView>(R.id.textView2)
                        sign.text = "GAME OVER!!"
                        endOfGame(sX, sY, idDict, field)
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun endOfGame(sX: Int, sY: Int, idDict: IntArray,
                          field: CellField)
    {
        for (i in 0 until sX) {
            for (j in 0 until sY) {
                val cellButton = findViewById<Button>(idDict[i * sX + j])
                cellButton.setOnClickListener {
                    setDefaultButtons(sX, sY, idDict)
                    field.clear()
                    val sign = findViewById<TextView>(R.id.textView2)
                    sign.text = "CLICK THE CELL!!"
                    beginOfGame(sX, sY, idDict, field)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_activity)

        val sX = 7
        val sY = 7

        val idDict = IntArray(sX * sY) { 0 }

        fillDict(idDict)

        val amount = intent.getStringExtra("tagFromAmountToGame")?.toInt()


        val field = amount?.let { CellField(it) }

        if (field != null) {
            beginOfGame(sX, sY, idDict, field)
        }
    }

}

