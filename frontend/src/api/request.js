import axios from "axios";


const request = axios.create({
    timeout: 20000
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

export default request;