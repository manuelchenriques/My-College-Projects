
import {Navigate, useLocation} from "react-router-dom";
import React from "react";
import {useAlertContext} from "../../Layout";
import {useAuthentication} from "./AuthProvider";

export function RequiresAuth({ children }: React.PropsWithChildren<{}>): React.ReactElement {
    const [userInfo] = useAuthentication()
    const [alert, setAlert] = useAlertContext()
    const location = useLocation()

    if (userInfo) {
        return <>{children}</>
    } else {
        setAlert({alert: "warning", message: "Authentication required!"})
        return <Navigate to="/" replace={true} state={{from: location}}/>
    }

}