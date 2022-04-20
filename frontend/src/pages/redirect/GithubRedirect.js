import React, {useEffect} from "react";
import axios from "axios";

export default function GithubRedirect(){
    const cid = "105e0b50eefc27b4dc81";
    const cpwd = "ce4d0a93a257529e78a8804f322ca629b1d7cba6"

    useEffect(()=>
    {
        let code = window.location.search.split("code=")[1];

        axios({
            url:"http://localhost:8080/user/register/github",
            params:{"code":code},
            method:"POST"
        }).then(res=>{
            console.log(res.data);
        }).catch(e=>{
            console.log(e);
        })
    })

    return(
        <div>
            hi
        </div>
    );
}