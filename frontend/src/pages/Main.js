import React from "react";
import { Button , AppBar , Toolbar , Typography , Box , 
        TextField , Avatar, InputBase , IconButton , Container ,
        List ,ListItem ,ListItemIcon ,Badge ,Tabs ,Tab 
} 
from "@mui/material";
import { useNavigate } from "react-router-dom";
import { Search as SearchIcon } from "@mui/icons-material";
import { styled } from "@mui/material";
import CustomAppBar from "../component/appbar/CustomAppBar";
import api from "../api/api";

const data = [
    {
        title: 'why could not found the symbol even define the fields name',
        content: '내용',
        tags: ['java'],
        views: 120,
        answers: 2
    } ,
    {
        title: 'sending client auth code to express server to allow server to modify clients  erver to modify clienerver to modify clien ienerver to modify clieienerver to modify clie ',
        content: '내용',
        tags: ['javascript','node.js','express'],
        views: 230,
        answers: 0
    } ,
    {
        title: 'React Native - Review data automatically in real time from AsyncStorage',
        content: '내용',
        tags: ['react native'],
        views: 10,
        answers: 1
    } ,
    {
        title: 'why could not found the symbol even define the fields name',
        content: '내용',
        tags: ['java'],
        views: 120,
        answers: 2
    } ,
    {
        title: 'sending client auth code to express server to allow server to modify client spreadsheets',
        content: '내용',
        tags: ['javascript','node.js','express'],
        views: 230,
        answers: 0
    } ,
    {
        title: 'React Native - Review data automatically in real time from AsyncStorage',
        content: '내용',
        tags: ['react native'],
        views: 10,
        answers: 1
    } ,
    {
        title: 'why could not found the symbol even define the fields name',
        content: '내용',
        tags: ['java'],
        views: 120,
        answers: 2
    } ,
    {
        title: 'sending client auth code to express server to allow server to modify client spreadsheets',
        content: '내용',
        tags: ['javascript','node.js','express'],
        views: 230,
        answers: 0
    } ,
    {
        title: 'React Native - Review data automatically in real time from AsyncStorage',
        content: '내용',
        tags: ['react native'],
        views: 10,
        answers: 1
    } ,
]

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
    // 검색창
    const [text,setText] = React.useState("");
    // 탭(Tab)
    const [value, setValue] = React.useState(0);
    const [allQna,setAllQna] = React.useState([]);

    const handleChange = (event, newValue) => {

        if( newValue == 1 ){
            api.getAllQna()
            .then( res => { setAllQna(res) })
            .catch( e => { });
        }

        setValue(newValue);
    };

    // 검색창 입력완료 후 자동호출
    function handleSubmit(){
        alert(text);
    }

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
                    <Tab label="QnA" icon={<SearchIcon sx={{ m: 1 ,width: 20 ,height: 20}}/>} sx={{ display: 'flex' ,flexDirection: 'row' ,textAlign: 'center'}}  />
                </Tabs>

                {/* 탭 : 홈 */}
                <TabPanel value={value} index={0}>
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
                        data.map( item => {
                            return(
                                <Box border={1} borderColor='rgb(240,240,240)' sx={{ display: 'flex' ,flexDirection: 'row' ,alignItems: 'center' }}>
                                    <Box sx={{ flex: 1 }}>
                                        <Typography sx={{ fontSize: 13 ,ml: 2 ,mb: 1 }}>{item.views} 조회수</Typography>
                                        <Typography sx={{ fontSize: 13 ,ml: 2 ,mb: 1 }}>{item.answers} 답변</Typography>
                                    </Box>
                                    <Box sx={{ flex: 10 }}>
                                        <Button sx={{ textAlignLast: 'start' ,m: 2 ,mb: 1 ,textAlign: 'start' }}>{item.title}</Button>
                                        <ListItem sx={{ display: 'flex' ,flexDirection: 'row'}}>
                                            {
                                                item.tags.map( tag => {return <Badge sx={{ backgroundColor: 'rgb(240,240,240)' ,p: 0.5 ,mr: 0.2 ,fontSize: 13}}>{tag}</Badge>})
                                            }
                                        </ListItem>
                                    </Box>
                                </Box>
                            );
                        })
                    }
                    </List>
                </TabPanel>

                <TabPanel value={value} index={1}>
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
                                <Box border={1} borderColor='rgb(240,240,240)' sx={{ display: 'flex' ,flexDirection: 'row' ,alignItems: 'center' }}>
                                    <Box sx={{ flex: 1 }}>
                                        <Typography sx={{ fontSize: 13 ,ml: 2 ,mb: 1 }}>100 조회수</Typography>
                                        <Typography sx={{ fontSize: 13 ,ml: 2 ,mb: 1 }}>2 답변</Typography>
                                    </Box>
                                    <Box sx={{ flex: 10 }}>
                                        <Button sx={{ textAlignLast: 'start' ,m: 2 ,mb: 1 ,textAlign: 'start' }}>제목없음</Button>
                                        <Typography sx={{ ml: 3 }}>{item.content.substr(0,100)}</Typography>
                                        {/* <ListItem sx={{ display: 'flex' ,flexDirection: 'row'}}>
                                            {
                                                item.tags.map( tag => {return <Badge sx={{ backgroundColor: 'rgb(240,240,240)' ,p: 0.5 ,mr: 0.2 ,fontSize: 13}}>{tag}</Badge>})
                                            }
                                        </ListItem> */}
                                    </Box>
                                </Box>
                            );
                        })
                    }
                    </List>
                </TabPanel>
            </Box>

            {/* <Box sx={{ margin: 10 , textAlign: 'center'}}>

                <Typography 
                    variant='h2' 
                    sx={{ color: 'black' , textAlign: 'center' , p: 5 }}
                >
                    {"<Hello World"}
                    <Typography display='inline' variant="h2" sx={{ color: 'red' }}>
                        {"/>"}
                    </Typography>
                </Typography>

                <Container sx={{ width: '50%'}}>
                <StyledForm 
                    onSubmit={handleSubmit}
                    sx={{ boxShadow: 3, borderRadius: 20 , justifyContent: 'center' }}
                >
                    <IconButton>
                    <SearchIcon sx={{ p : 1 }} />
                    </IconButton>
                    <InputBase
                        sx={{ width: '90%'  }}
                        onChange={(t) => setText(t.target.value)}
                        value={text}
                        placeholder="Search Anything"
                    />
                </StyledForm>
                </Container>

            </Box> */}

            
    </Box>
    )   ;
}

// TextField의 부모 Compnent로 감싸줘야 onSumbit() 작동
const StyledForm = styled('form')({  

});