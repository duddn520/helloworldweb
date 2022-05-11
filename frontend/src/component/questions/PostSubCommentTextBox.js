import { Box, styled } from "@mui/system";
import React, { useEffect, useState } from "react";
import api from "../../api/api";
import { Button } from "@mui/material";

export default function PostSubCommentTextBox({postCommentId}){

    const [state, setState] = useState(false)
    const [value, setValue] = React.useState(1)
    const [reply,setReply] = React.useState("")

    function registerPostSubComment(){
        api.registerPostSubComment(postCommentId,reply)
        .then(res=>{
            console.log(res)
        }).catch(e=>{
            console.log(e)
        })
    }

    return (
        <Box>
            <Button onClick={()=>setState(!state)}>
                답글달기
            </Button>
           {state&& 
           <Box>
           <Box >
                <TextArea 
                    sx={{ width: '80%' ,m: 2 ,height: 150 , p : 2 ,borderColor: 'lightgray' ,borderRadius: 2 }}
                    value={reply}
                    onChange={value => setReply(value.target.value)}
                    size='small'
                    placeholder='e.g. 리액트 질문'
                />
            </Box>
            <Box>
                <Button 
                    variant='contained' 
                    sx={{ m: 2 ,mb: 3 }}
                    onClick={()=>registerPostSubComment()}
                >
                    작성하기
                </Button>
            </Box>
            </Box>
            }
        </Box>
    )
}

const TextArea = styled('textarea')({  

});