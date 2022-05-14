import { Box, styled } from "@mui/system";
import React, { useEffect, useState } from "react";
import api from "../../api/api";
import { Button, Grid, Typography, Container, Card} from "@mui/material";

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

    const renderReplyPreview = () => {
        let tmp = reply.split("```");
        return(
            
                tmp.map( (text,index) => {
                    // 본문
                    if( index % 2 == 0){
                        return(
                            <pre style={{ fontFamily: "inherit" ,verticalAlign: 'middle' }}>
                                <Grid container spacing={2}>
                                {renderLine(text)}
                                </Grid>
                            </pre>
                        );
                    }
                    // 코드
                    else {
                        text = text.replace('\n','');
                        return(
                            <Typography key={index} sx={{ p: 2 ,backgroundColor: 'rgb(240,240,240)' }}>
                                <pre style={{ fontFamily: 'inherit' }}>{renderLine(text)}</pre>
                            </Typography>
                        );
                    }
                })
                
        );

    }

    const renderLine = (text) => {
        return(
            text.split("\n").map( line => {
                return(
                    <Grid item xs={12} sx={{ flexDirection: 'row' ,display: 'flex' ,justfiyContent: 'center' ,alignItems: 'center' }}>
                        {renderStrongText(line)}
                    </Grid>
                );
            })
        );
    }

    const renderStrongText = (value) => {
        let text = value;
        const regExp = /\*\*.{0,}\*\*/m;
        let s = [];
        
        // **text** 모두 찾을때까지
        while( text && text.length > 0 ){
            if( regExp.test(text) ){
                const startIndex = text.search(regExp);
                // normal text
                s.push({ "type" : "normal" , "text" : text.substring(0,startIndex) });
                let restText = text.substring(startIndex+2,text.length);
                const finIndex = restText.search(/\*\*/); 
                // // strong text
                s.push({ "type" : "strong", "text" : restText.substring(0,finIndex) })
                text = restText.substring(finIndex+2,restText.length);
            }
            else {
                s.push({ "type": "normal", "text" : text });
                text = "";
            }
        }

        // console.log(s);
        return(
                s.map( item => {
                    if( item.type === "normal") 
                        return <pre style={{ fontFamily: 'inherit' }}>{item.text}</pre>
                    else if( item.type === "strong")
                        return <b style={{ fontFamily: 'inherit' ,fontWeight: 'bold'}}>{item.text}</b>    
            })
        )

    }

    return (
        <Box>
            <Button onClick={()=>setState(!state)}>
                답글달기
            </Button>
           {state&& 
           <Card sx={{ flex:3}}>
               <Grid container spacing={2}>
           <Grid item xs={7}>
                <TextArea 
                    sx={{ width: '90%' ,m: 2 ,height: 350 , p : 2 ,borderColor: 'lightgray' ,borderRadius: 2 }}
                    value={reply}
                    onChange={value => setReply(value.target.value)}
                    size='small'
                    placeholder='e.g. 리액트 질문'
                />
            </Grid>
            <Grid item xs={5}>
                <Card sx={{ m:2 ,flex: 1 }}>
                        <Typography sx={{ fontWeight: 'bold', fontSize: 22 ,m: 2 ,justifyContent: 'center', display: 'flex' }}>Tips</Typography>
                    
                    {
                        tipContents.map( tip => {
                            return(
                                    <Card sx={{ m: 2 }}>
                                        <Container sx={{ backgroundColor: 'rgb(240,240,240)' ,p: 2 }}>
                                            <Typography sx={{ fontWeight: 'bold', fontSize: 17 }}>{tip.header}</Typography>
                                        </Container>
                                        <Typography sx={{ m:2 }}>
                                            <pre style={{ fontFamily: 'inherit'}}>
                                                {tip.body}
                                            </pre>
                                        </Typography>
                                    </Card>
                            );
                        })
                    }

                        
                    </Card>
            </Grid>
            </Grid>
            <Container>{renderReplyPreview()}</Container>
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

const TextArea = styled('textarea')({  

});
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