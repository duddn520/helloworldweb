import { Avatar, Button, Grid, Paper, Typography } from "@mui/material";
import { Box } from "@mui/system";
import React, { useEffect } from "react";
import { Divider,} from "@mui/material";
import PostSubCommentTextBox from "./PostSubCommentTextBox";
import MDEditor from '@uiw/react-md-editor';
import ThumbUpIcon from '@mui/icons-material/ThumbUp';
import { useNavigate } from 'react-router';

export default function QnAComment({postsubcomments, postCommentId}){

    const navigate = useNavigate();
    const [subComments, setSubComments] = React.useState([])
    // const [likes, setLikes] = React.useState("")


    useEffect(()=>{
        if(postsubcomments){
            setSubComments(postsubcomments)
        }        

    },[])


    return(
    <Box>
        <Divider variant="fullWidth" sx={{ flexGrow: 1}}/>
        <Grid container spacing={2}>

            <Grid item xs={1}>
                <Box sx={{
                    m:4,
                    display:"flex",
                    flexDirection:"column" ,
                    alignItems: 'center'
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
                                <MDEditor.Markdown source={postsubcomment.content} style={{ marginTop: 5 }}/>
                            </Box>
                            <Box sx={{
                                flexGrow:1 ,
                                display: 'flex'
                            }}>
                                <Typography 
                                    variant="caption"
                                    sx={{ color: 'gray'}}
                                >
                                        {postsubcomment.createdTime}
                                </Typography>
                                <Box sx={{
                                    marginLeft:'auto' ,
                                    color: 'gray'
                                }}>
                                    <Box onClick={() => navigate("/minihome", {state: {tabIndex: 0, targetEmail: postsubcomment.userResponseDto.email}}) } sx={{display: 'flex', alignItems: 'center', mb: 1}}>
                                      <Box sx={{width: 30, height: 30, borderRadius: 15, overflow: 'hidden', marginRight: 1, display: 'flex', alignItems: 'center'}}>
                                        <img src={postsubcomment.userResponseDto.profileUrl} width={30} height={30} alt={'프로필 사진'}></img>
                                    </Box>
                                        <Typography variant="caption">{postsubcomment.userResponseDto.userName}</Typography>
                                    </Box>
                                </Box>
                            </Box>
                            <Divider variant="fullWidth" sx={{ flexGrow: 1 ,mt: 1}}/>
                            </div>
                        )
                    }
                    else{       //대댓글인 경우
                        return(
                            <Box sx={{ ml: 2 }}>
                                <Box sx={{
                                    minBlockSize:"20px" ,
                                    mt: 1
                                }}>
                                    <MDEditor.Markdown source={postsubcomment.content} style={{ marginTop: 5 }}/>
                                </Box>
                                <Box sx={{
                                    flexGrow:1 ,
                                    display: 'flex' ,
                                    mt: 2
                                }}>
                                    <Typography 
                                        variant="caption"
                                        sx={{ color: 'gray' }}
                                    >
                                        {postsubcomment.createdTime}
                                    </Typography>
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
                                <Divider variant="fullWidth" sx={{ flexGrow: 1 ,mt: 1}}/>
                            </Box>
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

