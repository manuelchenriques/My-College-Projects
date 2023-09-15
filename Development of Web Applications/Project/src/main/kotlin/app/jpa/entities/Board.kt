package app.jpa.entities

import app.gameDomain.Position
import app.gameDomain.Ship
import app.gameDomain.squares.Squares


class Board(val x: Int, val y: Int) {

    private val grid: ArrayList<Squares> = arrayListOf()

    fun getPosition(position: Position): Squares? = grid.firstOrNull(){it.pos == position}

    fun setPosition(position: Position, square: Squares){
        if (position.x > x || position.y > y) throw  IllegalArgumentException()

        val gridInPosition = grid.filter { it.pos == position }.singleOrNull()
        if (gridInPosition == null){
            grid.add(square)
            return
        }
        grid[grid.indexOf(gridInPosition)] = square
    }

    fun getFilteredGrid(): HashMap<Position, String> {
        val res = hashMapOf<Position,String>()

        val filteredGrid = grid.filter { it.type != Squares.Types.Ship }
        filteredGrid.forEach {
            res[it.pos] = it.type.name

        }
        return res
    }

    fun getGrid() = grid

    fun sunkShip(ship: Ship){

            grid.filter { it.type == Squares.Types.Hit && it.ship!!.id == ship.id }
            .forEach {  square ->
                val idx = grid.indexOf(square)
                grid[idx] = Squares(ship,square.pos,Squares.Types.Sunk)
            }
    }

}