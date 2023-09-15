
import React from "react";

export type PositionStatus = 'Hit' | 'Miss' | 'Sunk' | 'Ship'

function Cell(props:{index: number, onClick: ()=>void, state: PositionStatus}) {
    return (
        <div className={'cell ' + props.index} style={cellStyle(props.index, props.state)} onClick={(props.onClick)}> {props.state}</div>
    )
}
//onClick={props.onClick}>
function cellStyle(index: number ,state: PositionStatus): React.CSSProperties{
    const row = Math.floor(index/10 + 1)
    const collumn = Math.floor(index % 10 + 1)
    let color

    switch (state){
        case undefined:
            color = ''
            break;
        case 'Hit':
            color = 'red'
            break;
        case 'Miss':
            color = 'grey'
            break;
        case 'Sunk':
            color = 'brown'
            break;
        case 'Ship' :
            color = '#5454d9'
            break
    }


    return {
        gridArea: `${row} / ${collumn}`,
        border: "solid",
        width: "50px",
        height: "50px",
        backgroundColor: color,
    }

}
export default Cell