import './App.css';
import {BrowserRouter, Router , Route , Routes }  from "react-router-dom";
import Login from "./pages/Login";
import Main from "./pages/Main";
import KakaoRedirect from './pages/redirect/KakaoRedirect';
import GithubRedirect from "./pages/redirect/GithubRedirect";
import NaverRedirect from './pages/redirect/NaverRedirect';
import Red from "./pages/Red";
import QnARegister from './pages/questions/QnARegister';
import QnA from './pages/questions/QnA';
import MiniHome from "./pages/MiniHomePage/MiniHome";
import WriteBlog from './pages/MiniHomePage/WriteBlog';
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
        {/* QnA */}
        <Route exact path="/qna" element={<QnA />} />
        <Route exact path="/qna/register" element={<QnARegister />} />
        
        <Route path="/minihome" element={<MiniHome />} />
        <Route path="/minihome/write" element={<WriteBlog />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
