import React from 'react';
import { useNavigate } from 'react-router';
import SubdirectoryArrowRightIcon from '@mui/icons-material/SubdirectoryArrowRight';
import { Box ,Typography ,Button ,ListItem} from '@mui/material';

export default function CommentItem({item , reply}){
    const navigate = useNavigate();
    return(
        <Box border={1} borderColor='rgb(240,240,240)' sx={styles.container}>
            <Box sx={styles.leftBox}>
                <Typography sx={{ fontSize: 13 ,ml: 2 ,mb: 1  ,textAlign: 'start' ,justifyContent: 'start' }}>{item.views} 조회수</Typography>
            </Box>
            <Box sx={styles.rightBox}>
                {/* <Box> */}
                    <Button 
                        sx={styles.titleButton}
                        onClick={() => { navigate(`/qna/${item.id}`,{ state : item }) }}
                    >
                        <div style={{ overflow: 'hidden' ,whiteSpace: 'nowrap' ,textOverflow: 'ellipsis' ,fontSize: 14 }}>
                            {item.title}
                        </div>
                    </Button>
                    <Box sx={styles.replyBox}>
                        <SubdirectoryArrowRightIcon sx={{ ml: 2 }}/>
                        <Typography sx={styles.reply}>
                            <div style={{ overflow: 'hidden' ,whiteSpace: 'nowrap' ,textOverflow: 'ellipsis' ,fontSize: 14 }}>
                                {reply?.substr(0,200)}
                            </div>
                        </Typography>
                    </Box>
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

const styles={
    container: {
        display: 'flex' ,flexDirection: 'row' ,alignItems: 'start' ,flexGrow: 1
    } ,
    leftBox: {
        flex: 1 ,ml: 3 ,justifyContent: 'end' ,alignItems: 'end' ,display: 'flex' ,flexDirection: 'column' , mt: 2.5
    },
    rightBox: { 
        flex: 9 ,ml: 5 ,alignItems: 'start' ,display: 'flex' ,justifyContent: 'start' ,flexDirection: 'column' ,overflow: 'hidden'
    } ,
    titleButton: { 
        textAlignLast: 'start' ,ml: 1,mb: 0.5 ,mt: 1 ,fontSize: 17 ,textTransform: 'none' ,justifyContent: 'start' ,textAlign: 'start', overflow: 'hiddden' ,width: '100%'
    },
    replyBox: { display: 'flex' ,flexDirection: 'row' ,overflow: 'hidden' ,whiteSpace: 'nowrap' ,textOverflow: 'ellipsis' ,width: '100%' },
    reply: {
        ml: 2 ,fontSize: 16 ,textTransform: 'none' ,textAlignLast: 'start' ,overflow: 'hidden' ,whiteSpace: 'nowrap' ,textOverflow: 'ellipsis' ,fontWeight: 'bold' ,width: '100%'
    }
}