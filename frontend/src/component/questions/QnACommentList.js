import { Typography } from "@mui/material";
import React from "react";
import QnAComment from "./QnAComment";

export default function QnACommentList({postComments}){
    const [comments,setComments] = React.useState([]);
    
    React.useEffect(() => {
        if(postComments)
            setComments(postComments);
    },[]);

    return (
        <div>
            <Typography variant="h6" >{comments.length} Answers </Typography>
        {
        comments.map((postComment)=>{
            return(
                <QnAComment postsubcomments={postComment.postSubCommentResponseDtos} postCommentId={postComment.id}></QnAComment>
            )
        })
    }
    </div>
            
    )
}