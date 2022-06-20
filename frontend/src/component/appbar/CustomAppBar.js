import React from 'react';
import { AppBar, Toolbar, Typography ,TextField ,IconButton ,Avatar ,
    MenuList , MenuItem , ListItemText ,Divider , ListItemButton , Paper ,
    Button ,Popover ,Drawer ,Box ,List ,ListItem ,ListItemIcon ,Popper
} from '@mui/material';
import { Menu as MenuIcon ,Search as SearchIcon,Home as HomeIcon ,Edit as EditIcon } from '@mui/icons-material';
import { styled } from '@mui/material';
import { useNavigate } from "react-router-dom";
import api from '../../api/api';

export default function(){
    // 화면이동
    const navigate = useNavigate();
    // 로그인 상태
    const [loginState,setLoginState] = React.useState(false);
    // 로그인 시 아바타 클릭 시 메뉴 활성화 
    const [loginMenuVisible,setLoginMenuVisible] = React.useState(false);
    // 프로필사진
    const [profileUrl,setProfileUrl] = React.useState("");
    // 사용자 이메일
    const [userEmail, setUserEmail] = React.useState(null);
    // 사용자 id
    const [userName,setUserName] = React.useState("anonymous");
    
    // 검색어 추천 
    const [anchorEl, setAnchorEl] = React.useState(null);
    const [sentence,setSentence] = React.useState("");
    const searchBarRef = React.useRef(null);
    const openPopper = Boolean(anchorEl);
    const handlePopper = (event) => {
        if( document.activeElement === searchBarRef.current && anchorEl ) {
            return;
        }
        setAnchorEl(anchorEl ? null : event.currentTarget);
    };
    // 검색내용
    const handleSentence = (value) => {
        setSentence(value.target.value);
    }
    // 검색 - "Enter" 눌렀을 때  
    const handleSubmit = (event) => {

        api.getSearchedPost({sentence : sentence, page: state.page })
            .then( res => {
                
                navigate('/search',{ state: { res: res , sentence: sentence ,page: state.page } })
            })
        .catch()
    }
    
    // 햄버거 메뉴 활성화
    const [state,setState] = React.useState([{ right: false }]);
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
                    setUserEmail(res.email);
                    setUserName(res.userName);
                })
                .catch();
            } else {
                setProfileUrl( window.sessionStorage.getItem("profileUrl") )
            }
        }
    },[]);

    function MoveToMyBlog(){
        if(userEmail !== null){
            navigate(`/minihome/`, {state: {tabIndex: 0, targetEmail: userEmail}});
        }
        else{
            alert("로그인이 필요합니다.");
        } 
    }

    return(
        <AppBar position='static' sx={{ backgroundColor: 'white' ,boxShadow: 0 ,borderBottomWidth: 1 }} >
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
                    
                    <StyledForm
                        onClick={handlePopper}
                        onBlur={handlePopper}
                        onSubmit={handleSubmit}
                        >
                        <TextField
                            inputRef={searchBarRef}
                            value={sentence}
                            onChange={handleSentence}
                            size='small'
                            sx={{ width: '90%' ,ml: 2 }}
                            placeholder="Search Anything"
                            InputProps={{ startAdornment: <SearchIcon/> }}
                            autoComplete="off"
                        />
                        <Popper pos id="simple-popper" open={openPopper} anchorEl={anchorEl} placement="bottom-start">
                        <Box sx={{ border: 2 ,borderColor: 'lightgray' ,width: document?.activeElement === null ? 0 : document.activeElement.clientWidth ,m: 2 ,p: 2 ,bgcolor: 'background.paper' ,borderRadius: 2 }}>
                           <Box sx={{ display: 'flex',flexDirection: 'row' ,flexGrow: 1}}>
                               <Box sx={{ flex: 1 ,display: 'flex',flexDirection: 'row' ,alignItems: 'center' }}>
                                    <Typography sx={{ fontSize: 12 ,fontWeight: 'bold' }}>[태그]</Typography>
                                    <Typography sx={{ color: 'gray' ,fontSize: 12 ,ml: 1}}>해당태그 포함</Typography>
                               </Box>
                               <Box sx={{ flex: 1 ,display: 'flex',flexDirection: 'row' }}>
                                    <Typography sx={{ fontSize: 12 ,fontWeight: 'bold'}}>"정확한 검색"</Typography>
                                    <Typography sx={{ color: 'gray' ,fontSize: 12 ,ml: 1}}>정확한 문구만 포함</Typography>
                               </Box>
                           </Box>
                        </Box>
                        </Popper>
                    </StyledForm>
                    {
                        loginState ? 
                        <Paper sx={{ alignItems: 'center', boxShadow: 0 , display: 'flex' ,flexDirection: 'row' }}>
                            <ListItemButton 
                                sx={{ alignItems: 'center' }}
                                onClick={MoveToMyBlog}
                            >
                                {
                                    profileUrl &&
                                    <Avatar src={profileUrl}>
                                        
                                    </Avatar>
                                }
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
                                            onClick={() => {MoveToMyBlog()}} 
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


// 미니홈용 ( 검색창 없음 )
export function MiniHomeBar(){
    const navigate = useNavigate();
    // 로그인 상태
    const [loginState,setLoginState] = React.useState(false);
    // 로그인 시 아바타 클릭 시 메뉴 활성화 
    const [loginMenuVisible,setLoginMenuVisible] = React.useState(false);
    const [state,setState] = React.useState(false);
    // 프로필사진
    const [profileUrl,setProfileUrl] = React.useState("");
    // 사용자 이메일
    const [userEmail, setUserEmail] = React.useState(null);

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
        navigate("/", {replace: true});
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

    function MoveToMyBlog(){
        if(userEmail !== null){
            navigate("/minihome", {state: {tabIndex: 0, targetEmail: userEmail}});
        }
        else{
            alert("로그인이 필요합니다.");
        } 
    }

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
                {/* <ListItemButton 
                    sx={{ alignItems: 'center' }}
                    onClick={() => { setLoginMenuVisible(!loginMenuVisible) }}
                >
                    <MenuIcon color='black' />
                </ListItemButton> */}
                
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
                                onClick={() => navigate('/qna/register')}
                                sx={{ width: '100%' ,color: 'black' }}
                            >
                                <ListItem>
                                    <ListItemButton disabled>
                                    <EditIcon />
                                    </ListItemButton>
                                    <ListItemText>질문 작성하기</ListItemText>
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