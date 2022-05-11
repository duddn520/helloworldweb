import React from 'react';
import { Box ,Avatar ,Container ,Typography ,Button ,Badge } from '@mui/material';
import { useParams ,useNavigate} from 'react-router';
import api from '../../api/api';
import QnAItem from '../questions/QnAItem';
import CustomAppBar from '../appbar/CustomAppBar';
export default function({id , qnas }){
    return(
        <Box>
            <Container border={1} sx={{ display: 'flex' ,flexDirection: 'row' }}>
                <Box sx={{ flex: 1 ,m: 2}}>
                    <Typography sx={{ fontSize: 25 }}>작성한 질문</Typography>
                    <Box sx={{ flex: 1 ,mt: 2 }}>
                    {
                        qnas.map( qna => {
                            return(
                                <QnAItem item={qna} />
                            );
                        })
                    }
                    </Box>
                </Box>

                <Box sx={{ flex: 1 ,m: 2 }}>
                    <Typography sx={{ fontSize: 25 }}>작성한 답변</Typography>
                    <Box sx={{ flex: 1 ,mt: 2 }}>
                    {
                        qnas.map( qna => {
                            return(
                                <QnAItem item={qna} />
                            );
                        })
                    }
                    </Box>
                </Box>
            </Container>
        </Box>
    );
}