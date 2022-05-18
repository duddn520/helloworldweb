import { Avatar, Button, Grid, Paper, Typography } from "@mui/material";
import { Box } from "@mui/system";
import React, { useEffect, useState } from "react";
import { Divider, styled } from "@mui/material";
import PostSubCommentTextBox from "./PostSubCommentTextBox";
import ThumbUpIcon from '@mui/icons-material/ThumbUp';
import { useNavigate } from 'react-router';

export default function QnAComment({postsubcomments, postCommentId}){

    const navigate = useNavigate();
    const [subComments, setSubComments] = React.useState([])
    const [likes, setLikes] = React.useState("")


    useEffect(()=>{
        if(postsubcomments){
            setSubComments(postsubcomments)
        }        

    },[])

    const renderContent = (content) => {
        if( content != null  ){
            let tmp = content?.split('```');
            return(
                tmp.map( (text,index) => {
                    // 본문
                    if( index % 2 == 0){
                        return(
                            <Typography sx={{ m: 2 }}>
                                <pre key={index} style={{ fontFamily: 'inherit' }}>{text}</pre>
                            </Typography>
                        );
                    }
                    // 코드
                    else {
                        text = text.replace('\n','');
                        return(
                            <Typography key={index} sx={{ p: 2 ,backgroundColor: 'rgb(240,240,240)' }}>
                                <pre style={{ fontFamily: 'inherit' }}>{text}</pre>
                            </Typography>
                        );
                    }
                })
            );
        }
    }

    return(
    <Box>
        <Divider variant="fullWidth" sx={{ flexGrow: 1}}/>
        <Grid container spacing={2}>

            <Grid item xs={1}>
                <Box sx={{
                    m:4,
                    display:"flex",
                    flexDirection:"column"

                }}>
                <Typography variant="h6">300</Typography>
                <ThumbUpIcon fontSize="large" />
                </Box>
            </Grid>
            <Grid item xs={11}>
                {subComments.map((postsubcomment,idx)=>{

                    if(idx===0){
                        return (
                            <div key={postsubcomment.id}>
                            <Box sx={{
                                minBlockSize:"60px"
                            }}>
                                {renderContent(postsubcomment.content)}
                            </Box>
                            <Box sx={{
                                flexGrow:1 ,
                                display: 'flex'
                            }}>
                                <Typography variant="caption">{postsubcomment.createdTime}</Typography>
                                <Box sx={{
                                    marginLeft:'auto'
                                }}>
                                    <Box onClick={() => navigate("/minihome", {state: {tabIndex: 0, targetEmail: postsubcomment.userResponseDto.email}}) } sx={{display: 'flex', alignItems: 'center', mb: 1}}>
                                      <Box sx={{width: 30, height: 30, borderRadius: 15, overflow: 'hidden', marginRight: 1, display: 'flex', alignItems: 'center'}}>
                                        <img src={postsubcomment.userResponseDto.profileUrl} width={30} height={30} alt={'프로필 사진'}></img>
                                    </Box>
                                        <Typography variant="caption">{postsubcomment.userResponseDto.userName}</Typography>
                                    </Box>
                                </Box>
                            </Box>
                            </div>
                        )
                    }
                    else{       //대댓글인 경우
                        return(
                            <div>
                            <Divider variant="fullWidth" sx={{ flexGrow: 1 }}/>
                            <Box sx={{
                                minBlockSize:"20px"
                            }}>
                                {renderContent(postsubcomment.content)}
                            </Box>
                            <Box sx={{
                                flexGrow:1 ,
                                display: 'flex'
                            }}>
                                <Typography variant="caption">{postsubcomment.createdTime}</Typography>
                                <Box sx={{
                                    marginLeft:'auto'
                                }}>
                                    <Box onClick={() => navigate("/minihome", {state: {tabIndex: 0, targetEmail: postsubcomment.userResponseDto.email}}) } sx={{display: 'flex', alignItems: 'center', mb: 1}}>
                                      <Box sx={{width: 30, height: 30, borderRadius: 15, overflow: 'hidden', marginRight: 1, display: 'flex', alignItems: 'center'}}>
                                        <img src={postsubcomment.userResponseDto.profileUrl} width={30} height={30} alt={'프로필 사진'}></img>
                                    </Box>
                                        <Typography variant="caption">{postsubcomment.userResponseDto.userName}</Typography>
                                    </Box>
                                </Box>

                            </Box>
                            </div>
                        
                            )
                    }
                    } 

                )
                }
                    <PostSubCommentTextBox postCommentId={postCommentId}/>
            </Grid>
        </Grid>
        <Divider variant="fullWidth" sx={{ flexGrow: 1 }}/>
    </Box> 
  )

     

}

