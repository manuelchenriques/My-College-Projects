import React from "react";
import logo from "../../assets/battleships_white_logo.png"
import {useAlertContext} from "../Layout";

export function Home(){

    return(
        <div className="container">
            <div id="container" className="center">
                <h1 className="center">BATTLESHIPS</h1>
                <img className="center" src={logo} id="logo" alt="Game logo"/>
            </div>
        </div>
    )
}