import React, {useEffect} from 'react'
import {
    useState
} from 'react'
import { useLocation, useNavigate } from 'react-router-dom'
import {useAuthentication} from "./AuthProvider";
import {useAlertContext, usePopUpContext} from "../../Layout";
import {isPasswordSecure, login, requestAuthToken} from "../authentication";
import {Box, FormControl, Input, InputAdornment, InputLabel} from "@mui/material";
import {AccountCircle} from "@mui/icons-material";
import LockIcon from '@mui/icons-material/LockPerson';
import {LoadingPanel} from "../../LoadingPanel";

export function Login(): React.ReactElement {

    const [inputs, setInputs] = useState({ username: "", password: "" })
    const [isSubmitting, setIsSubmitting] = useState(false)
    const [error, setError] = useState(undefined)
    const [loading, setLoading] = useState(false)
    const [content, setContent] = useState(undefined)

    const [userInfo, setUserInfo] = useAuthentication()
    const [pop, setPop] = usePopUpContext()
    const [alert, setAlert]  = useAlertContext()

    const location = useLocation()
    const navigate = useNavigate()

    useEffect(()=>{
        let cancelled = false
        async function fetchUser(username: string, password: string){
            try {
                const loginResponse = await login(username, password)

                if (!cancelled){
                    setLoading(false)
                    setContent(loginResponse)
                }

            }catch (error) {
                setLoading(false)
                setIsSubmitting(false)

                handleError(error)
            }
        }

        if (isSubmitting){
            setLoading(true)
            fetchUser(inputs.username, inputs.password)
            return () => {
                cancelled = true
            }
        }
    }, [isSubmitting, setContent])


    function handleError(error: {status: number, message: string}){
        if (error.status == 401){
            setError('Invalid credentials! Try again')
            return
        }
        //TODO: MANDAR PARA PAGINA DE ERROS
        navigate(location.state?.from?.pathname || "/")
        setPop(!pop)
        setAlert({alert: "error", message: `ERROR: ${error.status} ${error.message}`})
    }

    function handleSubmit(ev: React.FormEvent<HTMLFormElement>) {
        ev.preventDefault()
        if (!isPasswordSecure(inputs.password)){
            setError("Invalid credentials!")
            return
        }
        setIsSubmitting(true)
    }

    function handleChange(ev) {
        const name = ev.target.name
        setInputs({ ...inputs, [name]: ev.currentTarget.value })
    }

    function handleSignUp(){
        setPop(!pop)
        navigate("/signUp")
    }

    if (loading){
        return (
            <LoadingPanel message={'Signing in...'}/>
        )
    }else{
        if (content){
            const userID = content.id
            const userToken = content.token
            localStorage.setItem(userToken, JSON.stringify({id: userID, username: inputs.username}))
            setUserInfo({id: userID, username: inputs.username, token: userToken})
            //Cookies.set('AuthToken', userToken, {expires: 3, sameSite: 'strict'})
            setPop(!pop)
            navigate(location.state?.from?.pathname || "/")
        }else {
            return (
                <div className='login-form'>
                    <h1><b>Login</b></h1><hr/>
                    <form onSubmit={handleSubmit}>
                        {error}
                        <fieldset disabled={isSubmitting}>
                            <div>
                                <InputLabel htmlFor="username" className='mui-theme'>
                                    Username
                                </InputLabel>
                                <Input
                                    className='mui-theme mui-input'
                                    error={!!error}
                                    id="username"
                                    required={true}
                                    name="username"
                                    onChange={handleChange}
                                    startAdornment={
                                        <InputAdornment position="start">
                                            <AccountCircle className='mui-theme'/>
                                        </InputAdornment>
                                    }
                                />
                            </div>
                            <div>
                                <InputLabel htmlFor="password" className='mui-theme'>
                                    Password
                                </InputLabel>
                                <Input
                                    className='mui-theme mui-input'
                                    error={!!error}
                                    id="password"
                                    type="password"
                                    required={true}
                                    name="password"
                                    onChange={handleChange}
                                    startAdornment={
                                        <InputAdornment position="start">
                                            <LockIcon className='mui-theme'/>
                                        </InputAdornment>
                                    }
                                />
                            </div>
                            <div>
                                <button className='styled-button' type="submit">Submit</button>
                            </div>
                            <div>Not a member? <a style={{color: "#3d3df9", cursor: "pointer"}} onClick={handleSignUp}>Sign up here</a></div>
                        </fieldset>
                    </form>
                </div>
            )
        }
    }
}