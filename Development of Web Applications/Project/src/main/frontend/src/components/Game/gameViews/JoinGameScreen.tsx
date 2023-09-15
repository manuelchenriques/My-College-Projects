import {useAuthentication} from "../../Auth/Components/AuthProvider";
import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import * as React from "react";
import {defaultFetch} from "../../../Fetch/defaultFetch";
import {LoadingPanel} from "../../LoadingPanel";

export function JoinGameScreen() {
    const [userInfo, setUserInfo] = useAuthentication()
    const navigate = useNavigate();

    const [game, setGame] = useState(undefined)
    const [waiting, setWaiting] = useState(true)

    useEffect(()=>{
        let cancelled = false
        async function joinGame() {
            try {
                const response = await defaultFetch(
                    '/api/game/fast',
                    'POST',
                    {'content-type': 'application/vnd.siren+json', 'Authorization': `Bearer ${userInfo.token}` },
                    { boardX: 10,
                        boardY: 10,
                        fleetschema:{
                        submarine: 1,
                            destroyer:1,
                            carrier:1,
                            cruiser:1,
                            battleship:1
                        } ,
                        shotsperround: 1,
                        setupTime: 900000,
                        roundtime: 300000}
                )
                await new Promise(r => setTimeout(r, 2000));
                if(!cancelled){
                    setGame(response)
                }
            }catch (error){
                console.log(error)
            }
        }

        if(!game){
            joinGame()
            return () => {
                cancelled = true
            }
        }
    })

    useEffect(()=>{
        if (game && waiting){

            let interval = setInterval(async ()=>{
                const response = await defaultFetch(
                    `/api/game/${game.properties.id}`,
                    'GET',
                    {'content-type': 'application/vnd.siren+json', 'Authorization': `Bearer ${userInfo.token}` },
                    {}
                )
                console.log(response)
                if (response.properties.gameState == 'SP'){
                    setGame(response)
                    setWaiting(false)
                }
            }, 1000)

            return () => clearInterval(interval)
        }

        if (!waiting){
            console.log('FOUND!!!')
        }
    })

    if (!game){
        return (
            <LoadingPanel message={'JOINING GAME...'}/>
        );
    }

    if (waiting){
        return (
            <LoadingPanel message={'WAITING FOR PLAYERS...'}/>
        );
    }
    navigate(`${game.links.find(e => e.rel.find(subel=> subel =="self")!= undefined).href }`)

}