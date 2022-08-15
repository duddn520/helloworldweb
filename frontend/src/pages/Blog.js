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
import axios from "axios";

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
        })
        .catch(e=>{
            console.log(e);
        })
    },[reRender]);

    React.useEffect(()=>{
        api.getUser()
        .then(res => {
            setMyInfo(res);
        })
        .catch(e=>{
            alert("사용자 프로필을 조회하지 못했습니다.");
        })
    },[])


    function openCommentEditer(){
        if(myInfo === null);
        setIsOpenCommentEditor(!isOpenCommentEditer);
    }

    function afterSaveComment(){
        setIsOpenCommentEditor(false);
        setReRender(!reRender);
    }

    function movetoUpdatePost(){
        navigate("/minihome/write", {state: {targetEmail: post.userResponseDto.email, post: post}});
    }
    function deleteThisPost(){
        api.deletePost(post.id)
        .then(res=>{
            navigate(`/minihome/`, {state: {tabIndex: 0, targetEmail: myInfo.email}});
        })
        .catch(e=>{
            console.log(e);
        })
    }

    function timeToString(s){
        let date = s.split("T")[0];
        let time = s.split("T")[1];
        let hm = time.split(":")[0] +":"+ time.split(":")[1];

        return  date+ " "+hm;
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
                                <DeleteOutline onClick={()=>{deleteThisPost()}}/>
                            </IconButton>}
                        </Box>
                        <SmallUserProfile userInfo={post.userResponseDto}/>
                        <Box sx={{justifyContent: 'space-between', display: 'flex', width: '100%', marginBottom: 1}}>
                            <Typography sx={{fontWeight :'bold', fontSize: 20, color: 'gray'}}>{'태그: '+post.tags}</Typography>
                            {/* <Typography sx={{fontWeight :'bold', fontSize: 15}}>{(post.modifiedTime === post.createdTime) ? timeToString(post.createdTime) : timeToString(post.modifiedTime)+' (수정됨)'}</Typography> */}
                            <Typography sx={{fontWeight :'bold', fontSize: 15}}>{timeToString(post.createdTime)}</Typography>
                        </Box>
                    </TitleBox>

                    <ContentBox id="BlogContent" dangerouslySetInnerHTML={ {__html: post.content} }/>

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
                    <Box sx={{flex: 1, justifyContent: 'flex-end', display: 'flex', mt: 2, mb: 2}}>
                        {isOwner && <Button onClick={movetoUpdatePost} variant="outlined" sx={{width: 100}}>수정</Button>}
                    </Box>

                </TotalBar>
            }
        </div>
    )
}

export default Blog;