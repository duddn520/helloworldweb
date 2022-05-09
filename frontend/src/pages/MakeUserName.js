import React from 'react';
import { Box, Button, Typography, TextField } from "@mui/material";
import styled from "styled-components";
import { useNavigate } from 'react-router';
import api from '../api/api';

const Total = styled.div`
    width: 100%;
    height: 100vh;
    padding-top: 100px;
`;


function MakeUserName(){
    const navigate = useNavigate();

    function saveUserName(){
        const nickName = document.getElementById('nickName').value;
        api.updateNickName(nickName)
        .then(res=>{
            navigate("/", {replace: true});
        })
        .catch(e=>{
            alert("닉네임 등록이 실패했습니다.");
        })
    }

    return (
        <Total>
            <Box sx={{flex: 1, mb: 10, justifyContent: 'center', display: 'flex'}}>
                <Typography sx={{fontWeight: 'bold', fontSize: 30 }}>닉네임</Typography>
            </Box>
            <Box sx={{flex: 1, display: 'flex', justifyContent: 'center', mb: 10}}>
                <TextField 
                    id="nickName"
                    sx={{ color: 'black', width: 350}}
                    size='small'
                    placeholder='사용할 닉네임을 입력해주세요.'
                />
            </Box>
            <Box sx={{flex: 1, justifyContent: 'center', display: 'flex'}}>
                <Button onClick={()=>{saveUserName();}} variant={'outlined'}>저장</Button>
            </Box>
        </Total>
    )
}

export default MakeUserName;