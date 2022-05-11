import React from "react";
import { useNavigate, useLocation } from 'react-router';
import { Box, Typography, IconButton, Button, TextField } from "@mui/material";
import { CommentOutlined, DeleteOutline, KeyboardArrowDownOutlined } from "@mui/icons-material";
import api from "../api/api";
import TotalBar from "../pages/MiniHomePage/TotalBar.js";
import Profile from "./MiniHomePage/Profile";
import styled from "styled-components";
import SmallUserProfile from "../component/SmallUserProfile";
import BlogComment from "../component/blog/BlogComment";
import strToHTML from "../function/strToHTML";

const Editor = styled.div`
    padding: 10px; 
    border: 1px solid lightgray;
    border-radius: 4px;
    height: 150px;
    overflow: auto;
    margin-top: 10px;
`;

const TitleBox = styled.div`
    flex: 1;
    /* align-items: center;
    justify-content: center; */
    border-bottom: 1px solid;
    border-color: lightgray;
`;

const ContentBox = styled.div`
    flex: 1;
    display: flex;
    padding-top: 20px;
    white-space: pre-wrap;
    padding-left: 10px;
    padding-right: 10px;
`;

function Blog(){
    const { state } = useLocation();
    const [post, setPost] = React.useState(null);
    const [isOwner, setIsOwner] = React.useState(false);
    const [isOpenComment, setIsOpenComment] = React.useState(false);
    const [isOpenCommentEditer, setIsOpenCommentEditor] = React.useState(false);
    const [myInfo, setMyInfo] = React.useState(null);

    React.useEffect(()=>{
        api.getPost(state.postId)
        .then(res=>{
            setIsOwner(res.isOwner);
            setPost(res);
        })
        .catch(e=>{
            console.log(e);
        })
    },[]);

    function PutImage(content, imageArray){
        const contentArray = content.split('&&&&');
        for(let i=0; i<contentArray.length; i++){
            return(
                <div>
                    <div>{contentArray[i]}</div>
                    {(i !== contentArray.length-1) && <img src={imageArray[i].storedUrl} variant={"contained"} alt={imageArray[i].storedUrl} style={{maxWidth: 800, maxHeight: 400}}/>}
                </div>
            )
        }
    }

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
        if(myInfo === null){
            getMyInfo();
            setIsOpenCommentEditor(!isOpenCommentEditer);
        }
        else{
            setIsOpenCommentEditor(!isOpenCommentEditer);
        }
    }

    function saveComment(){
        const divE = document.getElementById('Editor');
        if(divE === null){
            openCommentEditer();
            return;
        }
        let content = strToHTML(divE.innerHTML);
        let commentString = '';
        for(let i = 0; i < content.length; i++){
            if(i === 0) {
                commentString += content[i].textContent === '' ? '\n' : content[i].textContent+'\n';
            }
            else {
                commentString += content[i].innerHTML === '<br>' ? '\n' : content[i].innerText+'\n';
            }
        }
        if(commentString === '\n' || commentString === '' || commentString === ' ') {
            alert("댓글을 입력해주세요.");
        }
        else {
            api.registerPostComment(state.postId, commentString)
            .then((res)=>{
                setIsOpenCommentEditor(false);
            })
            .catch(e=>{
                alert("댓글 등록에 실패했습니다.");
            });
        }
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
                            <Typography sx={{fontWeight :'bold', fontSize: 20}}>{post.createdTime}</Typography>
                        </Box>
                    </TitleBox>

                    <ContentBox>
                        {PutImage(post.content, post.postImageResponseDtos)}
                    </ContentBox>

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
                                    <BlogComment postComment={item}/>
                                )
                            })}

                            {/* 댓글 작성  */}
                            <Box sx={{flex: 1, display: 'flex', flexDirection: 'column', mt: 3}} >
                                {(isOpenCommentEditer && myInfo !== null) && 
                                <Box sx={{bgcolor: 'white', pt: 2, pr: 1, pl: 1}}>
                                    <SmallUserProfile userInfo={myInfo}/>
                                    <Editor id="Editor" contentEditable="true"/>
                                    <Box sx={{flex: 1, display: 'flex', justifyContent: 'flex-end', mb: 2}}>
                                        <Typography>0/3000</Typography>
                                    </Box>
                                </Box>}
                                <Box sx={{flex: 1, display: 'flex'}}>
                                    <Box sx={{flex: 15, height: 50, bgcolor: 'white', display: 'flex', alignItems: 'center', pl: 5, border: 1, borderColor: 'lightgray'}} onClick={()=>{openCommentEditer()}}>
                                        {!isOpenCommentEditer && <Typography sx={{color: 'gray'}}>댓글을 입력해주세요.</Typography>}
                                    </Box>
                                    <Box sx={{flex: 1, height: 50, display: 'flex', alignItems: 'center', justifyContent: 'center', bgcolor: 'orange' }} onClick={()=>{saveComment()}}>
                                        <Typography sx={{color: 'white'}}>등록</Typography>
                                    </Box>
                                </Box>
                            </Box>
                        </Box>}

                </TotalBar>
            }
        </div>
    )
}

export default Blog;