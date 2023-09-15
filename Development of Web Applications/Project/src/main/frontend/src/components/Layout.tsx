import {
    Link,
    Outlet, useNavigate,
} from "react-router-dom";
import * as React from "react";

import {createContext, useContext, useState} from "react";
import {AuthPopUp} from "./Auth/Components/AuthPopUp";
import {useAuthentication} from "./Auth/Components/AuthProvider";
import {Menu, MenuItem} from "@mui/material";
import {AlertContext, ClientLog} from "./ErrorHandling/ClientLog";

type AlertState = [
    alert: AlertContext,
    setAlert: (state: AlertContext) =>void,
]

type PopInState = [
    pop: boolean,
    setPop: (state: boolean) => void,
]

const PopUpContext = createContext<PopInState>([undefined, () => {} ])
const AlertContext = createContext<AlertState>([undefined, () => {} ])

export function Layout(){

    const [pop, setPop] = useState(false)
    const [alert, setAlert] = useState(undefined)
    const [userInfo, setUserInfo] = useAuthentication()

    return (
        <AlertContext.Provider value={[alert, setAlert]}>
            <PopUpContext.Provider value={[pop, setPop]}>
                <div className="animatedBackground">

                    <div className="navbar">
                        <div className="topnav">
                            <Link to="/">Home</Link>
                            <Link to="/leaderboard">Leaderboard</Link>
                            <Link to="/about">About</Link>
                            <Link to="/game">Play</Link>
                            {userInfo ? <ProfileButton/> : <LoginButton/>}
                        </div>
                    </div>
                    {alert ? <ClientLog context={{alert: alert.alert, message: alert.message}} onClose={()=>{setAlert(undefined)}}/> : null}
                    <div className='content'>
                        <Outlet/>
                    </div>
                </div>
                {pop ? <AuthPopUp/> : null}
            </PopUpContext.Provider>
        </AlertContext.Provider>
    )
}

export function usePopUpContext(): PopInState{
    return useContext(PopUpContext)
}

export function useAlertContext(): AlertState{
    return useContext(AlertContext)
}

export function LoginButton(){
    const [pop, setPop] = usePopUpContext()
    return(
        <a className="login-index" onClick={function () {setPop(!pop)}}>Login</a>
    )
}

export function ProfileButton(){
    const [userInfo, setUserInfo] = useAuthentication()
    const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
    const navigate = useNavigate()

    const handleMenu = (event: React.MouseEvent<HTMLElement>) => {
        setAnchorEl(event.currentTarget);
    };

    const handleClose = () => {
        setAnchorEl(null);
    };

    const handleClick = () => {
        handleClose()
        navigate('/me')
    }

    function onLogout() {
        const Cookies = require('js-cookie')

        localStorage.removeItem(userInfo.token)
        Cookies.remove('Authorization')
        setUserInfo(undefined)
        handleClose()
    }

    return (
        <div>
            <a
                className="login-index"
                aria-label="account of current user"
                aria-controls="menu-appbar"
                aria-haspopup="true"
                color="inherit"
                onClick={handleMenu}
            >
                {userInfo.username}
            </a>
            <Menu
                id="menu-appbar"
                anchorEl={anchorEl}
                anchorOrigin={{
                    vertical: 'top',
                    horizontal: 'right',
                }}
                keepMounted
                transformOrigin={{
                    vertical: 'top',
                    horizontal: 'right',
                }}
                open={Boolean(anchorEl)}
                onClose={handleClose}
            >
                <MenuItem onClick={handleClick}>Profile</MenuItem>
                <MenuItem onClick={onLogout} style={{color: "red"}}>Logout</MenuItem>
            </Menu>
        </div>
    )
}



