import React from 'react';
import { AppBar, Toolbar, Typography ,TextField ,IconButton ,Avatar ,
    MenuList , MenuItem , ListItemText ,Divider , ListItemButton , Paper ,
    Button
} from '@mui/material';
import { Menu as MenuIcon ,Search as SearchIcon } from '@mui/icons-material';
import { styled } from '@mui/material';
import { useNavigate } from "react-router-dom";


// 로그아웃 함수
function logout(){
    window.sessionStorage.removeItem("Auth");
}

export default function(){
    // 로그인 상태
    const [loginState,setLoginState] = React.useState(false);
    // 로그인 시 아바타 클릭 시 메뉴 활성화 
    const [loginMenuVisible,setLoginMenuVisible] = React.useState(false);
    const navigate = useNavigate();

    // 화면 로딩 시 호출
    React.useEffect(()=>{
        // 로그인 상태 확인
        if( window.sessionStorage.getItem("Auth") != null ){
            setLoginState(true);
        }
    },[]);

    return(
        <AppBar position='static' sx={{ backgroundColor: 'white' ,boxShadow: 0}}>
                <Toolbar>
                    <Typography 
                        variant='h6' 
                        sx={{ color: 'black' , textAlign: 'center' , p: 1 , mr: 1 }}
                    >
                        {"<Hello World"}
                        <Typography display='inline' variant="h5" sx={{ color: 'red' }}>
                            {"/>"}
                        </Typography>
                    </Typography>
                    
                    <StyledForm>
                        <TextField
                            size='small'
                            sx={{ width: '90%' ,ml: 2 }}
                            placeholder="Search Anything"
                            InputProps={{ startAdornment: <SearchIcon/> }}
                        />
                    </StyledForm>

                    {
                        loginState ? 
                        <Paper sx={{ alignItems: 'center', boxShadow: 0 }}>
                            <ListItemButton 
                                sx={{ alignItems: 'center' }}
                                onClick={() => { setLoginMenuVisible(!loginMenuVisible) }}
                            >
                                <Avatar>
                                    유저
                                </Avatar>
                            </ListItemButton>
                            {
                                loginMenuVisible &&
                                <MenuList sx={{ borderWidth: 1 , boxShadow: 2 , position: 'absolute' }}>
                                    <MenuItem>
                                        <ListItemText>Cut</ListItemText>
                                    </MenuItem>

                                    <MenuItem>
                                        <ListItemText>Copy</ListItemText>
                                    </MenuItem>

                                    <MenuItem>
                                        <ListItemText>Paste</ListItemText>
                                    </MenuItem>

                                    <Divider />
                                    <MenuItem onClick={logout}>
                                        <ListItemText>로그아웃</ListItemText>
                                    </MenuItem>
                                </MenuList>
                            }
                        </Paper>
                        :
                        <Button 
                            onClick={ () => navigate("/login") } 
                            color="inherit" 
                            sx={{ color: 'black' , fontSize: 17 }}
                            >
                                로그인
                        </Button>
                    }
                    <IconButton>
                        <MenuIcon color='black' />
                    </IconButton>
                </Toolbar>
            </AppBar>
    );
}

const StyledForm = styled('form')({
    flexGrow: 1 ,
});