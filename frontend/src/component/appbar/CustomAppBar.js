import React from 'react';
import { AppBar, Toolbar, Typography ,TextField ,IconButton ,Avatar ,
    MenuList , MenuItem , ListItemText ,Divider , ListItemButton , Paper ,
    Button ,Popover ,Drawer ,Box ,List ,ListItem ,ListItemIcon 
} from '@mui/material';
import { Menu as MenuIcon ,Search as SearchIcon,Home as HomeIcon } from '@mui/icons-material';
import { styled } from '@mui/material';
import { useNavigate } from "react-router-dom";
import api from '../../api/api';

export default function(){
    const navigate = useNavigate();
    // 로그인 상태
    const [loginState,setLoginState] = React.useState(false);
    // 로그인 시 아바타 클릭 시 메뉴 활성화 
    const [loginMenuVisible,setLoginMenuVisible] = React.useState(false);
    // 햄버거 메뉴 활성화
    const [state,setState] = React.useState([{ right: false }]);
    // 프로필사진
    const [profileUrl,setProfileUrl] = React.useState("");

    const toggleDrawer = (anchor, open) => (event) => {
        if (event.type === 'keydown' && (event.key === 'Tab' || event.key === 'Shift')) {
          return;
        }    
        setState({ ...state, [anchor]: open });
    };        

    // 로그아웃 함수
    function logout(){
        window.sessionStorage.removeItem("Auth");
        setLoginState(false);
    }

    // 화면 로딩 시 호출
    React.useEffect(()=>{
        // 로그인 상태 확인
        if( window.sessionStorage.getItem("Auth") != null ){
            setLoginState(true);
            if( window.sessionStorage.getItem("profileUrl") == null ){
                api.getUser()
                .then( res=> {
                    setProfileUrl(res.profileUrl);
                })
                .catch();
            } else {
                setProfileUrl( window.sessionStorage.getItem("profileUrl") )
            }
        }
    },[]);

    return(
        <AppBar position='static' sx={{ backgroundColor: 'white' ,boxShadow: 0 }} >
                <Toolbar>
                    <IconButton disableRipple onClick={() => navigate('/')}>
                        <Typography 
                            variant='h6' 
                            sx={{ color: 'black' , textAlign: 'center' , p: 1 , mr: 1 }}
                        >
                            {"<Hello World"}
                            <Typography display='inline' variant="h5" sx={{ color: 'red' }}>
                                {"/>"}
                            </Typography>
                        </Typography>
                    </IconButton>
                    
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
                        <Paper sx={{ alignItems: 'center', boxShadow: 0 , display: 'flex' ,flexDirection: 'row' }}>
                            <ListItemButton 
                                sx={{ alignItems: 'center' }}
                                onClick={() => { setLoginMenuVisible(!loginMenuVisible) }}
                            >
                                <Avatar src={profileUrl}>
                                    유저
                                </Avatar>
                            </ListItemButton>
                            
                            <Popover
                                open={loginMenuVisible}
                                onClose={() => setLoginMenuVisible(false)}
                                sx={{ borderWidth: 1 , top: 50 }}
                                anchorOrigin={{
                                    horizontal: 'right',
                                }}
                            >
                                <MenuList>
                                    <MenuItem onClick={() => navigate('/qna/register')}>
                                        <ListItemText>질문 작성하기</ListItemText>
                                    </MenuItem>

                                    <Divider />
                                    <MenuItem onClick={logout}>
                                        <ListItemText sx={{ color: 'red' }}>로그아웃</ListItemText>
                                    </MenuItem>
                                </MenuList>
                            </Popover>

                            <IconButton onClick={toggleDrawer('right',true)}>
                                <MenuIcon color='black' />
                            </IconButton>

                            {/* 햄버거 메뉴 */}
                            <Drawer
                                anchor={'right'}
                                open={state['right']}
                                onClose={toggleDrawer('right', false)}
                                >
                                <Box
                                    sx={{ width: 250 }}
                                    role="presentation"
                                    onClick={toggleDrawer('right', false)}
                                    onKeyDown={toggleDrawer('right', false)}
                                >
                                    <List>
                                        <Button 
                                            onClick={() => { navigate('/minihome') }} 
                                            sx={{ width: '100%' ,color: 'black' }}
                                        >
                                            <ListItem>
                                                <ListItemIcon>
                                                    <HomeIcon />
                                                </ListItemIcon>
                                                <ListItemText primary={'내 블로그'} />
                                            </ListItem>
                                        </Button>
                                    </List>
                                </Box>
                            </Drawer>
                            

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
                </Toolbar>
            </AppBar>
    );
}

