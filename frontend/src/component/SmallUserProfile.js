import React from "react";
import { Box, Typography, IconButton } from "@mui/material";
import { useNavigate } from "react-router-dom";

function SmallUserProfile({userInfo}){
    const navigate = useNavigate();

    function moveToMiniHome(){
        navigate(`/minihome/`, {state: {tabIndex: 0, targetEmail: userInfo.email}});
    }

    return(
        <Box sx={{display: 'flex', alignItems: 'center', mb: 1}}>
            <Box sx={{display: 'flex', alignItems: 'center'}} onClick={()=>{moveToMiniHome();}}>
                <Box sx={{width: 30, height: 30, borderRadius: 15, overflow: 'hidden', marginRight: 1, display: 'flex', alignItems: 'center'}}>
                    <img src={userInfo.profileUrl} width={30} height={30} alt={'프로필 사진'}></img>
                </Box>
                <Typography>{userInfo.userName}</Typography>
            </Box>
        </Box>
    )
}

export default SmallUserProfile;