import React from 'react';
import { useNavigate } from 'react-router';
import axios from 'axios';
import api from '../../api/api';

const rest_api_key = "0961546bc0d26c67ab09717f36939b7c";
const redirect_url = "http://localhost:3000/login/redirect";

export default function Redirect(){
    const navigate = useNavigate();

    React.useEffect(() => {

        if( window.location.search.startsWith("?code") ){

            const kakao_code = window.location.search.split("code=")[1] ;

            axios({
                method: "post",
                url: "https://kauth.kakao.com/oauth/token",
                params: {
                    "grant_type" : "authorization_code" ,
                    "client_id" : rest_api_key ,
                    "redirect_url" : redirect_url ,
                    "code" : kakao_code ,
                } 
            })
            .then( async(res) => {

                // 서버에 요청 ( Promise 함수 호출을 통해 jwt 리턴 보장 )
                const token = await api.registerUserWithKakao( res.data.access_token );

                // SessionStorage에 jwt 저장
                window.sessionStorage.setItem("Auth",token);

                // 메인화면으로 이동
                navigate("/");

            })
            .catch( e => {  
                // 로그인 또는 회원가입 실패 
            });

        }

    })

    return(
        <div>
            <h1>로그인 중...</h1>
        </div>
    );
}
