import React from "react";
import { Box } from '@mui/system';
import {Button} from "@mui/material";
import {Avatar} from "@mui/material";
import axios from "axios";


export default function LoginButton({ imagelocation , login ,login_method }){

    return(
        <Box sx={{marginTop:0.8}}>
            <Button
                startIcon={<Avatar sx={{position : "absolute", left:5, alignSelf:"center", bottom:4.2}} onClick={login} src={require(`../../images/${imagelocation}.png`)} alt='LoginButtonImage' width='50' height='50' />}
                sx={{ color: 'gray', width : 300, height: 50, fontSize:14 ,backgroundColor : "white" ,boxShadow:0 , borderColor:"gray", borderWidth:0.9 ,":hover":{ boxShadow:0, backgroundColor:"whitesmoke", borderColor:"gray"}}}
                variant={"outlined"}
                onClick={login}

            >
                {`${login_method}로 로그인`}
            </Button>
        </Box>

    );
}
