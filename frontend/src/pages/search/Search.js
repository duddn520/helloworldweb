import React from 'react';
import CustomAppBar from '../../component/appbar/CustomAppBar';
import { useLocation } from 'react-router';
import { Box ,Typography ,Button } from '@mui/material';
import QnAItem from '../../component/questions/QnAItem';
import api from '../../api/api';
import { useNavigate ,useParams } from 'react-router';

export default function Search( props ){
    const navigate = useNavigate();
    const params = useParams();
    // const state = useLocation();
    const [qnaList,setQnaList] = React.useState([]);
    const [blogList,setBlogList] = React.useState([]);
    
    // Post -> QNA,Blog로 구분
    React.useEffect(() => {

        api.getSearchedPost({sentence : params.sentence , page: 0 })
            .then( res => {
                let s1 = [];
                let s2 = [];
                res.map( item => {
                    if ( item.category == "QNA" ) 
                        s1.push(item);
                    else
                        s2.push(item);
                });
                setQnaList(s1);
                setBlogList(s2); 
            })
            .catch( e => {
            });

        
    },[ ]);

    return(
        <Box sx={{ flexGrow: 1 }}>
            <CustomAppBar />
            <Box sx={{ display: 'flex' ,flexDirection: 'row' }}>
                        <Typography sx={{ fontSize: 24 ,flex: 1 ,m: 3 }}>"{params.sentence}" 검색 결과</Typography>
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