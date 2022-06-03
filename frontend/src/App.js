import './App.css';
import {BrowserRouter, Router , Route , Routes }  from "react-router-dom";
import Login from "./pages/Login";
import Main from "./pages/Main";
import KakaoRedirect from './pages/redirect/KakaoRedirect';
import GithubRedirect from "./pages/redirect/GithubRedirect";
import NaverRedirect from './pages/redirect/NaverRedirect';
import QnARegister from './pages/questions/QnARegister';
import QnA from './pages/questions/QnA';
import MiniHome from "./pages/MiniHomePage/MiniHome";
import WriteBlog from './pages/MiniHomePage/WriteBlog';
import GithubConnectRedirect from "./pages/redirect/GithubConnectRedirect";
import Search from './pages/search/Search';
import Blog from './pages/Blog';
import MakeUserName from './pages/MakeUserName';
import React from 'react';
import { firebaseApp, vapidKey } from './firebase';
import { getMessaging, getToken, onMessage } from 'firebase/messaging'
import { onBackgroundMessage } from 'firebase/messaging/sw';
import api from './api/api';
import { MessageOutlined } from '@mui/icons-material';

const firebaseMessaging = getMessaging(firebaseApp);

function App() {

  onMessage(firebaseMessaging,(payload)=>{
    console.log(payload.notification.title);
    console.log(payload.notification.body);
  });

  // onBackgroundMessage(firebaseMessaging, (payload) => {
  //   console.log('[firebase-messaging-sw.js] Received background message ', payload);
  //   // Customize notification here
  //   const notificationTitle = 'Background Message Title';
  //   const notificationOptions = {
  //     body: 'Background Message body.',
  //     icon: '/firebase-logo.png'
  //   };
  // });

  React.useEffect(()=>{
    getToken(firebaseMessaging,{vapidKey:vapidKey})
    .then((currentToken) => {
      if(currentToken){
        console.log(currentToken);
        window.sessionStorage.setItem("fcm",currentToken);
        console.log(window.sessionStorage.getItem("fcm"))
        api.updateFcm(currentToken)
        .then(res=>{ console.log(res)})
        .catch(e=>{ console.log(e)})
      }
      else
      {
        console.log("fcm 획득 실패")
      }
    })
    .catch(function (error) {
      console.log("FCM Error : ", error);
    });

  },[])

  return (
    <BrowserRouter>
      <Routes>
        <Route exact path="/" element={<Main />} />
        <Route path="/login" element={<Login />} />
        <Route path="/login/redirect/kakao" element={<KakaoRedirect />} />
        <Route path="/login/redirect/github" element={<GithubRedirect />} />
        <Route path="/login/redirect/naver" element={<NaverRedirect />} />
        <Route path="/login/redirect/github/connect" element={<GithubConnectRedirect />} />
        <Route path="/makeusername" element={<MakeUserName />} />
        {/* QnA */}
        <Route exact path="/qna/:id" element={<QnA />} />
        <Route exact path="/qna/register" element={<QnARegister />} />
        {/* search */}
        <Route path="/search" element={<Search />} />

        <Route path="/minihome" element={<MiniHome />} />
        <Route path="/minihome/write" element={<WriteBlog />} />
        <Route path="/blog" element={<Blog/>} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
