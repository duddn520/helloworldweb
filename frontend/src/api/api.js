import axios from 'axios';

const baseUrl = "http://localhost:8080"

//   GET - /api/guestbook
function getGuestBooks(){
    axios.get(baseUrl+'/api/guestbook')
        .then( res => {
            return res.data.data;
        })
        .catch( e => {
            console.log(e);
        })

}
function kakaoLogin(token){
    console.log(token);
    axios({
        method: 'POST' ,
        // baseURL: baseUrl ,
        url: '/login/kakao',
        headers: { 
            "token" : token ,
            // "Access-Control-Allow-Origin" : true
        },
    })
    .then( res => {
        console.log(res.data)
    })
    .catch( e => {

    })
}

export default { getGuestBooks , kakaoLogin } ;