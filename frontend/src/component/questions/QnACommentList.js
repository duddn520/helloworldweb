import { Typography } from "@mui/material";
import React from "react";
import QnAComment from "./QnAComment";

export default function QnACommentList({postComments, Solved, Owner}){
    const [comments,setComments] = React.useState([]);
    
    React.useEffect(() => {
        if(postComments)
            setComments(postComments);
    },[]);

    return (
        <div>
            <Typography variant="h6" sx={{ mt: 2 ,mb: 2}}>{comments.length} Answers </Typography>
        {
        comments.map((postComment)=>{
            return(
                <QnAComment postsubcomments={postComment.postSubCommentResponseDtos} postComment={postComment} Solved={Solved} Owner={Owner}></QnAComment>
            )
        })
    }
    </div>
            
    )
}