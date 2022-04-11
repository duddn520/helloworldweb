import logo from './logo.svg';
import './App.css';
import {BrowserRouter, Router , Route , Routes }  from "react-router-dom";
import Login from "./pages/Login";
import Main from "./pages/Main";
import Redirect from './pages/redirect/Redirect';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route exact path="/" element={<Main />} />
        <Route path="/login" element={<Login />} />
        <Route path="/login/redirect" element={<Redirect />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
