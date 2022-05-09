import React from 'react';
import { Tabs ,Box ,Tab ,Typography ,Divider ,Container ,styled ,Button ,Badge} from '@mui/material';
import { Search as SearchIcon } from '@mui/icons-material';
import { useLocation } from 'react-router';
import api from '../../api/api';
import CustomAppBar from '../../component/appbar/CustomAppBar';
import { useNavigate } from 'react-router';

export default function( props ){
    const navigate = useNavigate();
    const { state } = useLocation();
    const [value, setValue] = React.useState(1);
    const [qna,setQna] = React.useState({});
    // 답변
    const [reply,setReply] = React.useState("");
    // 태그
    const [tags,setTags] = React.useState([]);
    // 유저 이메일
    const [targetUserEmail,setTargetUserEmail] = React.useState("");

    const handleChange = (event, newValue) => {
        setValue(newValue);
    };

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
    
    React.useEffect(() => {
       setQna(state);

        // 조회수+1
        api.updatePost(state.id);

        api.getPost(state.id)
        .then( res => {
            setTargetUserEmail(res.userResponseDto.email);
        })
        .catch( e => { })

       if( state.tags != null ){
          setTags( state.tags.split(',') );
       }

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
                            <Typography sx={{ ml: 2 ,fontSize: 13}}>작성일 2022.04.28</Typography>
                            <Button onClick={() => navigate("/minihome", {state: {tabIndex: 0, targetEmail: targetUserEmail}}) }>이새끼 홈피로 이동해주세요.</Button>
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
                    <Typography sx={{ m : 2 ,fontSize: 25 ,fontWeight: '600' }}>{'당신의 답변'}</Typography>
                    <Box>
                        <TextArea
                            sx={{ width: '60%' ,m: 2 ,height: 300 , p : 2 ,borderColor: 'lightgray' ,borderRadius: 2 }}
                            value={reply}
                            onChange={value => setReply(value.target.value)}
                            size='small'
                            placeholder='e.g. 리액트 질문'
                        />
                    </Box>
                    <Box>
                        <Button 
                            variant='contained' 
                            sx={{ m: 2 ,mb: 10 }}
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