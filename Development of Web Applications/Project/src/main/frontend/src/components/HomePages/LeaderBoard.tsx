import {useFetch} from "../../Fetch/useFetch";
import * as React from "react";
import {useEffect, useState} from "react";
import {Button} from "@mui/material";
import {LoadingPanel} from "../LoadingPanel";
import {LeaderboardTable} from "../LeaderboardTable";

export type UserDetails={
    user: string,
    gamesPlayed: number,
    gamesWon: number
}

export function LeaderBoard(prop: { uri: string }) {
    const [searchParams, setSearchParams] = useState('/leaderboard?orderBy=Wins&top=6&idx=0');
    const [content, loading] = useFetch(searchParams, {headers:{'content-type': 'application/vnd.siren+json'}})

    if (content){
        const users = Array<UserDetails>()
        content.properties.stats.forEach(entry =>{
            users.push({user: entry.username, gamesPlayed: entry.gamesPlayed, gamesWon: entry.gamesWon})
        })
        return (
            <div className='content-container' style={{display: 'flex', textAlign: 'center', alignItems: 'center', flexDirection: 'column'}}>
                <h1>Leaderboard</h1>
                <LeaderboardTable data={users}/>
                <div style={{marginTop: '10px'}}>
                    <Button disabled={!hasPrev(content)} variant="contained" onClick={() => {
                            setSearchParams(content.links.find((elem) => elem.rel.includes("previous")).href)
                    }}>Previous</Button>

                    <Button disabled={!hasNext(content)} variant="contained" onClick={() => {
                            setSearchParams(content.links.find((elem) => elem.rel.includes("next")).href)
                    }}>Next</Button>
                </div>
            </div>
        )

    }else return (<LoadingPanel message='Loading...'/>)

    function hasPrev(content): boolean {
        return content.links.find((elem) => elem.rel.includes("previous")) != undefined
    }

    function hasNext(content): boolean {
        return content.links.find((elem) => elem.rel.includes("next")) != undefined
    }
}