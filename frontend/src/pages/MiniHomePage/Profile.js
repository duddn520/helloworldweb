import React from "react";
import { Box ,Typography ,Badge} from '@mui/material';
import {  LocationOn as LocationOnIcon } from '@mui/icons-material';


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
                    <img src={userInfo.profileUrl} width={200} height={200} alt={'프로필 사진'}></img>
                </Box>
            </Box>
            <h3>{userInfo.userName}</h3>
            <Box sx={{ display: 'flex' , flexDirection: 'row' ,justifyContent: 'center' }}>
                <LocationOnIcon sx={{ color: 'gray'}}/>
                <Typography sx={{ color: 'gray' ,ml: 2 }}>서울, 대한민국</Typography>
            </Box>
            <Box sx={{ m: 2 ,display: 'flex' , flexDirection: 'row' ,justifyContent: 'center' }}>
                <Badge sx={{ m: 1 ,backgroundColor: 'green' ,p: 0.5 ,color: 'white'}}>Python</Badge>
                <Badge sx={{ m: 1 ,backgroundColor: 'orange' ,p: 0.5 ,color: 'white' }}>Java</Badge>
            </Box>
            {/* <h5>{userInfo.userName}의 블로그 입니다^^(고정)</h5> */}
        </Box>
       
    )
}

export default Profile;