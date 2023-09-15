import React, {Dispatch} from "react";
import Cell from "./Cell";
import {gridStyle} from "./DragDropStyle";

function Board(props:{values: Array<string>,updateBoard: Dispatch<any>,plays: {plays: Array<{x:number,y:number}> } | undefined,updateShots: Dispatch<any>,owner, onClick: (pos: number, board :Array<string>,updateBoard, shots,updateShots)=> void }) {

    function renderCell(i, value) {
        return (<Cell key ={props.owner+ `-${i}`} index={i} state={value} onClick={() => props.onClick(i,props.values,props.updateBoard,props.plays,props.updateShots)}/>)
    }


    return (
        <div className="grid">
            {props.values.map((value,idx)=>renderCell(idx, value))}
        </div>
    )
}

export default Board