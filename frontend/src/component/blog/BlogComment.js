import { Box, Typography } from "@mui/material";
import React from "react";

const BlogComment = ({postComment}) => {
    return (
        postComment.postSubCommentResponseDtos.map((comment)=>{
            return(
                <Box key={comment.id} sx={{flex: 1, display: 'flex', flexDirection: 'column', pt: 3, pb: 3, borderTop: 1, borderBottom: 1, borderColor: 'lightgray'}}>
                    <Typography>{comment.content}</Typography>
                    <Typography sx={{fontSize: 14, color: 'lightgray'}}>{comment.createdTime}</Typography>
                </Box>
            )
        })
    )
}

export default BlogComment;