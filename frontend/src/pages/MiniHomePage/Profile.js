import React from "react";
import { useNavigate } from 'react-router';

function Profile(){
    const navigate = useNavigate();

    function LogOut(){
        window.sessionStorage.removeItem("Auth");
        navigate("/");
      }

    return(
        <div>
            <img src={'https://play-lh.googleusercontent.com/8_0SDfkFXAFm12A7XEqkyChCdGC055J6fC8JR7qynNuO3qNOczIoNHo4U4lad8xYMJOL'} width={200} height={200}></img>
            <h3>허지훈</h3>
            <h5>난...ㄱㅏ끔...눈물을...흘린ㄷr...</h5>
            <img src={require('../../images/btnG_LogOut.png')} width={200} height={50} onClick={()=>LogOut()}></img>
        </div>
       
    )
}

export default Profile;