import './App.css';
import {BrowserRouter, Router , Route , Routes }  from "react-router-dom";
import Login from "./pages/Login";
import Main from "./pages/Main";
import KakaoRedirect from './pages/redirect/KakaoRedirect';
import GithubRedirect from "./pages/redirect/GithubRedirect";
import FlatlistTest from "./pages/FlatlistTest";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/*<Route exact path="/" element={<Main />} />*/}
        <Route exact path="/" element={<FlatlistTest />} />
        <Route path="/login" element={<Login />} />
        <Route path="/login/redirect/kakao" element={<KakaoRedirect />} />
        <Route path="/login/redirect/github" element={<GithubRedirect />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
