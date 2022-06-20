import React from 'react';
import { useNavigate } from 'react-router';
import axios from 'axios';
import api from '../../api/api';
import urls from '../../api/URL';

const rest_api_key = "0961546bc0d26c67ab09717f36939b7c";
const redirect_url = `${urls.front}/login/redirect`;


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
                api.registerUserWithKakao( res.data.access_token )
                .then(async (res) => {
                    // SessionStorage에 jwt 저장
                    window.sessionStorage.setItem("Auth", res.accessToken);
                    window.sessionStorage.setItem("Refresh", res.refreshToken);
                    api.updateFcm(window.sessionStorage.getItem("fcm"))
                    .then(response =>{
                        if( res.isAlreadyRegister ){
                            // 미니홈피페이지로 이동
                            navigate("/", {replace: true});
                        }
                        else{
                            navigate("/makeusername", {replace: true});
                        }
                    }).catch(e=>{
                        console.log(e)
                    })
                });

            })
            .catch( e => {  
                // 로그인 또는 회원가입 실패 
                navigate("/login");
            });

        }

    },[]);

    return(
        <div>
            <h1>로그인 중...</h1>
        </div>
    );
}
