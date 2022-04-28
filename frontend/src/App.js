import './App.css';
import {BrowserRouter, Router , Route , Routes }  from "react-router-dom";
import Login from "./pages/Login";
import Main from "./pages/Main";
import KakaoRedirect from './pages/redirect/KakaoRedirect';
import GithubRedirect from "./pages/redirect/GithubRedirect";
import NaverRedirect from './pages/redirect/NaverRedirect';
import MiniHome from "./pages/MiniHomePage/MiniHome";
import GithubConnectRedirect from "./pages/redirect/GithubConnectRedirect";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route exact path="/" element={<Main />} />
        <Route path="/login" element={<Login />} />
        <Route path="/login/redirect/kakao" element={<KakaoRedirect />} />
        <Route path="/login/redirect/github" element={<GithubRedirect />} />
        <Route path="/login/redirect/naver" element={<NaverRedirect />} />
        <Route path="/login/redirect/github/connect" element={<GithubConnectRedirect />} />
        <Route path="/minihome" element={<MiniHome />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
