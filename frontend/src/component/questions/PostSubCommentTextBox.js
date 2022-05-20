import { Box} from "@mui/system";
import React, { useState } from "react";
import api from "../../api/api";
import { Button, Grid, Card} from "@mui/material";
import CommentTextArea from "./CommentTextArea";

export default function PostSubCommentTextBox({postCommentId}){

    const [state, setState] = useState(false)
    // const [value, setValue] = React.useState(1)
    const [reply,setReply] = React.useState("")

    function registerPostSubComment(){
        api.registerPostSubComment(postCommentId,reply)
        .then(res=>{
            console.log(res);
            window.location.reload()
        }).catch(e=>{
            console.log(e);
        })
    }

    return (
        <Box sx={{ m: 1 }}>
            <Button onClick={()=>setState(!state)}>
                답글달기
            </Button>
           {state&& 
           <Card sx={{ flex:3}}>
               <Grid container spacing={2}>
                    <Grid item xs={12}>
                            <CommentTextArea content={reply} setContent={setReply} />
                    </Grid>
                </Grid>
            <Box>
                <Button 
                    variant='contained' 
                    sx={{ m: 2 ,mb: 3 }}
                    onClick={()=>registerPostSubComment()}
                >
                    작성하기
                </Button>
            </Box>
            </Card>
            }
        </Box>
    )
}

const tipContents = [
    {
        header: '코드작성( ``` )',
        body: '```\nyour text here\n```'
    } ,
    {
        header: '강조( ** )',
        body: '**text here**'
    } ,
]