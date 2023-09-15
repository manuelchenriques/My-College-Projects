

// The function to compute the CSS style for a source `div`
import {ShipSchema, TargetState} from "../gameViews/SetUpGameScreen";

export function sourceDivStyle(sourceState: ShipSchema, column: number, row: number): React.CSSProperties {
    return {
        gridColumn: column,
        gridRow: row,
        border: "solid",
        width: "25px",
        height: "25px",
        borderColor: sourceState.count > 0 ? 'green' : 'red',
    }
}

export function targetDivStyle(targetState: TargetState, column: number, row: number): React.CSSProperties {
    return {
        gridColumn: column,
        gridRow: row,
        border: "solid",
        width: "50px",
        height: "50px",
        backgroundColor: targetState === undefined ? '' : 'gray'
    }
}

export const wrapperStyle: React.CSSProperties = {
    display: 'grid',
    gap: "5px",
    gridAutoRows: "minmax(25px, auto)",
}

export const gridStyle: React.CSSProperties = {
    display: 'grid',
    gridTemplateColumns: "repeat(10, 56px)",
    gridAutoRows: "minmax(25px, auto)",
}