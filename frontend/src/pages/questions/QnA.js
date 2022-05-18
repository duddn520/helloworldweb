import React from 'react';
import { Tabs ,Box ,Tab ,Typography ,Divider ,Container ,styled ,Button ,Badge, Grid, Card} from '@mui/material';
import { Search as SearchIcon } from '@mui/icons-material';
import { useLocation } from 'react-router';
import api from '../../api/api';
import CustomAppBar from '../../component/appbar/CustomAppBar';
import { useNavigate } from 'react-router';
import QnAComment from '../../component/questions/QnAComment';
import QnACommentList from '../../component/questions/QnACommentList';
import SmallUserProfile from '../../component/SmallUserProfile';

export default function( props ){
    const navigate = useNavigate();
    const { state } = useLocation();
    const [value, setValue] = React.useState(1);
    const [qna,setQna] = React.useState({});
    const [writer, setWriter] = React.useState({});
    // 답변
    const [reply,setReply] = React.useState("");
    const [postComment,setPostComment] = React.useState([]);
    // 태그
    const [tags,setTags] = React.useState([]);
    // 유저 이메일
    const [targetUserEmail,setTargetUserEmail] = React.useState("");
    //refresh


    const handleChange = (event, newValue) => {
        setValue(newValue);
    };

    function registerPostComment(postId,content){
        api.registerPostComment(postId,content)
        .then(res=>{
            console.log(res)
            window.location.reload()
        }).catch(e=>{
            console.log(e)
        })
    }

    const renderContent = () => {
        if( qna?.content != null  ){
            let tmp = qna?.content?.split('```');
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
    
    React.useEffect(() => {
        // naivgate state가 없을 경우
       if( state == null){
            api.getPost( props.id )
            .then( res =>{
                console.log(res);
            })
       } else {
            setQna(state);
        
            // 조회수+1
            api.updatePost(state.id);

        // 조회수+1
        api.updatePost(state.id);

        api.getPost(state.id)
        .then( res => {
            console.log(res)
            setPostComment(res.postCommentResponseDtos)
            setTargetUserEmail(res.userResponseDto.email)
            setWriter(res.userResponseDto)
        })
        .catch( e => { })
    
       if( state.tags != null ){
          setTags( state.tags.split(',') );
       }}

    },[]);


    return(
        <Box sx={{ flexGrow: 1 }}>
            
            <CustomAppBar />
            <Box
                sx={{ flexGrow: 1, bgcolor: 'background.paper', display: 'flex'}}
            >
                <Tabs
                    orientation="vertical"
                    variant="scrollable"
                    value={value}
                    onChange={handleChange}
                    sx={{ borderRight: 1, borderColor: 'lightgray' ,width: 200 }}
                >
                    <Tab label="홈" href="/" sx={{ display: 'flex' ,flexDirection: 'row'}} />
                    <Tab label="Questions" href="/" icon={<SearchIcon sx={{ m: 1 ,width: 20 ,height: 20}}/>} sx={{ display: 'flex' ,flexDirection: 'row' ,textAlign: 'center' ,textTransform: 'none' }}  />
                </Tabs>

                <Container>
                    <Box sx={{ justifyContent: 'start' }}>
                        <Typography sx={{ m : 1 ,ml: 2 ,fontSize: 25 ,fontWeight: '600'}}>{qna.title}</Typography>
                        <Box sx={{ display: 'flex' ,flexDirection: 'row' ,mb: 2 ,alignItems: 'center'}}>
                            <Typography sx={{ ml: 2 ,fontSize: 13}}>조회수 {qna.views}</Typography>
                            <Typography sx={{ ml: 2 ,fontSize: 13}}>작성일 {qna.createdTime}</Typography>
                            <Box onClick={() => navigate("/minihome", {state: {tabIndex: 0, targetEmail: targetUserEmail}})} sx={{marginLeft:'auto'}}>
                            <SmallUserProfile  userInfo={writer}></SmallUserProfile>
                            </Box>
                        </Box>
                    </Box>
                    <Divider variant="fullWidth" sx={{ flexGrow: 1 }}/>
                    <Box>
                        {renderContent()}
                    </Box>
                    {
                        tags.map( tag => { if( tag.length ) return <Badge sx={{ backgroundColor: 'rgb(240,240,240)' ,ml: 2 ,p: 1,mt: 10,fontSize: 13}}>{tag}</Badge>})
                    }
                    <Divider variant="fullWidth" sx={{ flexGrow: 1 ,mt: 10}}/>
                    {
                        postComment.length && <QnACommentList postComments={postComment} />
                    }
                    <Typography sx={{ m : 2 ,fontSize: 25 ,fontWeight: '600' }}>{'당신의 답변'}</Typography>
                    <Grid container spacing={2}>
                        <Grid item xs={8}>
                            <Box>
                                <TextArea
                                    sx={{ width: '80%' ,m: 2 ,height: 350 , p : 2 ,borderColor: 'lightgray' ,borderRadius: 2 }}
                                    value={reply}
                                    onChange={value => setReply(value.target.value)}
                                    size='small'
                                    placeholder='e.g. 리액트 질문'
                                />
                            </Box>
                        </Grid>
                        <Grid item xs={4}>
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
                    
                    <Container>
                        {
                            renderReplyPreview()
                        }
                    </Container>
                    <Box>
                        <Button 
                            variant='contained' 
                            sx={{ m: 2 ,mb: 10 }}
                            onClick={()=>registerPostComment(state.id,reply)}
                        >
                            작성하기
                        </Button>
                    </Box>
                </Container>

            </Box>
        </Box>
    );
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