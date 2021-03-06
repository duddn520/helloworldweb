import React from "react";
import { Box, IconButton } from "@mui/material";
import muiStyled from "@mui/system/styled";
import { Close, DeleteOutline } from "@mui/icons-material";
import styled from "styled-components";
import { useNavigate } from 'react-router';
import api from "../../api/api";

const TitleBox = styled.div`
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    font-size: 20px;
    font-weight: bold;
`;
const ContentBox = styled.div`
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    color: gray;
`;

const OnePost = muiStyled(Box)({
    flex: 1,
    alignItems: 'center',
    paddingTop: 10,
    paddingBottom: 10,
});

function PostThumbnail({post, isOwner}){
    const navigate = useNavigate();

    function makeThumbnailContent(content){
        // return content.replace(/<[^>]+>/g, ' ');
        let temp = content.replace(/<[^>]+>/g, '%').split('%');
        let result = "";
        let i;
        for (i = 0; i < temp.length; i++) {
            if(temp[i] !== ""){
                result = temp[i];
                break;
            }
        }
        return result;
        
    }

    function deletePost(postId){
        api.deletePost(postId)
        .then(res=>{
            window.location.reload();
        })
        .catch(e=>{
            console.log(e);
        })
    }

    function MoveToOnePost(id){
        navigate("/blog", {state: {postId: id}});
    }

    return(
        <OnePost sx={{borderBottom: 1, borderColor: 'lightgray'}} onClick={()=>{MoveToOnePost(post.id)}}>
            <Box sx={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}}>
                <TitleBox>{post.title}</TitleBox>
                {isOwner && <IconButton onClick={(event)=>{ event.stopPropagation(); deletePost(post.id); }}>
                    <DeleteOutline/>
                </IconButton>}
            </Box>
            <ContentBox>{makeThumbnailContent(post.content)}</ContentBox>
            <ContentBox>{(post.modifiedTime === null || post.modifiedTime === post.createdTime) ? post.createdTime.split('T')[0] : post.modifiedTime.split('T')[0]}</ContentBox>
            <ContentBox>{'views: '+post.views}</ContentBox>
        </OnePost>
    )
}

export default PostThumbnail;