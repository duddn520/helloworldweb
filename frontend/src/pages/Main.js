import React from "react";
import {Button , AppBar , Toolbar , Typography , Box , TextField , Container } from "@mui/material";
import {useNavigate } from "react-router-dom";
import { Search as SearchIcon } from "@mui/icons-material";
import { styled } from "@mui/material";
import api from "../api/api";

export default function ( props ){
    const [text,setText] = React.useState("");
    const navigate = useNavigate();


    // 검색창 입력완료 후 자동호출
    function handleSubmit(){
        alert(text);
    }

    return(
        <Box sx={{ flexGrow: 1 }}>
            <AppBar 
                position="static" 
                variant="outlined" 
                sx={{ backgroundColor: 'white' }}
            >
                <Toolbar sx={{ position: 'absolute' ,right: 5 }} >
                    <Button 
                        onClick={api.getGuestBooks} 
                        color="inherit" 
                        sx={{ color: 'black' , fontSize: 17 }}
                        >
                            임시
                    </Button>
                    <Button 
                        onClick={ () => navigate("/login") } 
                        color="inherit" 
                        sx={{ color: 'black' , fontSize: 17 }}
                        >
                            로그인
                    </Button>
                </Toolbar>
            </AppBar>


            <Box sx={{ margin: 10 , textAlign: 'center'}}>

                <Typography 
                    variant='h2' 
                    sx={{ color: 'black' , textAlign: 'center' , p: 5 }}
                >
                    {"<Hello World"}
                    <Typography display='inline' variant="h2" sx={{ color: 'red' }}>
                        {"/>"}
                    </Typography>
                </Typography>

                <StyledForm 
                    onSubmit={handleSubmit}
                    >
                    <TextField 
                        sx={{ width: '50%' , p: 1 }}
                        InputProps={{ startAdornment: <SearchIcon sx={{ p: 1 }} />}} 
                        onChange={(t) => setText(t.target.value)}
                        size='medium' 
                        placeholder="Search Anything"
                    />
                </StyledForm>

            </Box>

            
        </Box>
    )   ;
}

// TextField의 부모 Compnent로 감싸줘야 onSumbit() 작동
const StyledForm = styled('form')({  
    alignItems: 'center',  
});