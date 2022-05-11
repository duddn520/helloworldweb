import React from 'react';
import { useNavigate } from 'react-router';
import { Box ,Typography ,Button ,ListItem ,Badge} from '@mui/material';

export default function QnAItem({item}){
    const navigate = useNavigate();
    return(
        <Box border={1} borderColor='rgb(240,240,240)' sx={{ display: 'flex' ,flexDirection: 'row' ,alignItems: 'center' ,height: 150 }}>
            <Box sx={{ flex: 1 ,ml: 3}}>
                <Typography sx={{ fontSize: 13 ,ml: 2 ,mb: 1  ,mt: 1 }}>{item.views} 조회수</Typography>
                <Typography sx={{ fontSize: 13 ,ml: 2 ,mb: 1 ,fontWeight: 'bold' }}>2 답변</Typography>
            </Box>
            <Box sx={{ flex: 5 ,alignItems: 'start' ,display: 'flex' ,justifyContent: 'start' }}>
                <Box>
                    <Button 
                        sx={{ textAlignLast: 'start' ,ml: 1,mb: 0.5 ,textTransform: 'none' ,justifyContent: 'start' ,textAlign: 'start'}}
                        onClick={() => { navigate(`/qna/${item.id}`,{ state : item }) }}
                    >
                        {item.title}
                    </Button>
                    <Typography sx={{ ml: 2 ,fontSize: 13 ,textTransform: 'none' ,textAlignLast: 'start' }}>{item.content.substr(0,100)}</Typography>
                        <ListItem sx={{ display: 'flex' ,flexDirection: 'row' , mt: 5}}>
                        {
                            item.tags.split(',').map( tag => { if(tag.length) return <Badge sx={{ backgroundColor: 'rgb(240,240,240)' ,p: 0.5 ,mr: 0.2 ,fontSize: 13}}>{tag}</Badge>})
                        }
                        </ListItem>
                </Box>
            </Box>
        </Box>
    );
}