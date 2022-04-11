import React from "react";
import {Box} from "@mui/material";
import {Typography} from "@mui/material";
import {Stack} from "@mui/material";
import LoginButton from "../component/login/LoginButton";
import axios from "axios";


export default function (){

    return(
        <Box className="App">
            <Box sx={{
                alignItems:'center',
                marginTop:20
            }}>
                <Typography variant='h3' sx={{
                    color:'gray'
                }}>로그인</Typography>
            </Box>
            <Box sx={{
                alignItems:'center',
                marginTop:10
            }}>
                <Stack sx={{ alignItems: 'center'}}>
                    <LoginButton imagelocation={'KakaoLogin'} />
                    <LoginButton imagelocation={'NaverLogin'} />
                    <LoginButton  imagelocation={'NaverLogin'} />
                </Stack>
            </Box>
        </Box>

        );

}