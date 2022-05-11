import React from 'react';
import { Box, Button, Typography, TextField } from "@mui/material";
import styled from "styled-components";
import { useNavigate } from 'react-router';
import api from '../api/api';

const Total = styled.div`
    width: 100%;
    height: 100vh;
    padding-top: 100px;
    display: flex;
    align-items: center;
    flex-direction: column;
`;


function MakeUserName(){
    const navigate = useNavigate();

    function saveUserName(){
        const nickName = document.getElementById('nickName').value;
        if(nickName === null || nickName === ""){
            alert("닉네임을 입력해주세요.");
        }
        else{
            api.updateNickName(nickName)
            .then(res=>{
                navigate("/", {replace: true});
            })
            .catch(e=>{
                alert("닉네임 등록이 실패했습니다.");
            });
        }
       
    }

    return (
        <Total>
            <Typography sx={{fontWeight: 'bold', fontSize: 30, mb: 10 }}>닉네임</Typography>
            <TextField 
                id="nickName"
                sx={{ color: 'black', width: 350, mb: 10}}
                size='small'
                placeholder='사용할 닉네임을 입력해주세요.'
            />
            <Button onClick={()=>{saveUserName();}} variant={'outlined'}>저장</Button>
        </Total>
    )
}

export default MakeUserName;