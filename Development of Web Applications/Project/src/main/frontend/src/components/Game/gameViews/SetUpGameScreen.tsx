import React, {Dispatch, useCallback, useEffect, useReducer, useState} from "react";
import {Button, Grid, Switch} from "@mui/material";
import {useAuthentication} from "../../Auth/Components/AuthProvider";
import {useNavigate} from "react-router-dom";
import {gridStyle, sourceDivStyle, targetDivStyle, wrapperStyle} from "../Components/DragDropStyle";
import {handleDragOver, handleDragStart} from "../DragAndDrop";
import {useAlertContext} from "../../Layout";
import {defaultFetch} from "../../../Fetch/defaultFetch";
import {LoadingPanel} from "../../LoadingPanel";
import useUndoableState from "../../UndoableState";
// The state type for each target element
export type TargetState =
    | string        // the target element received the source element with the index provided by the number
    | undefined     // the target element is available to receive a source element
export type ShipSchema = { count: number, size: number, shipType: "Submarine" | "Destroyer" | "Carrier" | "Cruiser" | "Battleship" }

type Orientation = 'Right' | 'Down'

type Ship = {
    orientation: Orientation, x: number, y: number, shipType: "Submarine" | "Destroyer" | "Carrier" | "Cruiser" | "Battleship"
}

// The overall component state
type State = {
    placements: Array<Ship>
    sources: Array<ShipSchema>
    targets: Array<TargetState>
    orientation: 'Down' | 'Right'
}

type Action =
    { type: 'rollback', stateHistoryArray, setStateHistoryArray }
    | { type: 'drop', sourceIx: number, targetIx: number, setHistoryArray, historyArray }
    | { type: 'changeOrientation' } // the `sourceId` element was dropped on the `targetId` element


const SCHEMA: Array<ShipSchema> = [
    {shipType: "Submarine", size: 2, count: 1},
    {shipType: "Destroyer", size: 2, count: 1},
    {shipType: "Carrier", size: 5, count: 1},
    {shipType: "Cruiser", size: 3, count: 1},
    {shipType: "Battleship", size: 4, count: 1},
]

const INITIAL_STATE: State = {
    placements: [],
    sources: SCHEMA,
    targets: new Array(10 * 10).fill(undefined),
    orientation: 'Right'
}

export function SetUpGameScreen(props: { id: number, setState: Dispatch<any>, setUpdate: Dispatch<any> }) {
    const [state, dispatch] = useReducer(reduce, INITIAL_STATE)
    const [userInfo, setUserInfo] = useAuthentication()
    const [alert, setAlert] = useAlertContext()
    const [awaiting, setAwaiting] = useState(false)
    const [stateHistory, setStateHistory] = useState(Array<State>(INITIAL_STATE))
    console.log("initial state ")
    console.log(stateHistory)

    // Handler for the `drop` event, which dispatches a `drop` event to the reducer managing state.
    const handleDrop = useCallback(function handleDrop(event: React.DragEvent<HTMLDivElement>) {
        // collect information about the source and the target
        const sourceIx = parseInt(event.dataTransfer.getData("text/plain"))
        const targetIx = parseInt(event.currentTarget.attributes.getNamedItem('data-ix').textContent)

        // dispatch a `drop` action to the reducer managing state
        dispatch({
            type: 'drop',
            sourceIx: sourceIx,
            targetIx: targetIx,
            setHistoryArray: setStateHistory,
            historyArray: stateHistory,
        })
    }, [dispatch, setStateHistory, stateHistory])

    function onChangeSwitch(e) {
        dispatch({type: 'changeOrientation'})
        console.log(state.orientation)
    }

    function undoOnClick() {
        if (stateHistory.length > 0) {
            dispatch({type: 'rollback', stateHistoryArray: stateHistory, setStateHistoryArray: setStateHistory})
        }
    }

    async function onClickButton() {
        if (!state.sources.every(value => value.count == 0)) {
            setAlert({alert: 'warning', message: 'You must place all ships before proceeding!'})
        } else {
            const response = await defaultFetch(
                `/api/game/${props.id}/ship`,
                'POST',
                {'content-type': 'application/vnd.siren+json', 'Authorization': `Bearer ${userInfo.token}`},
                {inputs: state.placements}
            )
            console.log(`response 1 `)
            console.log(response)
            const response2 = await defaultFetch(
                `/api/game/${props.id}/ready`,
                'POST',
                {'content-type': 'application/vnd.siren+json', 'Authorization': `Bearer ${userInfo.token}`},
                {}
            )
            console.log(`response 2`)
            console.log(response2)
            props.setState(response2)
            props.setUpdate(true)
        }

    }

    function createSourceElement(index: number, schema: ShipSchema) {
        return (
            <div
                key={index}
                style={sourceDivStyle(schema, 1, index + 1)}
                draggable={schema.count != 0}
                onDragStart={handleDragStart}
                data-ix={index}
            >{schema.shipType} {schema.size}</div>
        )
    }

    function createTargetElement(index: number, target: string) {

        return (
            <div key={index}
                 style={targetDivStyle(target, index % 10 + 1, index / 10 + 1)}
                 onDragOver={target === undefined ? handleDragOver : undefined}
                 onDrop={target === undefined ? handleDrop : undefined}
                 data-ix={index}>
                {target}
            </div>
        )
    }

    if (awaiting) {
        return (
            <LoadingPanel message={'Waiting for opponent to place ships...'}/>
        )
    }


    return (
        // a source `div` is draggable only if it is 'available'
        // a source `div` is a drop target only if is available, i.e., its value is still undefined
        <div className='gameSet-div'>
            <div>
                <h2>Ships</h2>
                <div style={wrapperStyle}>
                    {state.sources.map((source, ix) => createSourceElement(ix, source))}
                </div>

                <Grid component="label" container alignItems="center" spacing={1}>
                    <Grid item>Horizontal</Grid>
                    <Grid item>
                        <Switch
                            onChange={onChangeSwitch} // relevant method to handle your change
                        />
                    </Grid>
                    <Grid item>Vertical</Grid>
                </Grid>
                <Button onClick={onClickButton} variant="contained">Place Ships</Button>
                <Button disabled={stateHistory == null || stateHistory.length == 1} onClick={undoOnClick}
                        variant="contained">Undo Placement</Button>
            </div>

            <div>
                <h2>Board</h2>
                <div className='grid'>
                    {state.targets.map((target, ix) => createTargetElement(ix, target))}
                </div>
            </div>
        </div>
    )
}

