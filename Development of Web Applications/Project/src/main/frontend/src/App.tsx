import '../src/style.css'

import * as React from 'react'
import {
    createBrowserRouter,
    RouterProvider,
    Link,
    useParams,
    Outlet,
    Location, useLocation, useNavigate, Navigate
} from 'react-router-dom'
import {useFetch} from "./Fetch/useFetch";
import {Layout} from "./components/Layout";
import {AuthnProvider, useAuthentication} from "./components/Auth/Components/AuthProvider";
import {LeaderBoard} from "./components/HomePages/LeaderBoard";
import {Home} from "./components/HomePages/Home";
import {Game} from "./components/Game/Game";
import {SignUp} from "./components/Auth/Components/SignUp";
import ErrorPage from "./components/ErrorHandling/ErrorPage";
import {JoinGameScreen} from "./components/Game/gameViews/JoinGameScreen";
import {RequiresAuth} from "./components/Auth/Components/RequiresAuth";
import {LoadingPanel} from "./components/LoadingPanel";
import {About} from "./components/HomePages/About";
import {UserDetails} from "./components/HomePages/UserDetails";
import {UserCard} from "./components/UserCard";


const router = createBrowserRouter([
    {

        element: <Layout/>,
        errorElement: <ErrorPage/>,
        children: [
            {
                path: "/",
                element: <Home/>
            },
            {
                path: "/about",
                element: <About uri={"about"}/>
            },
            {
                path: "/me",
                element: <RequiresAuth><UserDetails/></RequiresAuth>,
            },
            {
                path: "/leaderboard",
                element: <LeaderBoard uri="leaderboard"/>
            },
            {
                path: "/signUp",
                element: <SignUp/>
            },
            {
                path: "/game",
                element: <RequiresAuth><JoinGameScreen/></RequiresAuth>,
            },
            {
                path: "/game/:id",
                element:  <RequiresAuth><Game apiUri={" "}/></RequiresAuth>
            },
            {
                path: "/test",
            }
        ]
    }
])

export function App() {
    return (
        <AuthnProvider>
            <RouterProvider router={router}/>
        </AuthnProvider>
    )
}
