import React from 'react';
import { useNavigate } from 'react-router';
import { Box ,Typography ,Button ,ListItem } from '@mui/material';
import styled from '@emotion/styled';
export default function QnAItem({item}){
    const navigate = useNavigate();
    return(
        <Box border={1} borderColor='rgb(240,240,240)' sx={styles.container}>
            <Box sx={styles.leftBox}>
                <Typography sx={styles.subinfo}>{item.views} 조회수</Typography>
                <Typography sx={{ ...styles.subinfo ,fontWeight: 'bold' }}>2 답변</Typography>
            </Box>
            <Box sx={styles.rightBox}>
                <Button 
                    sx={styles.titleButton}
                    onClick={() => { navigate(`/qna/${item.id}`,{ state : item }) }}
                >
                    <div style={styles.title}>
                        {item.title}
                    </div>
                </Button>
                <Typography sx={styles.content}>{item.content.substr(0,100)}</Typography>
                <ListItem sx={{ display: 'flex' ,flexDirection: 'row' }}>
                {
                    item.tags.split(',').map( tag => { if(tag.length) return <Button size='small' sx={styles.tagButton}>{tag}</Button>})
                }
                </ListItem>
            </Box>
        </Box>
    );
}
const TitleBox = styled.div`
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    font-size: 15px;
    font-weight: bold;
`;

const styles = {
    container: {
        display: 'flex' ,flexDirection: 'row' ,alignItems: 'start' ,height: 120
    },
    leftBox: {
        flex: 1 ,ml: 3 ,justifyContent: 'end' ,alignItems: 'end' ,display: 'flex' ,flexDirection: 'column' , mt: 2.5 
    },
    rightBox: {
        flex: 9 ,alignItems: 'start' ,justifyContent: 'start' ,whiteSpace: 'nowrap' ,textOverflow: 'ellipsis' ,overflow: 'hidden' ,pr: 2
    },
    titleButton: {
        textAlignLast: 'start' ,ml: 1,fontSize: 17 
        ,textTransform: 'none' ,justifyContent: 'start' ,textAlign: 'start',
        width: '100%'
    },
    title: {
        whiteSpace: 'nowrap' ,textOverflow: 'ellipsis' ,overflow: 'hidden' 
    },
    content: {
        ml: 2 ,fontSize: 13 ,textTransform: 'none' ,textAlignLast: 'start' , whiteSpace: 'nowrap' ,textOverflow: 'ellipsis' ,overflow: 'hidden' 
    } ,
    subinfo: {
        fontSize: 13 ,ml: 2  
    } ,
    tagButton: {
        backgroundColor: 'lightblue' , fontSize: 11 ,borderRadius: 1 ,textTransform: 'none' ,mr: 0.5
    }
}