import React from 'react';
import CustomAppBar from '../../component/appbar/CustomAppBar';
import { useLocation } from 'react-router';
import { Box ,Typography ,Button } from '@mui/material';
import QnAItem from '../../component/questions/QnAItem';
import { useNavigate } from 'react-router';

export default function Search(){
    const navigate = useNavigate();
    const { state } = useLocation();
    const [qnaList,setQnaList] = React.useState([]);
    const [blogList,setBlogList] = React.useState([]);
    
    // Post -> QNA,Blog로 구분
    React.useEffect(() => {
        let s1 = [];
        let s2 = [];
        state.res.map( item => {
            if ( item.category == "QNA" ) 
                s1.push(item);
            else
                s2.push(item);
        });
        setQnaList(s1);
        setBlogList(s2);
    },[]);

    return(
        <Box sx={{ flexGrow: 1 }}>
            <CustomAppBar />
            <Box sx={{ display: 'flex' ,flexDirection: 'row' }}>
                        <Typography sx={{ fontSize: 24 ,flex: 1 ,m: 3 }}>"{state.sentence}" 검색 결과</Typography>
                        <Button 
                            onClick={() => navigate('/qna/register')}
                            variant='contained' 
                            size='small' 
                            sx={{ m: 2 ,p: 1}}
                        >
                            질문 작성하기
                        </Button>
            </Box>
            <Box sx={{ alignItems: 'center' ,m: 2 ,flex: 1 }}>
                <Typography sx={{ m: 2 }}>질문</Typography>
                {
                    qnaList.map( item => {
                        return(
                            <QnAItem key={item} item={item}/>
                        );
                    })
                }
            </Box>
            <Box sx={{ alignItems: 'center' ,m: 2 ,flex: 1 }}>
                <Typography sx={{ m: 2 }}>블로그</Typography>
                {
                    blogList.map( item => {
                        return(
                            <QnAItem key={item} item={item}/>
                        );
                    })
                }
            </Box>
        </Box>
    );
}