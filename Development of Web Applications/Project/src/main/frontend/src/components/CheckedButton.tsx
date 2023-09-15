import {Checkbox, TextField} from "@mui/material";
import * as React from "react";
import {useState} from "react";

export function CheckedButton({name} :{ name : string} ) {
    const [textField, setTextField] = useState("")
    const [checkbox, setCheckBox] = useState(false)

        return (<div >< Checkbox id = {name +  "_checkbox"} value={checkbox} name={ name} checked={checkbox} onChange={() => {

            if(checkbox){ // if the checkbox was checked when it was clicked then we must set the text value to 0 and disable it
                setTextField("")
            } else setTextField("1")
            setCheckBox( !checkbox)

        }}/>
            <TextField
                id={name + "_textField"}
                type="number"
                label = {name}
                value = {textField}
                disabled={!checkbox}// if checkbox is enabled disable this textfield
                onChange={e => {
                    if( Number(e.target.value)>0)setTextField(e.target.value)
                    else {
                        setTextField("")
                        setCheckBox( !checkbox)
                    }
                }}
                InputLabelProps={{
                    shrink: true,
                }}
            />
        </div>)


    
}