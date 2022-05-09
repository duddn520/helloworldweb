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
            if ( res.data.statusCode === status.POST_SUCCESS ){
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
            // Jwt 반환
            if ( res.data.statusCode === status.GET_SUCCESS ){
                resolve (res.data.data);
            }
        })
        .catch( e => {
            console.log(e);
            reject();
        })
    });
}
// PUT - 방명록 수정 및 답글 달기
function updateGuestBook({ id, reply }){
    return new Promise((resolve,reject) => {
        request({
            method: 'PUT' ,
            url: '/api/guestbook',
            data: {
                id: id ,
                reply: reply
            }
        })
        .then( res => {
            console.log(res);

        })
        .catch( e => {
            console.log(e);
            reject();
        })
    });
}

//Post - 방명록 작성
function registerGuestBook({ targetUserEmail ,content }){

    return new Promise((resolve,reject) => {
        request({
            method: 'POST' ,
            url: '/api/guestbook' ,
            data: {
                "targetUserEmail": targetUserEmail ,
                "content": content
            }
        })
        .then( res => {
            // Jwt 반환
            if ( res.data.statusCode === status.POST_SUCCESS ){
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
function registerQnA( {content,type,title,tags}){
    return new Promise((resolve,reject) => {
        request({
            method: 'POST' ,
            url: '/api/post',
            params: {
                title: title ,
                tags: tags ,
                content: content ,
                category: type ,
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

// GET - Post 검색
function getSearchedPost({ sentence }){
    sentence = sentence.replace("[","%").replace("]","%");
    return new Promise((resolve,reject) => {
        request({
            method: 'GET' ,
            url: '/api/search',
            params: {
                sentence: sentence
            }
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

// PUT - Post 조회수+1
function updatePost(id){
    return new Promise((resolve,reject) => {
        request({
            method: 'PUT' ,
            url: '/api/post',
            params: {
                id: id
            }
        })
        .then( res => {
            if ( res.data.statusCode == status.PUT_SUCCESS ){
                resolve();
            }
        })
        .catch( e => {
            console.log(e);
            // 실패 시 빈 리스트 리턴
            reject([]);
        })
    });
}

function registerUserWithNaver(code, state){
    return new Promise((resolve,reject) => {
        request({
            method: 'POST',
            url : `/user/register/naver`,
            params: {
                state : state,
                code: code,
            },
        })
        .then( res => {
            // Jwt 반환
            if ( res.data.statusCode === status.POST_SUCCESS ){
                resolve (res.headers.auth);
            }
        })
        .catch( e => {
            console.log(e);
            reject();
        })
    });
}

function getUser(){
    return new Promise((resolve,reject) => {
        request({
            method: 'GET',
            url : '/api/user',
        })
        .then( res => {
            resolve(res.data.data);
        })
        .catch( e => {
            console.log(e);
            reject();
        })
    });
}

function registerPost(formdata, title, totalContent, tags){
    if(formdata.get('files') === null){
        return new Promise((resolve,reject) => {
            request({
                method: 'POST',
                url : '/api/post',
                params: {
                    content: encodeURIComponent(totalContent),
                    category: "BLOG",
                    title: encodeURIComponent(title),
                    tags: encodeURIComponent(tags),
                }
            })
            .then( res => {
                if ( res.data.statusCode === status.POST_SUCCESS ){
                    resolve(res.data);
                }
            })
            .catch( e => {
                console.log(e);
                reject();
            })
        });
    }
    else{
        const token = window.sessionStorage.getItem("Auth");
        formdata.append('content', encodeURIComponent(totalContent));
        formdata.append('category', "BLOG");
        formdata.append('title', encodeURIComponent(title));
        formdata.append('tags', encodeURIComponent(tags));
        return new Promise((resolve,reject) => {
            if(token === null){
                reject();
            }
            else{
                axios.post('/api/post', formdata, {
                    headers: {
                        'content-type': 'multipart/form-data',
                        Auth: token
                    }
                })
                .then( res => {
                    if ( res.data.statusCode === status.POST_SUCCESS ){
                        resolve(res.data);
                    }
                })
                .catch( e => {
                    console.log(e);
                    reject();
                })
            }
        });
    }
    
}

function getBlogPosts(email){
    return new Promise((resolve,reject) => {
        request({
            method: 'GET',
            url : "/api/post/blogs",
            params: {
                email: email
            }
        })
        .then( res => {
            resolve(res.data.data);
        })
        .catch( e => {
            console.log(e);
            reject();
        })
    });
}

function deletePost(postId){
    return new Promise((resolve,reject) => {
        request({
            method: 'DELETE',
            url : "/api/post",
            params: {
                id: postId,
            }
        })
        .then( res => {
            resolve(res.data.data);
        })
        .catch( e => {
            console.log(e);
            reject();
        })
    });
}

function getPost(postId){
    return new Promise((resolve,reject) => {
        request({
            method: 'GET',
            url : "/api/post",
            params: {
                id: postId,
            }
        })
        .then( res => {
            resolve(res.data.data);
        })
        .catch( e => {
            console.log(e);
            reject();
        })
    });
}

function getOtherUser(email){
    return new Promise((resolve,reject) => {
        request({
            method: 'GET',
            url : '/api/user',
            params: {
                email: email
            }
        })
        .then( res => {
            resolve(res.data.data);
        })
        .catch( e => {
            console.log(e);
            reject();
        })
    });
}





export default { registerUserWithKakao, getGuestBooks, registerUserWithNaver, 
    getUser ,registerPost ,getAllQna,registerGuestBook,updateGuestBook , 
    getBlogPosts, registerQnA ,getSearchedPost ,updatePost, deletePost, getPost,
    getOtherUser} ;
