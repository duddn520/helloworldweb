import React from "react";
import { Button ,Typography ,Box , 
         List ,Card ,CardHeader ,CardContent ,ListItem
} 
from "@mui/material";
import { useNavigate } from "react-router-dom";
import { Search as SearchIcon } from "@mui/icons-material";
import { styled } from "@mui/material";
import CustomAppBar from "../component/appbar/CustomAppBar";
import QnAItem from "../component/questions/QnAItem";
import api from "../api/api";

export default function ( props ){
    const navigate = useNavigate();
    // 탭(Tab)
    const [value, setValue] = React.useState(0);
    const [allQna,setAllQna] = React.useState([]);

    React.useEffect(() => {
        api.getAllQna()
        .then( res => { setAllQna(res) })
        .catch( e => { });
        
    },[]);

    return(
        <Box sx={styles.container}> 
            <CustomAppBar />
            <Box sx={styles.InnerBox}>
                <Box sx={styles.leftBox}>
                    <Card sx={{ flex: 0.9, m: 2 }}>
                        <CardContent>
                            <Home />
                        </CardContent>
                    </Card> 
                </Box>
                <Box sx={styles.rightBox}>
                    <Card sx={{ flex: 1,m: 5 }}>
                        <CardContent>
                            <Questions />
                        </CardContent>
                    </Card> 
                </Box>
            </Box>
        </Box>
    );

function Home(){
    return(
        <div>
                    <Box sx={styles.titleBox}>
                        <Typography sx={styles.cardLabel}>질문</Typography>
                        <Button 
                            onClick={() => navigate('/qna/register')}
                            variant='contained' 
                            size='small' 
                            sx={{ mb: 1 ,p: 1 ,ml: 'auto' }}
                        >
                            질문 작성하기
                        </Button>
                    </Box>
                    <List sx={{ flex: 1 }}>
                    {
                        allQna.map( (item,index) => {
                            return(
                                <QnAItem key={index} item={item}/>
                            );
                        })
                    }
                    </List>
        </div>
    );
}
function Questions(){
    return(
        <div>
                    <Box sx={styles.titleBox}>
                        <Typography sx={styles.cardLabel}>많이 본 질문</Typography>
                    </Box>
                    <List>
                    {
                        allQna.sort((a,b) => b.views - a.views ).slice(0,5).map( (item,index) => {
                            return(
                                <Box sx={styles.mostViewedBox}>
                                    <div> {index+1} </div>
                                    <Button sx={styles.mostViewedButton} onClick={() => { navigate(`/qna/${item.id}`,{ state : item }) }}>  
                                        <div style={{ whiteSpace: 'nowrap' ,textOverflow: 'ellipsis' ,overflow: 'hidden' }}>
                                            {item.title}
                                        </div>
                                    </Button>
                                </Box>
                            );
                        })
                    }
                    </List>
        </div>
    );
}

}

// TextField의 부모 Compnent로 감싸줘야 onSumbit() 작동
const StyledForm = styled('form')({  

});

const styles={
    container: {
        width: '100%'
    },
    InnerBox: {
        display: 'flex' ,flexDirection: 'row' ,width: '100%' 
    },
    leftBox: {
        width: '66%' ,alignItems: 'center' ,display: 'flex' ,alignItems: 'center' ,justifyContent: 'center' 
    },
    rightBox: {
        width: '33%'
    },
    titleBox: {
        display: 'flex' ,flexDirection: 'row'
    },
    cardLabel: {
        fontSize: 18,flex: 1 ,fontWeight: 'bold'
    },
    mostViewedBox : {
        flex: 1,alignItems: 'start' ,justifyContent: 'start' ,
        whiteSpace: 'nowrap' ,textOverflow: 'ellipsis' ,overflow: 'hidden'
        ,display: 'flex' ,flexDirection: 'row' ,alignItems: 'center'
    } ,
    mostViewedButton : {
        textAlignLast: 'start' ,ml: 2 ,fontSize: 17 
        ,textTransform: 'none' ,justifyContent: 'start' ,textAlign: 'start' ,width: '100%' ,p: 1
    }
}
