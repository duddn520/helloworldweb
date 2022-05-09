import React from 'react';
import { Box } from '@mui/material';
import ChromeTabBar from '../../component/miniHome/ChromeTabBar'
import Profile from './Profile';
import MyInfoTab from './MyInfoTab';
import BlogsTab from './BlogsTab';
import GuestBooksTab from './GuestBooksTab';
import TotalBar from './TotalBar';
import { useNavigate, useLocation } from 'react-router';
import api from '../../api/api';

const BlankUser = {
  id: -1,
  email: '',
  userName: '',
}

function MiniHome() {
  const navigate = useNavigate();
  const { state } = useLocation();
  const [index, setIndex] = React.useState((state.tabIndex === null) ? 0 : state.tabIndex);
  const [userInfo, setUserInfo]= React.useState(null);
  const [thisHomeOwner, setThisHomeOwner] = React.useState(state.targetEmail);

  function LogOut(){
    window.sessionStorage.removeItem("Auth");
    navigate("/", {replace: true});
  }

  React.useEffect(()=>{
    api.getOtherUser(thisHomeOwner)
    .then(res => {
      setUserInfo(res);
    })
    .catch((e) => {
      alert('회원정보를 불러오는데 실패했습니다.');
    })
  }, [])
  
  return (
    <div>
     {userInfo === null ? 
     <div/> : 
     <TotalBar drawer={<Profile userInfo={userInfo}/>} title={userInfo.userName+'의 미니홈피'} logOut={LogOut}>
         <Box sx={{flex: 1}}>
            <div>
                <ChromeTabBar index={index} setIndex={setIndex} tabNames={['내 정보', '게시물', '방명록']}/>
                {index === 0 && <MyInfoTab userInfo={userInfo}/>}
                {index === 1 && <BlogsTab userInfo={userInfo}/>}
                {index === 2 && <GuestBooksTab userInfo={userInfo}/>}
            </div> 
        </Box>
     </TotalBar>}
    </div>
  );
}

export default MiniHome;