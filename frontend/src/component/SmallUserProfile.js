import React from "react";
import { Box, Typography, IconButton } from "@mui/material";

function SmallUserProfile({userInfo}){
    return(
        <Box sx={{display: 'flex', alignItems: 'center' }}>
            <Box sx={{width: 30, height: 30, borderRadius: 15, overflow: 'hidden', marginRight: 2}}>
                <img src={userInfo.profileUrl} width={30} height={30} alt={'프로필 사진'}></img>
            </Box>
            <Typography>{userInfo.userName}</Typography>
        </Box>
    )
}

export default SmallUserProfile;