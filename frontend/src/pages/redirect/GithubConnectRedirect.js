import React, {useEffect} from "react";
import axios from "axios";
import {useNavigate} from "react-router";
import request from "../../api/request";
import api from "../../api/api";

export default function GithubConnectRedirect(){

    const [userEmail,setUserEmail] = React.useState([])
    const navigate = useNavigate()

    useEffect(()=>
    {
        let code = window.location.search.split("code=")[1];
        request({
            url:"/user/githubconnect",
            params:{
                "code":code
            },
            method:"POST"
        }).then(()=> {
            // 미니홈으로 이동
            api.getUser()
            .then(res=>{
                setUserEmail(res.email)
                navigate("/minihome",{state:{tabIndex:0, targetEmail:userEmail}})
            })
            .catch(e=>{
                console.log(e)
            })

        }).catch(e=>{
            console.log(e);
        })
    },[]);

    return(
        <div>
            연동 중입니다.
        </div>
    );
}