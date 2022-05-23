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
            url: '/api/user/register/kakao',
            headers: { 
                token: token
                // "Access-Control-Allow-Origin" : true
            },
        })
        .then( res => {
            // Jwt 반환
            if ( res.data.statusCode === status.POST_SUCCESS ){
                resolve (res.data.data);
            }
        })
        .catch( e => {
            console.log(e);
            reject();
        })
    });
}

// GET - 나의 방명록 조회
function getGuestBooks( email ){
    return new Promise((resolve,reject) => {
        request({
            method: 'GET' ,
            url: '/api/guestbook',
            params: { 
                email : email
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
                title: encodeURIComponent(title) ,
                tags: encodeURIComponent(tags) ,
                content: encodeURIComponent(content) ,
                category: encodeURIComponent(type) ,
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
function updatePostViews(id){
    return new Promise((resolve,reject) => {
        request({
            method: 'PUT' ,
            url: '/api/post/views',
            params: {
                post_id: id
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
            url : `/api/user/register/naver`,
            params: {
                state : state,
                code: code,
            },
        })
        .then( res => {
            // Jwt 반환
            if ( res.data.statusCode === status.POST_SUCCESS ){
                resolve (res.data.data);
            }
        })
        .catch( e => {
            console.log(e);
            reject();
        })
    });
}

//jwt로 자기자신의 정보를 조회
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
                post_id: postId,
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
                post_id: postId,
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

//PK 인 email을 사용하여 다른 유저의 정보를 조회하는 함수
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

function registerPostComment(postId, content){
    return new Promise((resolve,reject) => {
        request({
            method: 'POST',
            url : '/api/postcomment',
            data:{
                postId:postId,
                content:content
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

function getPostComment(postId){
    return new Promise((resolve,reject) => {
        request({
            method: 'GET',
            url : '/api/postcomment',
            params:{
                id:postId
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

function registerPostSubComment(postCommentId, content){
    return new Promise((resolve,reject) => {
        request({
            method: 'POST',
            url : '/api/postsubcomment',
            data:{
                postCommentId:postCommentId,
                content:content
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

function updateNickName(nickName){
    return new Promise((resolve,reject) => {
        request({
            method: 'PUT',
            url : '/api/user/nickname',
            params: {
                nickName: nickName
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

function getUserQnas(id){
    return new Promise((resolve,reject) => {
        request({
            method: 'GET',
            url : '/api/user/qnas',
            params: {
                id: id
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

function getUserComments(id){
    return new Promise((resolve,reject) => {
        request({
            method: 'GET',
            url : '/api/user/comments',
            params: {
                id: id
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

function getGithubRepositories(email){
    return new Promise((resolve,reject) => {
        request({
            method:"GET",
            url:"/api/user/repos_url",
            params:{
                email: email
            }
        })
        .then( res => {
            resolve(res);
        })
        .catch( e => {
            console.log(e);
            reject();
        })
    });
}

function registerUserWithGithub(code){
    return new Promise((resolve,reject) => {
        request({
            url:"/api/user/register/github",
            params:{
                "code":code
            },
            method:"POST"
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

function connectUserToGithub(code){
    return new Promise((resolve,reject) => {
        request({
            url:"/api/user/githubconnect",
            params:{
                "code":code
            },
            method:"POST"
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

function updatePost(postId, formdata, title, totalContent, tags) {
    if(formdata.get('files') === null){
        return new Promise((resolve,reject) => {
            request({
                method: 'PUT',
                url : '/api/post',
                params: {
                    post_id: postId,
                    content: encodeURIComponent(totalContent),
                    title: encodeURIComponent(title),
                    tags: encodeURIComponent(tags),
                }
            })
            .then( res => {
                if ( res.data.statusCode === status.PUT_SUCCESS ){
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
        formdata.append('post_id', postId);
        formdata.append('content', encodeURIComponent(totalContent));
        formdata.append('title', encodeURIComponent(title));
        formdata.append('tags', encodeURIComponent(tags));
        return new Promise((resolve,reject) => {
            if(token === null){
                reject();
            }
            else{
                axios.put('/api/post', formdata, {
                    headers: {
                        'content-type': 'multipart/form-data',
                        Auth: token
                    }
                })
                .then( res => {
                    if ( res.data.statusCode === status.PUT_SUCCESS ){
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

function getNewToken(accessToken,refreshToken){
    return new Promise((resolve,reject) => {
        axios({
            method: "GET" ,
            url:"/api/user/getnewtoken",
            headers: {
                "Auth" : accessToken ,
                "Refresh" : refreshToken 
            }
        })
        .then( res => {
            resolve(res.headers);
        })
        .catch( e => {
            reject();
        })
    }); 
}

function updatePostSubComment(id,content){
    return new Promise((resolve,reject) => {
        axios({
            method: "PUT" ,
            url:"/api/postsubcomment",
            data: {
                "id" : id ,
                "content" : content
            }
        })
        .then( res => {
            resolve(res.data.data);
        })
        .catch( e => {
            reject();
        })
    });  
}

function deletePostSubComment(id) {
    return new Promise((resolve,reject) => {
        axios({
            method: "DELETE" ,
            url:"/api/postsubcomment",
            params: {
                "id" : id ,
            }
        })
        .then( res => {
            resolve(res.data.data);
        })
        .catch( e => {
            reject();
        })
    });  
}

function selectPostComment(id) {
    return new Promise((resolve,reject) => {
        request({
            method: "POST" ,
            url:"/api/postcomment/select",
            params: {
                "id" : id ,
            }
        })
        .then( res => {
            resolve(res.data.data);
        })
        .catch( e => {
            reject();
        })
    });  
}



export default { registerUserWithKakao, getGuestBooks, registerUserWithNaver, 
    getUser ,registerPost ,getAllQna,registerGuestBook,updateGuestBook , 
    getBlogPosts, registerQnA ,getSearchedPost ,updatePost, deletePost, getPost,
    getOtherUser,registerPostComment,getPostComment,registerPostSubComment,updateNickName 
    ,getUserQnas ,getUserComments, getGithubRepositories, registerUserWithGithub, connectUserToGithub , getNewToken
    ,updatePostSubComment ,deletePostSubComment, selectPostComment
} ;
