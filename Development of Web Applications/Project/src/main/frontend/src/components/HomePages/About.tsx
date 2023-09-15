import {useFetch} from "../../Fetch/useFetch";
import * as React from "react";
import {Link} from "react-router-dom";
import {LoadingPanel} from "../LoadingPanel";
import {UserCard} from "../UserCard";

export function About(prop: { uri: string }) {
    const [content, loading] = useFetch(prop.uri, {})
    if (content) {
        const developers = content.developers
        console.log(developers)

        return (
            <div className='content-container' style={{textAlign: 'center', width: 'fit-content'}}>
                <h1>About Us</h1>
                <div style={{display: "flex", flexDirection: "row", gap: '10px', justifyContent: "center"}}>
                    {developers.map((target, idx) => fillCard(target))}
                </div>
                <Link to={"/"}> go back</Link>
            </div>
        )
    }
    return (
        <LoadingPanel message={'Loading...'}/>
    )
}

function fillCard(input){
    return(
        <UserCard title={input.name} contents={[{label: 'Name', value: input.name}, {label: 'Number', value: input.number}, {label: 'email', value: input.publicMail}]}/>
    )
}