import React from 'react';
import { Box } from '@mui/material';
import ChromeTabBar from '../../component/miniHome/ChromeTabBar'
import Profile from './Profile';
import MyInfo from './MyInfo';
import Posts from './Posts';
import GuestBooks from './GuestBooks';
import TotalBar from './TotalBar';
import { useNavigate } from 'react-router';
import api from '../../api/api';

const BlankUser = {
  id: -1,
  email: '',
  userName: '',
}

function Main() {
  const navigate = useNavigate();
  const [index, setIndex] = React.useState(0);
  const [userInfo, setUserInfo]= React.useState(BlankUser);

  function LogOut(){
    window.sessionStorage.removeItem("Auth");
    navigate("/", {replace: true});
  }

  React.useEffect(()=>{
    api.getUser()
    .then(res => {
      setUserInfo(res);
    })
    .catch((e) => {
      alert('회원정보를 불러오는데 실패했습니다.')
    })
  }, [])
  
  return (
     <TotalBar drawer={<Profile userInfo={userInfo}/>} title={userInfo.userName+'의 미니홈피'} logOut={LogOut}>
         <Box>
            <div>
                <ChromeTabBar index={index} setIndex={setIndex} tabNames={['내 정보', '게시물', '방명록']}/>
                {index === 0 && <MyInfo/>}
                {index === 1 && <Posts/>}
                {index === 2 && <GuestBooks/>}
            </div> 
        </Box>
     </TotalBar>
  );
}

export default Main;