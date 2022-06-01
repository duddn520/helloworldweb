import React from "react";
import { useNavigate } from 'react-router';
import {  EditOutlined, LocationOn as LocationOnIcon, Edit as EditIcon  } from '@mui/icons-material';
import { Box ,Typography ,Badge ,IconButton, Button } from '@mui/material';
import api from "../../api/api";
import ReactPlayer from 'react-player'

function Profile({ userInfo }){
    const [profileMusic, setProfileMusic] = React.useState(userInfo?.profileMusicUrl);

    const navigate = useNavigate();

    const musicReader = (e) => {
        const targetMusic = e.target.files[0];
        let formdata = new FormData();
        formdata.append('file', targetMusic);
        api.updateProfileMusic(formdata, userInfo.id)
        .then((res)=>{
            setProfileMusic(res);
        })
        .catch(()=>{
            console.log(e);
        })
    }

    return(
        <Box sx={{flex: 1, marginTop: 5, textAlign: 'center'}}>
            <Box 
                display="flex" 
                width={300}
                alignItems="center"
                justifyContent="center"
                flex={1}
            >
                <Box sx={{width: 200, height: 200, borderRadius: 100, overflow: 'hidden', mb: 3}} >
                    <img src={userInfo.profileUrl} width={200} height={200} alt={'프로필 사진'}></img>
                </Box>
            </Box>
            <div style={{ padding: 7, display: 'flex', justifyContent: 'center' ,alignItems: 'center' }} onClick={()=>{ if(userInfo.isOwner) navigate("/makeusername")}}>
                <Typography sx={{ fontWeight: 'bold' }}>{userInfo.userName}</Typography>
                {userInfo.isOwner && <IconButton><EditOutlined sx={{ fontSize: 17 }}/></IconButton>}
            </div>
            <Box sx={{ display: 'flex' , flexDirection: 'row' ,justifyContent: 'center' }}>
                <Badge sx={{ m: 1 ,backgroundColor: 'green' ,p: 0.5 ,color: 'white' ,borderRadius: 1 ,fontSize: 13}}>Python</Badge>
                <Badge sx={{ m: 1 ,backgroundColor: 'orange' ,p: 0.5 ,color: 'white' ,borderRadius: 1 ,fontSize: 13}}>Java</Badge>
            </Box>
            <Box sx={{ display: 'flex' , flexDirection: 'row' ,justifyContent: 'center', mt: 1 }}>
                <LocationOnIcon sx={{ color: 'gray' ,fontSize: 17}}/>
                <Typography sx={{ color: 'gray' ,ml: 2 ,fontSize: 13}}>서울, 대한민국</Typography>
            </Box>
            <h5>{userInfo.userName}의 블로그 입니다^^</h5>
            <Box sx={{ display: 'flex' , flexDirection: 'row' ,justifyContent: 'center', mt: 1, alignItems: 'center'}}>
                <Typography sx={{ mr: 1 ,fontSize: 13}}>{userInfo?.profileMusic === null || userInfo?.profileMusic === undefined ? "프로필 뮤직 설정": userInfo.profileMusic}</Typography>
                {userInfo.isOwner && 
                    <Button variant="outlined" component="label" sx={{height: 30}}> MUSIC
                        <input 
                        type="file"
                        accept="audio/mp3"
                        multiple={false}
                        hidden
                        onChange={musicReader}/>
                    </Button>}
            </Box>
            {(profileMusic != null) &&
            <Box sx={{ display: 'flex', width: '100%', height: 20, mt: 1}}>
                <ReactPlayer 
                url={profileMusic}
                playing={true}
                controls={true}
                width={"100%"}
                height={"100%"}
                />
            </Box>}
        </Box>      
    )
}

export default Profile;