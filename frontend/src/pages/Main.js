import React from "react";
import { Button , AppBar , Toolbar , Typography , Box , 
        TextField , Avatar, InputBase , IconButton , Container ,
} 
from "@mui/material";
import { useNavigate } from "react-router-dom";
import { Search as SearchIcon } from "@mui/icons-material";
import { styled } from "@mui/material";
import CustomAppBar from "../component/appbar/CustomAppBar";



export default function ( props ){
    // 검색창
    const [text,setText] = React.useState("");
    
    const navigate = useNavigate();


    // 검색창 입력완료 후 자동호출
    function handleSubmit(){
        alert(text);
    }

    return(
        <Box sx={{ flexGrow: 1 }}>
            
            {/* <CustomAppBar /> */}

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

            </Box>

            
        </Box>
    )   ;
}

// TextField의 부모 Compnent로 감싸줘야 onSumbit() 작동
const StyledForm = styled('form')({  

});