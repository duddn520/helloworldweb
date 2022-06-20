import React from 'react';
import { Backdrop, CircularProgress } from "@mui/material";

function LoadingSpinner({open, onClick}){
    return (
        <Backdrop
            sx={{ color: '#fff', zIndex: (theme) => theme.zIndex.drawer + 1 }}
            open={open}
            onClick={onClick}
            >
            <CircularProgress color="inherit" />
        </Backdrop>
    )
}

export default LoadingSpinner;