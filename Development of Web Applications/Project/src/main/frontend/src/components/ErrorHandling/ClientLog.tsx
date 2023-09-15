import React, {Dispatch} from "react"
import {Alert, AlertTitle} from "@mui/material";
import {useAlertContext} from "../Layout";

export type AlertContext= {
    alert: "error"| "warning" | "info" | "success",
    message: string
}

export function ClientLog(props: {context: AlertContext, onClose:()=>void}) {

    const type = props.context.alert
    return(
        <div className='alert'>
            <Alert severity={type} onClose={props.onClose}>
                <AlertTitle> {type.charAt(0).toUpperCase() + type.slice(1)} </AlertTitle>
                {props.context.message}
            </Alert>
        </div>
    )
}