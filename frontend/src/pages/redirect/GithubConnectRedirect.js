import React, {useEffect} from "react";
import axios from "axios";
import {useNavigate} from "react-router";
import request from "../../api/request";

export default function GithubConnectRedirect(){
    const navigate = useNavigate()

    useEffect(()=>
    {
        let code = window.location.search.split("code=")[1];
        request({
            url:"http://localhost:8080/user/githubconnect",
            params:{
                "code":code
            },
            method:"POST"
        }).then(()=> {
            // 미니홈으로 이동
            navigate("/minihome")

        }).catch(e=>{
            console.log(e);
        })
    })

    return(
        <div>
            연동 중입니다.
        </div>
    );
}