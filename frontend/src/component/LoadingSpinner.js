import { ThreeDots } from  'react-loader-spinner';
import React from 'react';
import { Box } from "@mui/material";
import styled from "@emotion/styled";

function LoadingSpinner(){
    return (
        <Box sx={{ top: 0, height: '100vh', width: '100%', bgcolor: 'rgba(0,0,0,0.3)', display: 'flex', alignItems: 'center', justifyContent: 'center' }} position={'absolute'}>
            <ThreeDots color="#3874CB"/>
        </Box>
    )
}

export default LoadingSpinner;