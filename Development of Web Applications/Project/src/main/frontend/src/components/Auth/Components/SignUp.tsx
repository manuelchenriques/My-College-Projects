import React, {useEffect, useState} from "react";
import {useAuthentication} from "./AuthProvider";
import {useAlertContext, usePopUpContext} from "../../Layout";
import {createAccount, isPasswordSecure, login, requestAuthToken} from "../authentication";
import {useLocation, useNavigate} from "react-router-dom";
import {Input, InputAdornment, InputLabel} from "@mui/material";
import {AccountCircle} from "@mui/icons-material";
import LockIcon from '@mui/icons-material/LockPerson';
import {LoadingPanel} from "../../LoadingPanel";

export function SignUp(){

    const [inputs, setInputs] = useState({ username: "", password: "", repassword: ""})
    const [isSubmitting, setIsSubmitting] = useState(false)
    const [error, setError] = useState(undefined)
    const [loading, setLoading] = useState(false)
    const [content, setContent] = useState(undefined)

    const [userInfo, setUserInfo] = useAuthentication()
    const [alert, setAlert] = useAlertContext()
    const Cookies = require('js-cookie');

    const location = useLocation()
    const navigate = useNavigate()

    useEffect(()=>{
        let cancelled = false
        async function createUser(username: string, password: string){

            try {
                await createAccount(username, password)
                const response = await login(username, password)

                if (!cancelled){
                    setLoading(false)
                    setContent(response)
                }
            }catch (error) {
                setLoading(false)
                setIsSubmitting(false)
                handleError(error)
            }
        }

        if (userInfo){
            setAlert({alert: "warning", message: `You are already logged in!`})
            navigate(location.state?.from?.pathname || "/")
        }

        if (isSubmitting){
            setLoading(true)
            createUser(inputs.username, inputs.password)
            return () => {
                cancelled = true
            }
        }
    }, [isSubmitting, setContent])

    function handleError(error: {status: number, message: string}){
        switch (error.status) {
            case 400:
                setError('Invalid password! It must contain at least 9 digits, as well as majuscule and minuscule letters')
                break;
            case 409:
                setError('Username is already taken')
                break;
            default:
                setAlert({alert: "error", message: `ERROR: ${error.status} ${error.message}`})
                navigate(location.state?.from?.pathname || "/")
                break;
        }
    }

    function handleSubmit(ev: React.FormEvent<HTMLFormElement>) {
        ev.preventDefault()
        if (!isPasswordSecure(inputs.password) || inputs.password != inputs.repassword){
            setError("Invalid credentials!")
            return
        }
        setIsSubmitting(true)
    }

    function handleChange(ev) {
        const name = ev.target.name
        setInputs({ ...inputs, [name]: ev.currentTarget.value })
    }

    if (loading){
        return (
            <LoadingPanel message={'Creating account...'}/>
        )
    }else{
        if (content){
            const userID = content.id
            const userToken = content.token
            localStorage.setItem(userToken, JSON.stringify({id: userID, username: inputs.username}))
            setUserInfo({id: userID, username: inputs.username, token: userToken})
            //Cookies.set('AuthToken', userToken, {expires: 3, sameSite: 'strict'})
            setAlert({alert: "success", message: "User created!"})
            navigate(location.state?.from?.pathname || "/")
        }else {
            return (
                <div className='content-container'>
                    <div className='signUp-container'>
                        <h1><b>SignUp</b></h1><hr style={{width: '70%'}}/>
                        <form onSubmit={handleSubmit}>
                            {error}
                            <fieldset disabled={isSubmitting}>
                                <div>
                                    <InputLabel htmlFor="username">
                                        Username
                                    </InputLabel>
                                    <Input
                                        className='mui-input'
                                        error={!!error}
                                        id="username"
                                        required={true}
                                        name="username"
                                        onChange={handleChange}
                                        startAdornment={
                                            <InputAdornment position="start">
                                                <AccountCircle/>
                                            </InputAdornment>
                                        }
                                    />
                                </div>
                                <div>
                                    <InputLabel htmlFor="password">
                                        Password
                                    </InputLabel>
                                    <Input
                                        className='mui-input'
                                        error={!!error}
                                        id="password"
                                        type="password"
                                        required={true}
                                        name="password"
                                        onChange={handleChange}
                                        startAdornment={
                                            <InputAdornment position="start">
                                                <LockIcon/>
                                            </InputAdornment>
                                        }
                                    />
                                </div>
                                <div>
                                    <InputLabel htmlFor="repassword">
                                        Repeat Password
                                    </InputLabel>
                                    <Input
                                        className='mui-input'
                                        error={!!error}
                                        id="repassword"
                                        type="password"
                                        required={true}
                                        name="repassword"
                                        onChange={handleChange}
                                        startAdornment={
                                            <InputAdornment position="start">
                                                <LockIcon/>
                                            </InputAdornment>
                                        }
                                    />
                                </div>
                                <div>
                                    <button className='styled-button' type="submit">Submit</button>
                                </div>
                            </fieldset>
                        </form>
                    </div>
                </div>
            )
        }
    }
}