import React from 'react';
import { Tabs ,Box ,Tab ,Typography ,Divider ,Container ,styled ,Button ,Badge} from '@mui/material';
import { Search as SearchIcon } from '@mui/icons-material';
import { useLocation } from 'react-router';
import CustomAppBar from '../../component/appbar/CustomAppBar';

export default function( props ){
    const { state } = useLocation();
    const [value, setValue] = React.useState(1);
    const [qna,setQna] = React.useState({});
    // 답변
    const [reply,setReply] = React.useState("");
    // 태그
    const [tags,setTags] = React.useState([]);

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
                            <Typography sx={{ m: 1 }}>
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
                    <Typography sx={{ m : 2 ,ml: 4 ,fontSize: 25 ,fontWeight: '600'}}>{'제목1'}</Typography>
                    <Divider variant="fullWidth" sx={{ flexGrow: 1 }}/>
                    <Container>
                        {renderContent()}
                    </Container>
                    {
                        tags.map( tag => { if( tag.length ) return <Badge sx={{ backgroundColor: 'rgb(240,240,240)' ,ml: 4 ,p: 0.5,mt: 10,fontSize: 13}}>{tag}</Badge>})
                    }

                    <Typography sx={{ m : 3 ,fontSize: 25 ,mt: 10 ,fontWeight: '600' }}>{'당신의 답변'}</Typography>
                    <TextArea
                        sx={{ width: '90%' ,m: 2 ,height: 300 , p : 2 ,borderColor: 'lightgray' }}
                        value={reply}
                        onChange={value => setReply(value.target.value)}
                        size='small'
                        placeholder='e.g. 리액트 질문'
                    />
                    <Button 
                        variant='contained' 
                        sx={{ m: 2 ,mb: 10 }}
                    >
                        작성하기
                    </Button>
                </Container>

            </Box>
        </Box>
    );
}

const TextArea = styled('textarea')({  

});