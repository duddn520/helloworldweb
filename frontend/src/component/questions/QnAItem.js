import React from 'react';
import { useNavigate } from 'react-router';
import { Box ,Typography ,Button ,ListItem } from '@mui/material';

export default function QnAItem({item}){
    const navigate = useNavigate();
    return(
        <Box border={1} borderColor='rgb(240,240,240)' sx={{ display: 'flex' ,flexDirection: 'row' ,alignItems: 'start' ,flexGrow: 1 }}>
            <Box sx={{ flex: 1 ,ml: 3 ,justifyContent: 'end' ,alignItems: 'end' ,display: 'flex' ,flexDirection: 'column' , mt: 2.5}}>
                <Typography sx={{ fontSize: 13 ,ml: 2 ,mb: 1  ,textAlign: 'start' ,justifyContent: 'start' }}>{item.views} 조회수</Typography>
                <Typography sx={{ fontSize: 13 ,ml: 2 ,mb: 1 ,fontWeight: 'bold' }}>2 답변</Typography>
            </Box>
            <Box sx={{ flex: 9 ,ml: 5 ,alignItems: 'start' ,display: 'flex' ,justifyContent: 'start' ,flexDirection: 'column'}}>
                {/* <Box> */}
                    <Button 
                        sx={{ textAlignLast: 'start' ,ml: 1,mb: 0.5 ,mt: 1 ,fontSize: 17 ,textTransform: 'none' ,justifyContent: 'start' ,textAlign: 'start'}}
                        onClick={() => { navigate(`/qna/${item.id}`,{ state : item }) }}
                    >
                        {item.title}
                    </Button>
                    <Typography sx={{ ml: 2 ,fontSize: 13 ,textTransform: 'none' ,textAlignLast: 'start' }}>{item.content.substr(0,100)}</Typography>
                        <ListItem sx={{ display: 'flex' ,flexDirection: 'row' , mt: 1}}>
                        {
                            item.tags.split(',').map( tag => { if(tag.length) return <Button sx={{ backgroundColor: 'lightblue' ,p: 0.5 ,mr: 0.2 ,fontSize: 11 ,borderRadius: 1 ,textTransform: 'none' }}>{tag}</Button>})
                        }
                        </ListItem>
                {/* </Box> */}
            </Box>
        </Box>
    );
}