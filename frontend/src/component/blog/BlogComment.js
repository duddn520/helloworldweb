import { Typography, Box } from "@mui/material";
import React from "react";
import { styled, experimental_sx as sx } from '@mui/system';
import CommentEditor from "./CommentEditor";
import api from "../../api/api";
import { ArrowForwardIos } from "@mui/icons-material";
import SmallUserProfile from "../SmallUserProfile";


const SubCommentBtn = styled('div')(
    sx({
        backgroundColor: 'white',
        width: 40,
        border: 1,
        borderRadius: 1,
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        marginTop: 2
    }),
  );

const CommentBundle = styled('div')(
    sx({
        flex: 1, 
        display: 'flex', 
        flexDirection: 'column', 
        paddingTop: 3, 
        paddingBottom: 3, 
        borderTop: 1, 
        borderBottom: 1, 
        borderColor: 'lightgray'
    }),
  );

  const SubCommentBundle = styled('div')(
    sx({
        flex: 1, 
        display: 'flex', 
        paddingTop: 3, 
        paddingBottom: 3, 
        borderTop: 1, 
        borderBottom: 1, 
        borderColor: 'lightgray',
    }),
  );

const BlogComment = ({postComment, MyInfo, afterSaveSubComment}) => {
    const [isOpenSubCommentEditor, setIsOpenSubCommentEditor] = React.useState(false);
    const [modifyTargetIndex, setModifyTargetIndex] = React.useState(null);
    const [myInfo, setMyInfo] = React.useState(MyInfo);

    function openCommentEditer(){
        setIsOpenSubCommentEditor(!isOpenSubCommentEditor);
    }

    function openCommentModifyEditor(index){
        setIsOpenSubCommentEditor(false);
        if(modifyTargetIndex === index) setModifyTargetIndex(null);
        else setModifyTargetIndex(index); 
    }

    return (
        <div>
            {postComment.postSubCommentResponseDtos.map((subComment, index)=>{
                if(index === 0){
                    return(
                        <div key={subComment.id}>
                            <CommentBundle>
                                <Box sx={{justifyContent: 'space-between', display: 'flex', width: '100%', alignItems: 'center', marginBottom: 1}}>
                                    <SmallUserProfile userInfo={subComment.userResponseDto}/>
                                    {subComment.isOwner && <Typography sx={{fontSize: 13, color: 'gray'}} onClick={()=>{openCommentModifyEditor(index)}}>수정</Typography>}
                                </Box>
                                <Typography>{subComment.content}</Typography>
                                <Typography sx={{fontSize: 14, color: 'lightgray'}}>{(subComment.modifiedTime === subComment.createdTime) ? subComment.createdTime : subComment.modifiedTime}</Typography>
                                <SubCommentBtn onClick={()=>{openCommentEditer()}}>
                                    <Typography sx={{fontSize: 12, fontWeight: 'bold'}}>답글</Typography>
                                </SubCommentBtn>
                            </CommentBundle>
                            {index === modifyTargetIndex && <CommentEditor myInfo={myInfo} isOpenCommentEditer={true} openCommentEditer={()=>{}} commentType={"SUBCOMMENT"} afterSaveComment={()=>{afterSaveSubComment(); setIsOpenSubCommentEditor(false);}} postCommentId={postComment.id} modify={true}/>}
                        </div>
                    )
                }
                else{
                    return(
                        <div key={subComment.id}>
                            <SubCommentBundle>
                                <Box sx={{height: 30, display: 'flex', alignItems: 'center'}}>
                                    <ArrowForwardIos sx={{color: 'gray', fontSize: 18}}/>
                                </Box>
                                <Box sx={{marginLeft: 1, flex: 1}}>
                                    <Box sx={{justifyContent: 'space-between', display: 'flex', width: '100%', alignItems: 'center', marginBottom: 1}}>
                                        <SmallUserProfile userInfo={subComment.userResponseDto}/>
                                        {subComment.isOwner && <Typography sx={{fontSize: 13, color: 'gray'}} onClick={()=>{openCommentModifyEditor(index)}}>수정</Typography>}
                                    </Box>
                                    <Typography>{subComment.content}</Typography>
                                    <Typography sx={{fontSize: 14, color: 'lightgray'}}>{(subComment.modifiedTime === subComment.createdTime) ? subComment.createdTime : subComment.modifiedTime}</Typography>
                                </Box>
                            </SubCommentBundle>
                            {index === modifyTargetIndex && <CommentEditor myInfo={myInfo} isOpenCommentEditer={true} openCommentEditer={()=>{}} commentType={"SUBCOMMENT"} afterSaveComment={()=>{afterSaveSubComment(); setIsOpenSubCommentEditor(false);}} postCommentId={postComment.id} modify={true}/>}
                        </div>
                    )
                }
                
            })}
            {isOpenSubCommentEditor && <CommentEditor myInfo={myInfo} isOpenCommentEditer={true} openCommentEditer={()=>{}} commentType={"SUBCOMMENT"} afterSaveComment={()=>{afterSaveSubComment(); setIsOpenSubCommentEditor(false);}} postCommentId={postComment.id} modify={false}/>}
            
        </div>
    )
}

export default BlogComment;