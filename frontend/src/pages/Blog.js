import React from "react";
import { useNavigate, useLocation } from 'react-router';
import { Box, Typography, IconButton, Button, Menu, MenuItem,  } from "@mui/material";
import { CommentOutlined, DeleteOutline, KeyboardArrowDownOutlined, MoreVertOutlined } from "@mui/icons-material";
import api from "../api/api";
import TotalBar from "../pages/MiniHomePage/TotalBar.js";
import Profile from "./MiniHomePage/Profile";
import styled from "@emotion/styled";
import SmallUserProfile from "../component/SmallUserProfile";
import BlogComment from "../component/blog/BlogComment";

import CommentEditor from "../component/blog/CommentEditor";
import strToHTML from "../function/strToHTML";

const TitleBox = styled.div`
    flex: 1;
    border-bottom: 1px solid;
    border-color: lightgray;
`;

const ContentBox = styled.div`
    flex: 1;
    padding-top: 20px;
    white-space: pre-wrap;
    padding-left: 10px;
    padding-right: 10px;
`;

function Blog(){
    const { state } = useLocation();
    const navigate = useNavigate();

    const [post, setPost] = React.useState(null);
    const [isOwner, setIsOwner] = React.useState(false);
    const [isOpenComment, setIsOpenComment] = React.useState(false);
    const [isOpenCommentEditer, setIsOpenCommentEditor] = React.useState(false);
    const [myInfo, setMyInfo] = React.useState(null);
    const [reRender, setReRender] = React.useState(true);

    React.useEffect(()=>{
        api.getPost(state.postId)
        .then(res=>{
            setIsOwner(res.isOwner);
            setPost(res);
            let divB = document.getElementById('BlogContent');
            if(strToHTML( divB.innerHTML ).length == 0){
                let imageIndex = 0;
                if(res !== null && res !== undefined){
                    const contentArray = res.content.split("\n");
                    for (let i = 0 ; i < contentArray.length ; i += 1) {
                        let currentLine = contentArray[i];

                        if(currentLine == '&&&&'){
                            let currentImg = res.postImageResponseDtos[imageIndex];
                            let imgNode = document.createElement('img');
                            imgNode.setAttribute("src", currentImg.storedUrl);
                            imgNode.setAttribute("name", currentImg.originalFileName);
                            imgNode.setAttribute("base64", currentImg.base64); // base64 인코딩된 값
                            imgNode.setAttribute("variant", "contained");
                            imgNode.setAttribute("alt", currentImg.storedUrl);
                            imgNode.style.maxWidth = '300px';
                            imgNode.style.maxHeight = 'auto';
                            divB.appendChild(imgNode);
                            imageIndex += 1;
                        }
                        else {
                            let divNode = document.createElement('div');
                            divNode.append(contentArray[i]);
                            divB.appendChild(divNode);
                        }
                    }
                }
            }
            
        })
        .catch(e=>{
            console.log(e);
        })
    },[reRender]);

    function getMyInfo(){
        api.getUser()
        .then(res => {
            setMyInfo(res);
        })
        .catch(e=>{
            alert("사용자 프로필을 조회하지 못했습니다.");
        })
    }

    function openCommentEditer(){
        if(myInfo === null) getMyInfo();
        setIsOpenCommentEditor(!isOpenCommentEditer);
    }

    function afterSaveComment(){
        setIsOpenCommentEditor(false);
        setReRender(!reRender);
    }

    function movetoUpdatePost(){
        navigate("/minihome/write", {state: {targetEmail: post.userResponseDto.email, post: post}});
    }

    return(
        <div>
            {post === null ? 
            <div/> : 
                <TotalBar drawer={<Profile userInfo={post.userResponseDto}/>}>
                    <TitleBox>
                        <Box sx={{justifyContent: 'space-between', display: 'flex', width: '100%', alignItems: 'center', marginBottom: 1}}>
                            <Typography sx={{fontWeight :'bold', fontSize: 30}}>{post.title}</Typography>
                            {isOwner && <IconButton>
                                <DeleteOutline/>
                            </IconButton>}
                        </Box>
                        <SmallUserProfile userInfo={post.userResponseDto}/>
                        <Box sx={{justifyContent: 'space-between', display: 'flex', width: '100%', marginBottom: 1}}>
                            <Typography sx={{fontWeight :'bold', fontSize: 20, color: 'gray'}}>{'태그: '+post.tags}</Typography>
                            <Typography sx={{fontWeight :'bold', fontSize: 15}}>{(post.modifiledTime === null || post.modifiedTime === post.createdTime) ? post.createdTime : post.modifiedTime+' (수정됨)'}</Typography>
                        </Box>
                    </TitleBox>

                    <ContentBox id="BlogContent"/>

                    <Box sx={{borderBottom: 1, borderColor: 'lightgray', mt: 2, padding: 1}}>
                        <Box sx={{display: 'flex', border: 1, padding: 1, borderColor: 'gray', borderRadius: 2, width: 120, height: 30 }} onClick={()=>{setIsOpenComment(!isOpenComment)}}>
                            <Box sx={{borderRight: 1, display: 'flex', width: 80, justifyContent: 'center', alignItems: 'center'}}>
                                <CommentOutlined sx={{ mr: 1, fontSize: 20}}/>
                                <Typography sx={{fontSize: 15}}>댓글</Typography>
                            </Box>
                            <Box sx={{width: 40, display: 'flex', justifyContent: 'center', alignItems: 'center'}}>
                                <Typography sx={{fontSize: 15}}>{post.postCommentResponseDtos.length}</Typography>
                                <KeyboardArrowDownOutlined sx={{fontSize: 20}}/>
                            </Box>
                        </Box>
                    </Box>

                    {isOpenComment && 
                        <Box sx={{backgroundColor: '#f7f7f7', padding: 5}}>
                            {post.postCommentResponseDtos.map((item)=>{
                                return (
                                    <BlogComment postComment={item} MyInfo={myInfo} key={item.id} afterSaveSubComment={()=>{afterSaveComment()}}/>
                                )
                            })}
                            <CommentEditor myInfo={myInfo} 
                            isOpenCommentEditer={isOpenCommentEditer} 
                            openCommentEditer={()=>{openCommentEditer()}} 
                            postId={state.postId} 
                            afterSaveComment={()=>{afterSaveComment()}} 
                            commentType={'COMMENT'}
                            modify={false}/>
                        </Box>}
                    <Box sx={{flex: 1, justifyContent: 'flex-end', display: 'flex', mt: 2}}>
                        {isOwner && <Button onClick={movetoUpdatePost} variant="outlined" sx={{width: 100}}>수정</Button>}
                    </Box>

                </TotalBar>
            }
        </div>
    )
}

export default Blog;