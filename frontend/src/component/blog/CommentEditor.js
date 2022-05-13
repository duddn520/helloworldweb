import React from 'react';
import { Box, Typography, IconButton, Button, TextField } from "@mui/material";
import { CommentOutlined, DeleteOutline, KeyboardArrowDownOutlined } from "@mui/icons-material";
import SmallUserProfile from '../SmallUserProfile';
import styled from '@emotion/styled';
import strToHTML from '../../function/strToHTML';
import api from '../../api/api';

const Editor = styled.div`
    padding: 10px; 
    border: 1px solid lightgray;
    border-radius: 4px;
    height: 150px;
    overflow: auto;
    margin-top: 10px;
`;

function CommentEditor({myInfo, isOpenCommentEditer, openCommentEditer, postId, afterSaveComment, commentType, postCommentId}){

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
            if(commentType === 'COMMENT'){
                api.registerPostComment(postId, commentString)
                .then((res)=>{
                    afterSaveComment();
                })
                .catch(e=>{
                    alert("댓글 등록에 실패했습니다.");
                });
            }
            else{
                api.registerPostSubComment(postCommentId, commentString)
                .then((res)=>{
                    afterSaveComment();
                })
                .catch(e=>{
                    alert("답글 등록에 실패했습니다.");
                });
            }
            
        }
    }

    return (
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
                <Box sx={{flex: 15, height: 50, bgcolor: 'white', display: 'flex', alignItems: 'center', pl: 5, border: 1, borderColor: 'lightgray'}} onClick={openCommentEditer}>
                    {!isOpenCommentEditer && <Typography sx={{color: 'gray'}}>댓글을 입력해주세요.</Typography>}
                </Box>
                <Box sx={{flex: 1, height: 50, display: 'flex', alignItems: 'center', justifyContent: 'center', bgcolor: 'orange' }} onClick={saveComment}>
                    <Typography sx={{color: 'white'}}>등록</Typography>
                </Box>
            </Box>
            
        </Box>
    )
}

export default CommentEditor;