export function MiniHomeBar(){
    const navigate = useNavigate();
    // 로그인 상태
    const [loginState,setLoginState] = React.useState(false);
    // 로그인 시 아바타 클릭 시 메뉴 활성화 
    const [loginMenuVisible,setLoginMenuVisible] = React.useState(false);
    const [state,setState] = React.useState(false);
    // 프로필사진
    const [profileUrl,setProfileUrl] = React.useState("");

    const toggleDrawer = (anchor, open) => (event) => {
        if (event.type === 'keydown' && (event.key === 'Tab' || event.key === 'Shift')) {
          return;
        }    
        setState({ ...state, [anchor]: open });
    };   

    // 로그아웃 함수
    function logout(){
        window.sessionStorage.removeItem("Auth");
        setLoginState(false);
    }

    // 화면 로딩 시 호출
    React.useEffect(()=>{
        // 로그인 상태 확인
        if( window.sessionStorage.getItem("Auth") != null ){
        setLoginState(true);
        if( window.sessionStorage.getItem("profileUrl") == null ){
            api.getUser()
            .then( res=> {
                setProfileUrl(res.profileUrl);
            })
            .catch();
        } else {
            setProfileUrl( window.sessionStorage.getItem("profileUrl") )
        }
    }
    },[]);

    return(
        <Box sx={{ display: 'flex' ,flexDirection: 'row' ,flex: 1}}>
        <IconButton disableRipple onClick={() => navigate('/')}>
            <Typography 
                variant='h6' 
                sx={{ color: 'black' , textAlign: 'center' , p: 1 , mr: 1 }}
            >
                {"<Hello World"}
                <Typography display='inline' variant="h5" sx={{ color: 'red' }}>
                    {"/>"}
                </Typography>
            </Typography>
        </IconButton>

        {
            loginState ? 
            <Paper sx={{ alignItems: 'center', boxShadow: 0 , display: 'flex' ,flexDirection: 'row' ,marginLeft: 'auto' }}>
                <ListItemButton 
                    sx={{ alignItems: 'center' }}
                    onClick={() => { setLoginMenuVisible(!loginMenuVisible) }}
                >
                    <Avatar src={profileUrl}>
                        유저
                    </Avatar>
                </ListItemButton>
                
                <Popover
                    open={loginMenuVisible}
                    onClose={() => setLoginMenuVisible(false)}
                    sx={{ borderWidth: 1 , top: 50 }}
                    anchorOrigin={{
                        horizontal: 'right',
                    }}
                >
                    <MenuList>
                        <MenuItem onClick={() => navigate('/qna/register')}>
                            <ListItemText>질문 작성하기</ListItemText>
                        </MenuItem>

                        <Divider />
                        <MenuItem onClick={logout}>
                            <ListItemText sx={{ color: 'red' }}>로그아웃</ListItemText>
                        </MenuItem>
                    </MenuList>
                </Popover>

                <IconButton onClick={toggleDrawer('right',true)}>
                    <MenuIcon color='black' />
                </IconButton>

                {/* 햄버거 메뉴 */}
                <Drawer
                    anchor={'right'}
                    open={state['right']}
                    onClose={toggleDrawer('right', false)}
                    >
                    <Box
                        sx={{ width: 250 }}
                        role="presentation"
                        onClick={toggleDrawer('right', false)}
                        onKeyDown={toggleDrawer('right', false)}
                    >
                        <List>
                            <Button 
                                onClick={() => { navigate('/minihome') }} 
                                sx={{ width: '100%' ,color: 'black' }}
                            >
                                <ListItem>
                                    <ListItemIcon>
                                        <HomeIcon />
                                    </ListItemIcon>
                                    <ListItemText primary={'내 블로그'} />
                                </ListItem>
                            </Button>
                        </List>
                    </Box>
                </Drawer>
                

            </Paper>
            :
            <Button 
                onClick={ () => navigate("/login") } 
                color="inherit" 
                sx={{ color: 'black' , fontSize: 17 ,marginLeft: 'auto' }}
                >
                    로그인
            </Button>
        }
        </Box>
    );
}

const StyledForm = styled('form')({
    flexGrow: 1 ,
});