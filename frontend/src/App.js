import './App.css';
import {BrowserRouter, Router , Route , Routes }  from "react-router-dom";
import Login from "./pages/Login";
import Main from "./pages/Main";
import KakaoRedirect from './pages/redirect/KakaoRedirect';
import NaverRedirect from './pages/redirect/NaverRedirect';
import Red from "./pages/Red";
import QnARegister from './pages/questions/QnARegister';
import QnA from './pages/questions/QnA';
import MiniHome from "./pages/MiniHomePage/MiniHome";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route exact path="/" element={<Main />} />
        <Route path="/login" element={<Login />} />
        <Route path="/login/redirect/kakao" element={<KakaoRedirect />} />
        <Route path="/login/red" element={<Red />} />
        <Route path="/login/redirect/naver" element={<NaverRedirect />} />
        {/* QnA */}
        <Route exact path="/qna" element={<QnA />} />
        <Route exact path="/qna/register" element={<QnARegister />} />
        
        <Route path="/minihome" element={<MiniHome />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
