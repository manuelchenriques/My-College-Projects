import React, {createContext, Dispatch, useContext, useState,} from "react"

type userInfo = {id: string, username: string, token: string }

type AuthnState = [
    userInfo: userInfo | undefined,
    setUsername: (state: userInfo | undefined) => void,
]

const AuthContext = createContext<AuthnState>([undefined, () => {} ])

export function AuthnProvider({ children }: { children: React.ReactNode }) {

    const Cookies = require('js-cookie');

    let info: userInfo = undefined
    const token = Cookies.get('Authorization')
    if (token){
        const InfoStored = JSON.parse(localStorage.getItem(token))
        info = {id: InfoStored.id, username: InfoStored.username, token: token}
    }
    const [userInfo, setUserInfo] = useState(info)

    return (
        <AuthContext.Provider value={[ userInfo, setUserInfo ]}>
            {children}
        </AuthContext.Provider>
    )
}

export function useAuthentication(): AuthnState {
    return useContext(AuthContext)
}
