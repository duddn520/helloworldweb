import React from "react";
import {Button} from "@mui/material";
import {useNavigate , Redirect , useParams } from "react-router-dom";


export default function ( props ){
    const navigate = useNavigate();

    return(
        <div>
            <h1>Main</h1>
            <Button onClick={() =>  { navigate('/login') }}>Login</Button>
        </div>
    )   ;
}