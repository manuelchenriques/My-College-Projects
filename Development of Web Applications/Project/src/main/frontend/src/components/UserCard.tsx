import {Button, Card, CardActions, CardContent, CardMedia, Typography} from "@mui/material";
import React from "react";
type UserCardInfo = {
    title: string,
    contents: Array<{label: string, value: any}>
}
export function UserCard(user: UserCardInfo){
    return (
        <Card sx={{ maxWidth: 345 }}>
            <CardMedia
                sx={{ height: 140 }}
                image='https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460__340.png'
                title="User Image"
            />
            <CardContent>
                <Typography gutterBottom variant="h5" component="div">
                    {user.title}
                </Typography>
                <Typography variant="body2" color="text.secondary">
                    <div style={{textAlign: 'left'}}>
                    {user.contents.map((target, ix)=> createText(target))}
                    </div>
                </Typography>
            </CardContent>
        </Card>
    );
}

function createText(input:{label: string, value: any}){
    return(
        <h5>{input.label}: {input.value}</h5>
    )
}