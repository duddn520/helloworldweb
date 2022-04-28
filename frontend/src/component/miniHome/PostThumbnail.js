import React from "react";
import { Box,Typography } from "@mui/material";
import { styled } from "@mui/system";

const OnePost = styled(Box)({
    flex: 1,
    height: 50,
    alignItems: 'center',
    marginTop: 1,
    paddingRight: 1,
    paddingLeft: 1,
    display: 'flex',
})

function PostThumbnail({id, title}){
    return(
        <OnePost sx={{borderBottom: 1, borderColor: 'gray'}}>
            <Typography>{title}</Typography>
        </OnePost>
    )
}

export default PostThumbnail;