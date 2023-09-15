import React, {useEffect, useState} from "react";
import {LoadingPanel} from "../LoadingPanel";
import {useNavigate} from "react-router-dom";
import {useAuthentication} from "../Auth/Components/AuthProvider";
import {useAlertContext} from "../Layout";
import {getUserInfo} from "../Auth/authentication";
import {useFetch} from "../../Fetch/useFetch";
import {Box} from "@mui/material";

export function UserDetails(){
    const [userInfo, setUserInfo] = useAuthentication()

    const [content, loading] = useFetch('/me', {headers: {'content-type': 'application/vnd.siren+json', 'Authorization': `Bearer ${userInfo.token}`}})

    if(content){
        const games = content.entities[0].properties.gamesPlayed
        const wins = content.entities[0].properties.gamesWon
        const winrate = (games == 0 && wins == 0)? 0 : (wins/games)*100


        return (
            <div className='content-container' style={{textAlign:'center'}}>
                <h1>{content.properties.username}</h1>
                <hr/>
                    <p>Games: {games}</p>
                    <p>Wins: {wins}</p>
                    <p>Win rate: {winrate}%</p>
            </div>
        )
    }else return <LoadingPanel message={'Loading...'}/>
}