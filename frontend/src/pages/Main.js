import React from "react";
import { Button ,Typography ,Box , 
         List ,Tabs ,Tab
} 
from "@mui/material";
import { useNavigate } from "react-router-dom";
import { Search as SearchIcon } from "@mui/icons-material";
import { styled } from "@mui/material";
import CustomAppBar from "../component/appbar/CustomAppBar";
import QnAItem from "../component/questions/QnAItem";
import api from "../api/api";

function TabPanel(props) {
    const { children, value, index, ...other } = props;

    return (
        value === index && (
          <Box sx={{ p: 3 ,width: '100%'}}>
            {children}
          </Box>
        )
    );
}  

export default function ( props ){
    const navigate = useNavigate();
    // 탭(Tab)
    const [value, setValue] = React.useState(0);
    const [allQna,setAllQna] = React.useState([]);

    const handleChange = (event, newValue) => {
        setValue(newValue);
    };

    React.useEffect(() => {
        api.getAllQna()
        .then( res => { setAllQna(res) })
        .catch( e => { });
        
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
                    <Tab label="홈" sx={{ display: 'flex' ,flexDirection: 'row'}} />
                    <Tab label="Questions" icon={<SearchIcon sx={{ m: 1 ,width: 20 ,height: 20}}/>} sx={{ display: 'flex' ,flexDirection: 'row' ,textAlign: 'center' ,textTransform: 'none' }}  />
                </Tabs>

                {/* 탭 : 홈 */}
                <TabPanel value={value} index={0}>
                    <Home />
                </TabPanel>

                <TabPanel value={value} index={1}>
                    <Questions />
                </TabPanel> 

            </Box>       
        </Box>
    );

function Home(){
    return(
        <div>
                    <Box sx={{ display: 'flex' ,flexDirection: 'row' }}>
                        <Typography sx={{ fontSize: 24 ,flex: 1 }}>Top Questions</Typography>
                        <Button 
                            onClick={() => navigate('/qna/register')}
                            variant='contained' 
                            size='small' 
                            sx={{ mb: 1 ,p: 1}}
                        >
                            질문 작성하기
                        </Button>
                    </Box>
                    <List>
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
                    <Box sx={{ display: 'flex' ,flexDirection: 'row' }}>
                        <Typography sx={{ fontSize: 24 ,flex: 1 }}>All Questions</Typography>
                        <Button 
                            onClick={() => navigate('/qna/register')}
                            variant='contained' 
                            size='small' 
                            sx={{ mb: 1 ,p: 1}}
                        >
                            질문 작성하기
                        </Button>
                    </Box>
                    <List>
                    {
                        allQna.map( item => {
                            return(
                                <QnAItem  key={item} item={item}/>
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


