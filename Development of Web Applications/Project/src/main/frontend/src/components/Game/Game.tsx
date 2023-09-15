import React, {useEffect, useState} from "react";
import {SetUpGameScreen} from "./gameViews/SetUpGameScreen";
import {useParams} from "react-router-dom";
import {useFetch} from "../../Fetch/useFetch";
import {defaultFetch} from "../../Fetch/defaultFetch";
import {useAuthentication} from "../Auth/Components/AuthProvider";
import {Simulate} from "react-dom/test-utils";
import waiting = Simulate.waiting;
import {PlayGameScreen} from "./gameViews/PlayGameScreen";
import {LoadingPanel} from "../LoadingPanel";

export type settingsSchema = {
    boardX: number,
    boardY: number,
    fleetschema: {
        submarine: number,
        destroyer: number,
        cruiser: number,
        carrier: number,
        battleship: number
    },
    shotsperround: number,
    setupTime: number,
    roundtime: number
}

const DEFAULT_SETTINGS: settingsSchema = {
    boardX: 10,
    boardY: 10,
    fleetschema: {
        submarine: 1,
        destroyer: 1,
        cruiser: 1,
        carrier: 1,
        battleship: 1
    },
    shotsperround: 1,
    setupTime: 900000,
    roundtime: 300000
}

function fetchGameById(id: number) {


    return 1
}

export function Game({apiUri}: { apiUri: string }) {
    const id = Number(useParams().id)
    const [userInfo] = useAuthentication()
    const [game, setGame] = useState(undefined)
    const [getUpdateState, setGetUpdateState] = useState(false)
    const [loading, setLoading] = useState(true)

    useEffect(() => {
        let interval
        if (getUpdateState && game && !loading) {
            interval = setInterval(async () => {
                const response = await defaultFetch(
                    `/api/game/${id}`,
                    'GET',
                    {'content-type': 'application/vnd.siren+json', 'Authorization': `Bearer ${userInfo.token}`},
                    {}
                )


                console.log(response)
                if (response.properties.gameState != game.properties.gameState) {
                    console.log(`updating old state ${game.properties.gameState}`)
                    console.log(`with the new State ${response.properties.gameState}`)
                    setGame(response)
                }
            }, 1000)
        }
        return () => clearInterval(interval)
    }, [getUpdateState])

    useEffect(() => {
        let cancelled = false

        async function fetchGame() {

            const res = await defaultFetch(
                `/api/game/${id}`,
                "GET",
                {'content-type': 'application/vnd.siren+json', 'Authorization': `Bearer ${userInfo.token}`},
                null
            )
            if (!cancelled) {
                setGame(res)
                setLoading(false)
            }
        }

        if (!game) {
            setLoading(true)
            fetchGame()
            return () => {
                cancelled = true
            }
        }
    }, [])

    if (loading) {
        return (
            <p>...loading...</p>
        )
    }
    console.log(`rendering game with ${game.properties.gameState}`)
    switch (game.properties.gameState) {
        case "SP":
            return <SetUpGameScreen id={id} setState={setGame} setUpdate={setGetUpdateState}/>

        case 'R2':
            return <LoadingPanel message={'Waiting for opponent ready...'}/>
        case 'R1' :
            return <LoadingPanel message={'Waiting for opponent to place ships...'}/>
        case "PT1" :
            return (
                <PlayGameScreen id={id} setState={setGame} setUpdate={setGetUpdateState} state={game}
                                settings={DEFAULT_SETTINGS}/>
            )

        case  "PT2": {

            return (
                <PlayGameScreen id={id} setState={setGame} setUpdate={setGetUpdateState} state={game}
                                settings={DEFAULT_SETTINGS}/>
            )
        }

        case "P1W":
            return getWinsRes(game.properties,"P1W")
        case "P2W":
          return  getWinsRes(game.properties,"P2W")
    }

    function getWinsRes(properties, state) {
        console.log("rendering win ")
        console.log(properties)
        if (properties.player1 == userInfo.id) {
            if (state == "P1W") {
                return (<>
                    <p>' You won '</p>
                </>)
            } else return (<>
                <p>' You Lost '</p>
            </>)
        } else if (state == "P2W") {
            return (<>
                <p>' You won '</p>
            </>)
        } else return (<>
            <p>' You Lost '</p>
        </>)
    }

}

//function handleClick(i) {
//    const cells = gameState().cells.slice();
//    cells[i] = gameState().xIsNext ? "X" : "O"
//    setGameState({
//        cells: cells,
//        xIsNext: !gameState().xIsNext,
//        game: gameState().game,
//        gameSettings: gameState().gameSettings
//    })
//
//}
// <div className="game-board">
//     <Board
//         nrRows={gameState().gameSettings.boardX}
//         rowSize={gameState().gameSettings.boardY}
//         getCells={() => gameState().cells}
//         onClick={i => handleClick(i)}
//     />
// </div>
// handleClick(i) {
//     const history = this.state.history.slice(0, this.state.stepNumber + 1);
//     const current = history[history.length - 1];
//     const squares = current.squares.slice();
//     if (calculateWinner(squares) || squares[i]) {
//         return;
//     }
//     squares[i] = this.state.xIsNext ? "X" : "O";
//     this.setState({
//         history: history.concat([
//             {
//                 squares: squares
//             }
//         ]),
//         stepNumber: history.length,
//         xIsNext: !this.state.xIsNext
//     });
// }
//
// jumpTo(step) {
//     this.setState({
//         stepNumber: step,
//         xIsNext: (step % 2) === 0
//     });
// }
//
// render() {
//     const history = this.state.history;
//     const current = history[this.state.stepNumber];
//     const winner = calculateWinner(current.squares);
//
//     const moves = history.map((step, move) => {
//         const desc = move ?
//             'Go to move #' + move :
//             'Go to game start';
//         return (
//             <li key={move}>
//                 <button onClick={() => this.jumpTo(move)}>{desc}</button>
//             </li>
//         );
//     });
//
//     let status;
//     if (winner) {
//         status = "Winner: " + winner;
//     } else {
//         status = "Next player: " + (this.state.xIsNext ? "X" : "O");
//     }
//
//     return (
//         <div className="game">
//             <div className="game-board">
//                 <Board
//                     squares={current.squares}
//                     onClick={i => this.handleClick(i)}
//                 />
//             </div>
//             <div className="game-info">
//                 <div>{status}</div>
//                 <ol>{moves}</ol>
//             </div>
//         </div>
//     );
// }
