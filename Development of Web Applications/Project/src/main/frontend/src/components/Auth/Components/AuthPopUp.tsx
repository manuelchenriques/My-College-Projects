import CloseIcon from "@mui/icons-material/Close";
import * as React from "react";
import {usePopUpContext} from "../../Layout";
import {Paper} from "@mui/material";
import {Login} from "./Login";

export function AuthPopUp(){
    const [pop, setPop] = usePopUpContext()

    return (
        <>
            <div className="modal">
                <Paper className="modal_content" elevation={24}>
                    <CloseIcon className="modal_close" onClick={function (){setPop(!pop)}}> </CloseIcon>
                    <Login/>
                </Paper>
            </div>
        </>
    )
}