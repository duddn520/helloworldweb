import { Button, Grid, Typography } from "@mui/material";
import { Box } from "@mui/system";
import React, { useEffect } from "react";
import { Divider,} from "@mui/material";
import PostSubCommentTextBox from "./PostSubCommentTextBox";
import MDEditor from '@uiw/react-md-editor';
import ThumbUpIcon from '@mui/icons-material/ThumbUp';
import CheckIcon from '@mui/icons-material/Check'
import api from "../../api/api";
import { useNavigate } from 'react-router';
import AlertDialog from "../dialog/AlertDialog";
import { green } from "@mui/material/colors";



export default function QnAComment({postsubcomments, postComment, Solved, Owner}){
    const [subComments, setSubComments] = React.useState([]);
    const [isSelected, setIsSelected] = React.useState("");


    useEffect(()=>{
        if(postsubcomments){
            console.log(postComment)
            setSubComments(postsubcomments);
            setIsSelected(postComment.selected);
        }        

    },[])

    function selectPostComment(postCommentId){
        if(window.confirm("이 댓글을 채택하시겠습니까?"))
        {
            api.selectPostComment(postCommentId)
            .then(res=>{
                window.location.reload();
            }).catch(e=>{
                console.log(e)
            })
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
                    flexDirection:"column" ,
                    alignItems: 'center'
                }}>
                { Solved ? isSelected ? <CheckIcon color="success" fontSize="large"/>: <Box /> : Owner &&<CheckIcon color="disabled" fontSize="large" sx={{ "&:hover":{color:"green"}}} onClick={()=>{selectPostComment(postComment.id)}}/>}
                </Box>
            </Grid>
            <Grid item xs={11}>
                {subComments.map((postsubcomment,idx)=>{
                    if(idx===0){
                        return (
                                <QnACommentComponent postsubcomment={postsubcomment} boxsize="60px" />
                            );
                        }
                        else{       // 대댓글인 경우
                            return(
                                <Box sx={{ ml: 2 }}>
                                    <QnACommentComponent postsubcomment={postsubcomment} boxsize="20px" />
                                </Box>
                            )
                    }
                    } 
                )
                }
                    <PostSubCommentTextBox postCommentId={postComment.id}/>
            </Grid>
        </Grid>
        <Divider variant="fullWidth" sx={{ flexGrow: 1 }}/>
    </Box> 
  )

}

function QnACommentComponent({postsubcomment,boxsize}){
    const [editEnabled,setEditEnabled] = React.useState(false);
    const [newComment,setNewComment] = React.useState(postsubcomment.content);
    // 다이얼로그 드로어
    const [open,setOpen] = React.useState(false);
    const handleOpen = () => {
        setOpen(true);
    };

    const handleDelete = () => {
        // 삭제함수
        api.deletePostSubComment(postsubcomment.id)
        .then( res => {
            window.location.reload();
        })
        .catch( e => { })
    };

    const navigate = useNavigate();

    const handleSubmit = () => {
        api.updatePostSubComment(postsubcomment.id,newComment)
        .then( res => {
            window.location.reload();
        })
        .catch( e => { });
    };
    
    return(
        <div key={postsubcomment.id}>
            <Box sx={{ display: 'flex' ,flexDireciton: 'row' ,mt: 0.5 }}>
                <AlertDialog open={open} setOpen={setOpen} onAgree={handleDelete}/>
                <Box sx={{
                    minBlockSize: boxsize ,
                    flex: 4
                }}>
                    {
                        editEnabled ? 
                        <Box>
                            <MDEditor style={{ flex: 1 }} value={newComment} onChange={setNewComment} /> 
                            <Button variant="contained" sx={{ mt: 1 }} onClick={handleSubmit}>수정하기</Button>
                        </Box>
                        : <MDEditor.Markdown source={postsubcomment.content} style={{ marginTop: 5 }}/>
                    }
                </Box>
                {
                    postsubcomment.isOwner &&
                    <Box sx={{ ml: 'auto' ,display: 'flex' ,height: 30 , flex: 1 }}>
                        <Button  size="small" sx={{ ml: 'auto' , color: editEnabled ? 'gray' : 'default' }} onClick={() => setEditEnabled(!editEnabled)}>{ !editEnabled ? "수정" : "수정취소" }</Button>
                        <Button  size="small" sx={{ color: 'red' }} onClick={handleOpen}>삭제</Button>
                    </Box>
                }
            </Box>

            <Box sx={{
                flexGrow:1 ,
                display: 'flex' ,
                flexDireciton: 'row' ,
                alignItems: 'end' ,
                mt: 3
            }}>
                <Typography 
                    variant="caption"
                    sx={{ color: 'gray'}}
                >
                    {
                        postsubcomment.createdTime === postsubcomment.modifiedTime ?
                        postsubcomment.createdTime :
                        postsubcomment.modifiedTime + "(수정됨)"
                    }
                </Typography>
                <Box sx={{
                    marginLeft:'auto' ,
                    color: 'gray'
                }}>
                    <Button onClick={() => navigate("/minihome", {state: {tabIndex: 0, targetEmail: postsubcomment.userResponseDto.email}}) } sx={{display: 'flex', alignItems: 'center' ,textTransform: 'none'}}>
                        <Box sx={{width: 25, height: 25, borderRadius: 15, overflow: 'hidden', marginRight: 1, display: 'flex', alignItems: 'center'}}>
                            <img src={postsubcomment.userResponseDto.profileUrl} width={25} height={25} alt={'프로필 사진'}></img>
                        </Box>
                        <Typography variant="caption">{postsubcomment.userResponseDto.userName}</Typography>
                    </Button>
                </Box>
            </Box>
            <Divider variant="fullWidth" sx={{ flexGrow: 1 ,mt: 1}}/>
        </div>
    );
}



