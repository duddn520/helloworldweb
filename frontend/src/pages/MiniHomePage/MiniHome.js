import React from 'react';
import { Box } from '@mui/material';
import ChromeTabBar from '../../component/miniHome/ChromeTabBar'
import Profile from './Profile';
import MyInfo from './MyInfo';
import Posts from './Posts';
import GuestBooks from './GuestBooks';

function Main() {
  const [index, setIndex] = React.useState(0);
  
  return (
    <Box sx={{display: 'flex'}}>
        <Box sx={{flex: 1, textAlign: 'center', pt: 10}}>
            <Profile/>
        </Box>
        <Box sx={{flex: 3, backgroundColor: 'white', paddingRight: 15, paddingLeft: 5, borderLeft: 1, borderColor: 'lightgray', minHeight: '100vmin', width: '100vmin'}}>
            <div>
                <ChromeTabBar index={index} setIndex={setIndex} tabNames={['내 정보', '게시물', '방명록']}/>
                <Box sx={{ border: 1, borderColor: '#E6E8EB', paddingLeft: 5, paddingRight: 5, wordBreak: 'normal', wordWrap: 'break-word'}}>
                    {index === 0 && <MyInfo/>}
                    {index === 1 && <Posts/>}
                    {index === 2 && <GuestBooks/>}
                </Box>
            </div> 
        </Box>
    </Box>
  );
}

export default Main;