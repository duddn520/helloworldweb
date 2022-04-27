import React from "react";
import { Box } from '@mui/material';

function Profile({ userInfo }){

    return(
        <Box sx={{flex: 1, marginTop: 5, textAlign: 'center'}}>
            <Box 
                display="flex" 
                width={300}
                alignItems="center"
                justifyContent="center"
                flex={1}
            >
                <Box sx={{width: 200, height: 200, borderRadius: 100, overflow: 'hidden'}}>
                    <img src={'https://d2u3dcdbebyaiu.cloudfront.net/uploads/atch_img/309/59932b0eb046f9fa3e063b8875032edd_crop.jpeg'} width={200} height={200} alt={'프로필 사진'}></img>
                </Box>
            </Box>
            <h3>{userInfo.userName}</h3>
            <h5>난...ㄱㅏ끔...눈물을...흘린ㄷr...</h5>
        </Box>
       
    )
}

export default Profile;