import axios from "axios";
import api from "./api";
import { useNavigate } from 'react-router-dom';
import urls from "./URL";

const request = axios.create({
    timeout: 20000,
    baseURL: urls.server
})

// HTTP 요청 직전에 가로챔. ( 전역 )
// jwt 있을 시 넣어서 요청 
request.interceptors.request.use( 
    async function (config) {
        const token = window.sessionStorage.getItem("Auth");
        if ( token !== null )
            config.headers['Auth'] =  token ;

        return config;
    },
    function ( error ) {
        // 요청 직전에 에러
        console.log(error);
    return Promise.reject(error);
    }
);

request.interceptors.response.use(
    (response) => {
        return response;
    },
    (error) => {
        
        // 서버 에러가 난 경우
        if( error?.response?.status === 403 && hasToken() ){
            api.getNewToken( window.sessionStorage.getItem("Auth") , window.sessionStorage.getItem("Refresh") )
            .then( res => {
                if( res.auth )
                    window.sessionStorage.setItem("Auth",res.auth);
                window.location.reload();
            })
            .catch( e => {
                alert("다시 로그인 해주세요.")
                window.sessionStorage.clear();
                window.location.replace("/login");
            })
        }else if(error?.response?.status === 403 && !hasToken())
        {
            alert("로그인 해주세요.")
            window.location.replace("/login");
        }
        
        return Promise.reject(error);
    }
);

const hasToken = () => {
    if( window.sessionStorage.getItem("Auth") && window.sessionStorage.getItem("Refresh") )
        return true;
    return false;
}

export default request;