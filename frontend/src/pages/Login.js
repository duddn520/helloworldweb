import React from "react";
import {Box} from "@mui/material";
import {Typography} from "@mui/material";
import {Stack} from "@mui/material";
import LoginButton from "../component/login/LoginButton";
import axios from "axios";
import api from "../api/api";
import { Button } from "@mui/material";

const rest_api_key = "0961546bc0d26c67ab09717f36939b7c";
const redirect_url = "http://localhost:3000/login/redirect";

// 카카오 인가코드를 받아옴 ( REST_API_KEY , REDIRECT_URL 필요 )
function kakao_getCode(){
    const kakao_url = `https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=${rest_api_key}&redirect_uri=${redirect_url}`;
    // 현재 페이지를 대체
    window.location.replace(kakao_url);
}


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