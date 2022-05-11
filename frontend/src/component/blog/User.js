import React from 'react';
import { Box ,Avatar ,Container ,Typography ,Button ,Badge } from '@mui/material';
import { useParams ,useNavigate} from 'react-router';
import api from '../../api/api';
import QnAItem from '../questions/QnAItem';
import CustomAppBar from '../appbar/CustomAppBar';
export default function({id , qnas ,comments }){
    return(
        <Box>
            <Container border={1} sx={{ display: 'flex' ,flexDirection: 'row' }}>
                <Box sx={{ flex: 1 ,m: 2}}>
                    <Typography sx={{ fontSize: 25 }}>작성한 질문</Typography>
                    <Box sx={{ flex: 1 ,mt: 2 }}>
                    {
                        qnas.length ?
                        qnas.map( qna => {
                            return(
                                <QnAItem item={qna} />
                            );
                        })
                        :
                        <Box sx={{ flex: 1 ,alignItems: 'center' ,justifyContent: 'center' ,display: 'flex' }}>
                            <Typography sx={{ fontSize: 15 ,color: 'gray' ,marginTop: 10 }}>아직 작성한 답변이 없어요.</Typography>
                        </Box>
                    }
                    </Box>
                </Box>

                <Box sx={{ flex: 1 ,m: 2 }}>
                    <Typography sx={{ fontSize: 25 }}>작성한 답변</Typography>
                    <Box sx={{ flex: 1 ,mt: 2 }}>
                    {
                        comments.length ?
                        comments.map( comment => {
                            return(
                                <QnAItem item={comment} />
                            );
                        })
                        :
                        <Box sx={{ flex: 1 ,alignItems: 'center' ,justifyContent: 'center' ,display: 'flex' }}>
                            <Typography sx={{ fontSize: 15 ,color: 'gray' ,marginTop: 10 }}>아직 작성한 답변이 없어요.</Typography>
                        </Box>
                    }
                    </Box>
                </Box>
            </Container>
        </Box>
    );
}