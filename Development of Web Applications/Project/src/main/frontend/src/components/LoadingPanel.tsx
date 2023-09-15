import React from "react";
import {Box, CircularProgress, circularProgressClasses} from "@mui/material";


export function LoadingPanel(prop:{message: string}){
    return (
        <div className='loading'>
            <Box sx={{ display: 'flex' }} style={{scale: 1.8}}>
                <CircularProgress />
            </Box>
            <h1>{prop.message}</h1>
        </div>
    );
}