import React from "react";
import { useNavigate, useLocation } from 'react-router';
import { Box, Typography, IconButton } from "@mui/material";
import { DeleteOutline } from "@mui/icons-material";
import api from "../api/api";
import TotalBar from "../pages/MiniHomePage/TotalBar.js";
import Profile from "./MiniHomePage/Profile";
import styled from "styled-components";

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

    React.useEffect(()=>{
        api.getPost(state.postId)
        .then(res=>{
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

    function SmallUserProfile(userInfo){
        return(
            <Box sx={{display: 'flex', alignItems: 'center', marginBottom: 1}}>
                <Box sx={{width: 30, height: 30, borderRadius: 15, overflow: 'hidden', marginRight: 2}}>
                    <img src={userInfo.profileUrl} width={30} height={30} alt={'프로필 사진'}></img>
                </Box>
                <Typography>{userInfo.userName}</Typography>
            </Box>
        )
    }

    return(
        <div>
            {post === null ? 
            <div/> : 
                <TotalBar drawer={<Profile userInfo={post.userResponseDto}/>}>
                    <TitleBox>
                        <Box sx={{justifyContent: 'space-between', display: 'flex', width: '100%', alignItems: 'center', marginBottom: 1}}>
                            <Typography sx={{fontWeight :'bold', fontSize: 30}}>{post.title}</Typography>
                            <IconButton>
                                <DeleteOutline/>
                            </IconButton>
                        </Box>
                        {SmallUserProfile(post.userResponseDto)}
                        <Box sx={{justifyContent: 'space-between', display: 'flex', width: '100%', marginBottom: 1}}>
                            <Typography sx={{fontWeight :'bold', fontSize: 20, color: 'gray'}}>{'태그: '+post.tags}</Typography>
                            <Typography sx={{fontWeight :'bold', fontSize: 20}}>{post.createdTime}</Typography>
                        </Box>
                    </TitleBox>
                    <ContentBox>
                        {PutImage(post.content, post.postImageResponseDtos)}
                    </ContentBox>
                </TotalBar>
            }
        </div>
    )
}

export default Blog;