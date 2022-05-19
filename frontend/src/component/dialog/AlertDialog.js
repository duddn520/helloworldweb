import React from 'react';
import { Dialog ,DialogTitle ,DialogContent ,DialogContentText ,DialogActions ,Button} from '@mui/material';

export default function AlertDialog({open,setOpen,onAgree}){

    const handleClose = () => {
        setOpen(false);
    };

    return(
        <Dialog
            open={open}
            onClose={handleClose}
        >
            {/* <DialogTitle id="alert-dialog-title">
            {"Use Google's location service?"}
            </DialogTitle> */}
            <DialogContent>
            <DialogContentText id="alert-dialog-description">
                정말로 삭제하시겠습니까?
            </DialogContentText>
            </DialogContent>
            <DialogActions>
            <Button onClick={handleClose} >취소</Button>
            <Button onClick={onAgree} autoFocus sx={{ color: 'red' }}>
                확인
            </Button>
            </DialogActions>
        </Dialog>
    );
}