// Helper function to compute an array `[0..len-1]`
function range(len: number) {

    return Array(len).map((val, idx) => idx)
}

function get(board, x, y) {
    return board[x + y * 10]
}

function reduce(state: State, action: Action): State {
    switch (action.type) {
        case 'drop': {
            const shipTobePlaced = state.sources.slice()[action.sourceIx]
            const x = action.targetIx % 10
            const y = (action.targetIx - x) / 10
            const placements = state.placements.slice()
            const board = tryToPlace(
                x,
                y,
                state.targets.slice(),
                shipTobePlaced,
                state.orientation
            )
            let updatedState
            if (board) {
                placements.push({
                    orientation: state.orientation,
                    x: x,
                    y: y,
                    shipType: shipTobePlaced.shipType
                })
                let holder2 =state.sources.slice()
                if (holder2[action.sourceIx].count > 0) {
                    holder2[action.sourceIx] = {count: holder2[action.sourceIx].count-1, shipType: holder2[action.sourceIx].shipType, size: holder2[action.sourceIx].size}
                }

                 updatedState = {
                    placements: placements,
                    targets: board ,
                     sources :holder2,
                    // sources: state.sources.slice().map((value, ix) => {
                    //     if (ix == action.sourceIx && board && value.count > 0) {
                    //         value.count = --value.count
                    //
                    //         return value
                    //     } else return value
                    // }),
                    orientation: state.orientation
                }

                let holder = action.historyArray.slice()
                holder.push(updatedState)
                action.setHistoryArray(holder)
            }else return state

            return updatedState
        }
        case "changeOrientation": {
            return {
                sources: state.sources,
                targets: state.targets,
                placements: state.placements,
                orientation: state.orientation == 'Right' ? 'Down' : 'Right'
            }
        }
        case "rollback": {
            let holder = action.stateHistoryArray.slice()
            holder.pop()
            let newState = holder[holder.length -1]
            action.setStateHistoryArray(holder)
            return {
                sources: newState.sources,
                targets: newState.targets,
                placements: newState.placements,
                orientation: newState.orientation
            }
        }

        default:
            // unknown action, type system ensures this cannot happen
            return state
    }
}

function tryToPlace(
    x: number,
    y: number,
    board: string[],
    ship: ShipSchema,
    orientation: Orientation
): string[] {
    let result = board

    for (let i = 0; i < ship.size; i++) {
        const currentPosition = {x: x, y: y}
        orientation == 'Right' ? currentPosition.x = currentPosition.x + i : currentPosition.y = currentPosition.y + i
        if (currentPosition.x > 9 || currentPosition.y > 9 || get(result, currentPosition.x, currentPosition.y) != undefined) {
            console.log('FAILED')
            result = null
            break
        }

        result[currentPosition.y * 10 + currentPosition.x] = ship.shipType
    }

    return result
}
