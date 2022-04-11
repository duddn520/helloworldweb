import React from "react";
import { Box } from '@mui/system';
import axios from "axios";


export default function LoginButton({ imagelocation , login }){
    const url = "https://github.com/login/oauth/authorize?client_id=105e0b50eefc27b4dc81&redirect_uri=http://localhost:3000/login/red";
    const cid = "105e0b50eefc27b4dc81";
    const cpwd = "ce4d0a93a257529e78a8804f322ca629b1d7cba6"

    function LoginMethod(){
        window.location.replace(url);


        let accToken = window.location.search.split("code=")[1];

        console.log(accToken);



    }

    return(
        <Box sx={{marginTop:0.4}}>
            <img onClick={login} src={require(`../../images/${imagelocation}.png`)} alt='LoginButtonImage' width='300' height='45'></img>
        </Box>

    );
}
