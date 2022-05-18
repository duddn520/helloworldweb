import React from 'react';
import { Tabs ,Box ,Tab ,Typography ,Divider ,Container ,Button , Grid, Card ,Avatar ,IconButton} from '@mui/material';
import { Search as SearchIcon } from '@mui/icons-material';
import { useLocation } from 'react-router';
import api from '../../api/api';
import CustomAppBar from '../../component/appbar/CustomAppBar';
import { useNavigate } from 'react-router';
import QnACommentList from '../../component/questions/QnACommentList';
import CommentTextArea from '../../component/questions/CommentTextArea';
import MDEditor from '@uiw/react-md-editor';
import SmallUserProfile from '../../component/SmallUserProfile';


export default function QnA( props ){
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
    // 프로필 사진
    const [userProfileUrl,setUserProfileUrl] = React.useState("");
    // 유저이름
    const [userName,setUserName] = React.useState("");


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
    
    React.useEffect(() => {
        // naivgate state가 없을 경우
       if( state === null){
            api.getPost( props.id )
            .then( res =>{
                console.log(res);
            })
       } else {
            setQna(state);

        api.getPost(state.id)
        .then( res => {
            console.log(res)
            setPostComment(res.postCommentResponseDtos)
            setTargetUserEmail(res.userResponseDto.email);
            setUserProfileUrl(res.userResponseDto.profileUrl);
            setUserName(res.userResponseDto.userName);
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
                            {/* <Box onClick={() => navigate("/minihome", {state: {tabIndex: 0, targetEmail: targetUserEmail}})} sx={{marginLeft:'auto'}}>
                                <SmallUserProfile  userInfo={writer}></SmallUserProfile>
                            </Box> */}
                        </Box>
                    </Box>
                    <Divider variant="fullWidth" sx={{ flexGrow: 1 }}/>
                    <Box sx={{ ml: 2 }}>
                        <MDEditor.Markdown source={qna.content} style={{ marginTop: 5 }}/>
                    </Box>
                    {
                        tags.map( tag => { if( tag.length ) return <Button sx={{ backgroundColor: 'lightblue' ,borderRadius: 1,ml: 2 ,p: 0.5,mt: 10,fontSize: 13 ,textTransform: 'none' }}>{tag}</Button>})
                    }
                    <Box sx={{ display: 'flex'}}>
                        <Box sx={{ flex: 9}}></Box>
                        <Box sx={{ flex: 1 ,ml: 'auto' ,display: 'flex' ,flexDirection: 'row' ,alignItems: 'center' ,backgroundColor: 'lightblue' ,borderRadius: 1}}>
                            <IconButton>
                            <Avatar 
                                onClick={() => navigate("/minihome", {state: {tabIndex: 0, targetEmail: targetUserEmail}}) } 
                                src={userProfileUrl} 
                                sx={{ borderRadius: 2 ,m: 0.5 }}    
                            />
                            </IconButton>
                            <Box sx={{ alignItems: 'start' ,flexGrow: 1 }}>
                                <Typography sx={{ fontSize: 10 ,ml: 0.5 }}>Asked By</Typography>
                                <Button 
                                    onClick={() => navigate("/minihome", {state: {tabIndex: 0, targetEmail: targetUserEmail}}) }
                                    sx={{ ml: 0.5 ,p: 0,pr: 0.5 ,textAlign: 'start' ,justifyContent: 'start' ,textTransform: 'none'}}
                                >
                                    {userName}
                                </Button>
                            </Box>
                        </Box>
                    </Box>
                    
                    {/* <Divider variant="fullWidth" sx={{ flexGrow: 1 ,mt: 10}}/> */}
                    {
                        postComment.length ?  <QnACommentList postComments={postComment} /> : <Box/>
                    }
                    
                    <Typography sx={{ m : 2 ,fontSize: 25 ,fontWeight: '600' }}>{'당신의 답변'}</Typography>
                    <Grid container spacing={2}>
                        <Grid item xs={12}>
                            <Box>
                                <CommentTextArea content={reply} setContent={setReply} />
                            </Box>
                        </Grid>
                        {/* <Grid item xs={4}>
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
                        </Grid> */}
                    </Grid>
                    
                    <Container>
                        <MDEditor.Markdown source={reply} style={{ marginTop: 1 }}/>
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