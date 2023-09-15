import {Dispatch, useEffect, useState} from "react";
import {defaultFetch} from "../../../Fetch/defaultFetch";
import * as React from "react";
import {useAuthentication} from "../../Auth/Components/AuthProvider";
import {useLocation, useNavigate, useParams} from "react-router-dom";
import Board from "../Components/Board";
import {settingsSchema} from "../Game";
import {Button} from "@mui/material";
import {useAlertContext} from "../../Layout";
import {FleetTable, Row} from "../Components/FleetTable";

export function PlayGameScreen(props: { id: number, state: any, setState: Dispatch<any>, setUpdate: Dispatch<any>, settings: settingsSchema }) {
    let maxNrShots = 1
    const [userInfo, set] = useAuthentication()
    console.log("rendedrin play game screen, calling geBoard with ")
    console.log(props.state.properties.myBoard)
    console.log("rendedrin play game screen, calling oppBoard with ")
    console.log(props.state.properties.opponentBoard)
    const [myBoard, setMyBoard] = useState(getBoard(props.state.properties.myBoard))
    const [otherBoard, setOtherBoard] = useState(getOpponent(props.state.properties.opponentBoard))
    const [shots, setShots] = useState({plays: Array<{ x: number, y: number }>(0)})
    const [alert, setAlert] = useAlertContext()
    function updateBoard(board, elements, update) {
        let holder = board
        if (elements.length > 0) elements.forEach((elem) => {
            if(elem.type== "Ship")
                holder[elem.pos.y * 10 + elem.pos.x] = "Ship"
            else holder[elem.pos.y * 10 + elem.pos.x] = elem.type
        })
        update(holder)
    }
    function updateOpponentBoard(board, elements, update) {
        let holder = board
        console.log("updating opp board")
        if (Object.keys(elements).length > 0){
            (Object.keys(elements) as (keyof typeof elements)[]).forEach((key,idx)=>{
                let x
                if (typeof key === "string") {
                    x = Number(key.substring(11, 12))
                }
                let y
                if (typeof key === "string") {
                    y = Number(key.substring(16, 17))
                }
                console.log(` opponet board has ${elements[key]} in x: ${x} , y:${y} `)
                holder[y * 10 + x] = elements[key]
            })
        }
        update(holder)
    }
    function getBoard(elements) {
        let holder = Array(props.settings.boardY * props.settings.boardX).fill(" ")

        if (elements.length > 0) elements.forEach((elem) => {
            console.log(elem.type)
            if(elem.type== "Ship")
                holder[elem.pos.y * 10 + elem.pos.x] = "Ship"
            else holder[elem.pos.y * 10 + elem.pos.x] = elem.type
        })
        return holder
    }
    function getOpponent(elements) {
        console.log("Getting  opp board")
        let holder = Array(props.settings.boardY * props.settings.boardX).fill(" ")
        console.log(Object.keys(elements))
        console.log("---")
        console.log(Object.keys(elements) as (keyof typeof elements)[])
        if (Object.keys(elements).length > 0){
            (Object.keys(elements) as (keyof typeof elements)[]).forEach((key,idx)=>{
                let x
                if (typeof key === "string") {
                    x = Number(key.substring(11, 12))
                }
                let y
                if (typeof key === "string") {
                    y = Number(key.substring(16, 17))
                }
                console.log(` opponet board has ${elements[key]} in x: ${x} , y:${y} `)
                holder[y * 10 + x] = elements[key]
            })
        }
        return holder
    }

    function getFleetState(fleet: 'myFleet' | 'opponentFleet'): Array<Row>{
        const result = Array<Row>()
        const obj = (fleet == 'myFleet')? props.state.properties.myFleet: props.state.properties.opponentFleet

        obj.forEach(element=>{
            result.push({
                shipName: element.type.name,
                shipNumber: element.count
            })
        })

        return result
    }


    function itsMyTurn(id: any, properties) {
        if (properties.player2 == id) {
            return properties.gameState == "PT2";
        } else return properties.gameState == "PT1"
    }

    useEffect(() => {
        console.log("in use effect")
        updateBoard(myBoard, props.state.properties.myBoard, setMyBoard)
        updateOpponentBoard(otherBoard, props.state.properties.opponentBoard, setOtherBoard)
        if (itsMyTurn(userInfo.id, props.state.properties)) props.setUpdate(false)
        else props.setUpdate(true)
        console.log("rendering Board with")
        console.log(myBoard)
        console.log("rendering otherBoard with")
        console.log(otherBoard)
    }, [props.state])



    function myTurnFunction(pos: number, board: Array<string>, updateBoard, shots, updateShots) {
        let holder = board.slice()
        console.log(`resolving on click om pos ${pos}`)
        console.log(` read ${holder[pos]} and nr of shots placed is ${shots.plays.length}`)
        if (holder[pos] == "Shot") {
            holder[pos] = " "
            updateShots({plays: shots.plays.filter(shot => !(shot.x == Math.floor(pos % 10) && shot.y == Math.floor(pos / 10)))})
        }
        if (holder[pos] == " " && shots.plays.length < maxNrShots) {
            console.log("placing shot")
            let updater = shots.plays
            updater.push({x: Math.floor(pos % 10), y: Math.floor(pos / 10)})
            console.log(shots)
            holder[pos] = "Shot"
            updateShots({plays: updater})
        }
        console.log(`shots object :`)
        console.log(shots)
        updateBoard(holder)
    }

    async function submitShot(){
         if (shots.plays.length == maxNrShots) {
            const response = await defaultFetch(
                `/api/game/${props.id}/play`,
                'POST',
                {'content-type': 'application/vnd.siren+json', 'Authorization': `Bearer ${userInfo.token}` },
                shots
            )
              setShots({plays: Array<{ x: number, y: number }>(0)})
            props.setState(response)
         }else setAlert({alert: 'warning', message: 'You must take a shot before proceeding!'})
    }


    return (
        <div className='game-container-div'>
            <h1>Play Game Screen</h1>
            <div className='game-content-div'>
                <div>
                    <h2>Opponent Board</h2>
                    <Board owner={"opponent"}
                           onClick={itsMyTurn(userInfo.id,props.state.properties) ? myTurnFunction : () => {
                           }} values={otherBoard} plays={shots} updateBoard={setOtherBoard} updateShots={setShots}/>
                </div>
                <div>
                    <h2>My Board</h2>
                    <Board owner={"mine"} onClick={() => {
                    }} values={myBoard} plays={undefined} updateBoard={undefined} updateShots={undefined}/>
                </div>
                <div style={{display: "flex", flexDirection: "column", justifyContent: "center", gap: '2rem', marginTop: '2rem'}}>
                    <FleetTable tableName="My Fleet" data={getFleetState('myFleet')}/>
                    <FleetTable tableName="Opponents Fleet" data={getFleetState('opponentFleet')}/>
                </div>
            </div>
            <Button disabled = {!itsMyTurn(userInfo.id, props.state.properties)} variant="contained" onClick={ submitShot }>Submit Shot</Button>
        </div>
    )
}