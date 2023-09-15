import {defaultFetch} from "../../Fetch/defaultFetch";


export async function login(username: string, password: string): Promise<object>{
    const tokenResponse = await defaultFetch(
        "api/user/signIn",
        'POST',
        {'content-type': 'application/json'},
        {
            username: username,
            passwordinfo: password
        })

    const token = tokenResponse.token

    const userResponse = await getUserInfo(token)
    return {id: userResponse.properties.id, token: token}
}



export async function createAccount(username: string, password: string): Promise<void>{
    await defaultFetch(
        'api/user',
        'POST',
        {'content-type': 'application/json'},
        {
            username: username,
            passwordinfo: password
        })
}

export async function getUserInfo(token:string){
    return await defaultFetch(
        'api/me',
        'GET',
        {'content-type': 'application/vnd.siren+json', 'Authorization': `Bearer ${token}`},
        null
    )
}


export async function requestAuthToken(username: string, password: string) : Promise<string> {

    const response = await defaultFetch(
        "api/user/signIn",
        'POST',
        {'content-type': 'application/json'},
        {
        username: username,
        passwordinfo: password
        })

    return response.token
}

export async function createUser(username: string, password: string){
    const response = await fetch("api/user", {
        method: 'POST',
        headers: {'content-type': 'application/json'},
        body: JSON.stringify({
            username: username,
            passwordinfo: password
        })
    })
}

export function isPasswordSecure(password: string): boolean{
    return ((/[A-Z]/.test(password)) && (/[a-z]/.test(password)) && (/[0-9]/.test(password)) && password.length >= 9);
}



