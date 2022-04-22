import React from "react";
import {Box} from "@mui/material";
import {Typography} from "@mui/material";
import {Stack} from "@mui/material";
import LoginButton from "../component/login/LoginButton";
import axios from "axios";
import api from "../api/api";
import { Button } from "@mui/material";

const rest_api_key = "0961546bc0d26c67ab09717f36939b7c";
const redirect_url = "http://localhost:3000/login/redirect/kakao";

const naver_client_id = '3RFZ_7joHf_HlJXavuMB';
const naverLogin_redirect_url = 'http://localhost:3000/login/redirect/naver';

const kakao_url = `https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=${rest_api_key}&redirect_uri=${redirect_url}`;
const github_url = "https://github.com/login/oauth/authorize?client_id=105e0b50eefc27b4dc81&redirect_uri=http://localhost:3000/login/red";
const naver_url = `https://nid.naver.com/oauth2.0/authorize?client_id=${naver_client_id}&response_type=code&redirect_uri=${naverLogin_redirect_url}&state=hLiDdL2uhPtsftcU`

// 카카오 인가코드를 받아옴 ( REST_API_KEY , REDIRECT_URL 필요 )
function kakao_getCode(){
    // 현재 페이지를 대체
    window.location.replace(kakao_url);
}

function github_getCode(){
    window.location.replace(github_url);
}

function naver_getCode(){
    window.location.replace(naver_url);
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
                    <LoginButton imagelocation={'kakao_icon'} login={kakao_getCode} login_method={"카카오"}/>
                    <LoginButton imagelocation={'naver_icon'} login={naver_getCode} login_method={"네이버"}/>
                    <LoginButton  imagelocation={'github_icon'} login={github_getCode} login_method={"Github"} />
                </Stack>
            </Box>
        </Box>

        );

}