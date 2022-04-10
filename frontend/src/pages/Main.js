import React from "react";
import {Button} from "@mui/material";
import {useNavigate} from "react-router-dom";

export default function (){
    const navigate = useNavigate();
    return(
        <div>
            <h1>Main</h1>
            <Button onClick={() =>  { navigate('/login') }}>Login</Button>
        </div>
    )   ;
}