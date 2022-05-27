import React, {useEffect} from "react";
import axios from "axios";
import {useNavigate} from "react-router";
import api from "../../api/api";

export default function GithubRedirect(){
    const navigate = useNavigate()

    useEffect(()=>
    {
        let code = window.location.search.split("code=")[1];

        api.registerUserWithGithub(code)
        .then(async (res)=>{
             // SessionStorage에 jwt 저장
             window.sessionStorage.setItem("Auth", res.accessToken);
             window.sessionStorage.setItem("Refresh", res.refreshToken);

             if( res.isAlreadyRegister ){
                 // 미니홈피페이지로 이동
                 navigate("/", {replace: true});
             }
             else{
                 navigate("/makeusername", {replace: true});
             }

        }).catch(e=>{
            console.log(e);
        })
    },[]);

    return(
        <div>
            로그인 중입니다.
        </div>
    );
}