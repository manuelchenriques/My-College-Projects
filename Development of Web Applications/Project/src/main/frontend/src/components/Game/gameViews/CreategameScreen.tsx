import {useEffect, useState} from "react";
import {Button, TextField} from "@mui/material";
import {CheckedButton} from "../../CheckedButton";
import {useFetch} from "../../../Fetch/useFetch";
import * as React from "react";
import {useNavigate} from "react-router-dom";
import {useAuthentication} from "../../Auth/Components/AuthProvider";

export function CreateGameScreen() {
    const [userInfo, setUserInfo] = useAuthentication()

    const shipTypes = ["Carrier", "BattleShip", "Cruiser", "Submarine", "Destroyer"]

    const [boardX, setX] = useState(8)
    const [boardY, setY] = useState(8)
    const [nrOfShots, setNrOfShots] = useState(3)
    const [roundTime, setRoundTime] = useState(60)
    const [setupTime, setSetupTime] = useState(120)

    const navigate = useNavigate();
    return (
        <div>
            <div id="container" className="center">
                <h1> Create Custom Game </h1>
                <div className="flex-parent-element">
                    <div className="flex-child-element">
                        <div>
                            <TextField
                                id="outlined-number"
                                label="X"
                                type="number"
                                value={boardX}
                                onChange={e => {
                                    if (Number(e.target.value) >= 0) setX(Number(e.target.value))
                                }}
                                InputLabelProps={{
                                    shrink: true,
                                }}
                            />
                        </div>
                        <div>
                            <TextField
                                id="outlined-number"
                                label="Y"
                                type="number"
                                value={boardY}
                                onChange={e => {
                                    if (Number(e.target.value) >= 0) setY(Number(e.target.value))
                                }}
                                InputLabelProps={{
                                    shrink: true,
                                }}
                            />
                        </div>
                        <div>
                            <TextField
                                id="outlined-number"
                                label="number_of_shots"
                                type="number"
                                value={nrOfShots}
                                onChange={e => {
                                    if (Number(e.target.value) >= 0) setNrOfShots(Number(e.target.value))
                                }}
                                InputLabelProps={{
                                    shrink: true,
                                }}
                            />
                        </div>
                        <div>
                            <TextField

                                id="outlined-number"
                                label="round_time"
                                type="number"
                                value={roundTime}
                                onChange={e => {
                                    if (Number(e.target.value) >= 0) setRoundTime(Number(e.target.value))
                                }}
                                InputLabelProps={{
                                    shrink: true,
                                }}
                            />
                        </div>
                        <div>
                            <TextField
                                id="outlined-number"
                                label="setup_time"
                                type="number"
                                value={setupTime}
                                onChange={e => {
                                    if (Number(e.target.value) >= 0) setSetupTime(Number(e.target.value))
                                }}
                                InputLabelProps={{
                                    shrink: true,
                                }}
                            />
                        </div>
                    </div>
                    <div className="flex-child-element">
                        {
                            shipTypes.map(ship => (<CheckedButton key={ship} name={ship}/>))

                        }
                    </div>


                </div>
                <Button onClick={async () => {

                    const response = await fetch("http://localhost:8083/api/game", {
                        method: 'POST',
                        credentials: 'include',
                        headers: {'content-type': 'application/vnd.siren+json', 'Authorization': `Bearer ${userInfo.token}` },
                        body: JSON.stringify({
                            boardX: boardX,
                            boardY: boardY,
                            fleetschema: shipTypes.reduce((previousValue: {}, currentValue) => {
                                if (document.getElementById(currentValue + "_checkbox").getAttribute("value") == "true") {
                                    previousValue[currentValue] = document.getElementById(currentValue + "_textField").getAttribute("value")
                                }
                                return previousValue
                            }, {}),
                            shotsperround: nrOfShots,
                            setupTime: setupTime,
                            roundtime: roundTime
                        })
                    })
                    await response.json().then( res=> {
                        navigate(`/game/${res.properties.id}`,{state: { game: res }})
                    })

                }} variant="contained">Create</Button>
            </div>
        </div>


    );
}