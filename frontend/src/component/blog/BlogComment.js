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
    const [myInfo, setMyInfo] = React.useState(MyInfo);

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
        setIsOpenSubCommentEditor(!isOpenSubCommentEditor);
    }

    return (
        <div>
            {postComment.postSubCommentResponseDtos.map((subComment, index)=>{
                if(index === 0){
                    return(
                        <CommentBundle key={subComment.id}>
                            <SmallUserProfile userInfo={subComment.userResponseDto}/>
                            <Typography>{subComment.content}</Typography>
                            <Typography sx={{fontSize: 14, color: 'lightgray'}}>{subComment.createdTime}</Typography>
                            <SubCommentBtn onClick={()=>{openCommentEditer()}}>
                                <Typography sx={{fontSize: 12, fontWeight: 'bold'}}>답글</Typography>
                            </SubCommentBtn>
                        </CommentBundle>
                    )
                }
                else{
                    return(
                        <SubCommentBundle key={subComment.id}>
                            <Box sx={{height: 30, display: 'flex', alignItems: 'center'}}>
                                <ArrowForwardIos sx={{color: 'gray', fontSize: 18}}/>
                            </Box>
                            <Box sx={{marginLeft: 1}}>
                                <SmallUserProfile userInfo={subComment.userResponseDto}/>
                                <Typography>{subComment.content}</Typography>
                                <Typography sx={{fontSize: 14, color: 'lightgray'}}>{subComment.createdTime}</Typography>
                            </Box>
                        </SubCommentBundle>
                    )
                }
                
            })}
            {isOpenSubCommentEditor && <CommentEditor myInfo={myInfo} isOpenCommentEditer={true} openCommentEditer={()=>{}} commentType={"SUBCOMMENT"} afterSaveComment={()=>{afterSaveSubComment(); setIsOpenSubCommentEditor(false)}} postCommentId={postComment.id}/>}
        </div>
    )
}

export default BlogComment;