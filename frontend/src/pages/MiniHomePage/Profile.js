import React from "react";
import { Box } from '@mui/material';
import { useNavigate } from 'react-router';

function Profile({ userInfo }){

    const navigate = useNavigate();

    return(
        <Box sx={{flex: 1, marginTop: 5, textAlign: 'center'}}>
            <Box 
                display="flex" 
                width={300}
                alignItems="center"
                justifyContent="center"
                flex={1}
            >
                <Box sx={{width: 200, height: 200, borderRadius: 100, overflow: 'hidden'}} >
                    <img src={userInfo.profileUrl} width={200} height={200} alt={'프로필 사진'}></img>
                </Box>
            </Box>
            <Box onClick={()=>{navigate("/makeusername")}}>
                <h3>{userInfo.userName}</h3>
            </Box>
            <h5>{userInfo.userName}의 블로그 입니다^^(고정)</h5>
        </Box>
       
    )
}

export default Profile;