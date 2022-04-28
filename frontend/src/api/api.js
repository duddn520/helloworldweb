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
            data: {
                title: title ,
                tags: tags ,
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

function registerPost(formdata, title, totalContent){
    return new Promise((resolve,reject) => {
        request({
            method: 'POST',
            url : '/api/post',
            data: {
                formdata: formdata,
                content: totalContent,
                category: "BLOG",
                title: title,
            }
        })
        .then( res => {
            resolve(res.data);
        })
        .catch( e => {
            console.log(e);
            reject();
        })
    });
}

function getMyBlogPosts(){
    return new Promise((resolve,reject) => {
        request({
            method: 'GET',
            url : "/api/post/myblogs",
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





export default { registerUserWithKakao, getGuestBooks, registerUserWithNaver, getUser ,registerPost ,getAllQna,registerGuestBook,updateGuestBook , getMyBlogPosts, registerQnA} ;
