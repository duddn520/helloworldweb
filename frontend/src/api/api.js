import axios from 'axios';
import request from './request';

// Http Response statusCode 정리
const status = {
    POST_SUCCESS : 210 ,
    GET_SUCCESS : 211 ,
    PUT_SUCCESS : 212 ,
    DELETE_SUCCESS : 213
}


// POST - 카카오 유저 로그인/회원가입
function registerUserWithKakao(token){
    return new Promise((resolve,reject) => {
        request({
            method: 'POST' ,
            url: '/user/register/kakao',
            headers: { 
                token: token
                // "Access-Control-Allow-Origin" : true
            },
        })
        .then( res => {
            // Jwt 반환
            if ( res.data.statusCode == status.POST_SUCCESS ){
                resolve (res.headers.auth);
            }
        })
        .catch( e => {
            console.log(e);
            reject();
        })
    });
}

// GET - 나의 방명록 조회
function getGuestBooks(){
    return new Promise((resolve,reject) => {
        request({
            method: 'GET' ,
            url: '/api/guestbook',
            params: { 
                id: 1
            },
        })
        .then( res => {
            if ( res.data.statusCode == status.GET_SUCCESS ){

                console.log(res.data.data);
                resolve (res.data.data);
            }
        })
        .catch( e => {
            console.log(e);
            reject();
        })
    });
}

// POST - 게시글/QnA 작성
function registerPost( {content,type}){
    return new Promise((resolve,reject) => {
        request({
            method: 'POST' ,
            url: '/api/post',
            data: {
                content: content ,
                category: type ,
                user_id: 1
            }
        })
        .then( res => {
            if ( res.data.statusCode == status.POST_SUCCESS ){
                resolve(true);
            }
        })
        .catch( e => {
            console.log(e);
            reject(false);
        })
    });
}
// GET - 모든 QnA 조회
function getAllQna(){
    return new Promise((resolve,reject) => {
        request({
            method: 'GET' ,
            url: '/api/post/qnas',
        })
        .then( res => {
            if ( res.data.statusCode == status.GET_SUCCESS ){
                resolve(res.data.data);
            }
        })
        .catch( e => {
            console.log(e);
            // 실패 시 빈 리스트 리턴
            reject([]);
        })
    });
}


export default { registerUserWithKakao,getGuestBooks,registerPost,getAllQna } ;