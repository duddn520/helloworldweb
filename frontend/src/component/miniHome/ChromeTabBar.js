import React from "react";
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import ChromeTabs from './ChromeTabs';

function ChromeTabBar(props){

    //ChromeTabBar 구성할 때 각 레이블 이름 설정
    //tabNames: 사용할 이름들을 string의 배열
    const changeTabName = (tabNames) => {
        let newNames = []
        tabNames.map((item)=>{
            newNames.push({label: item});
        })
        return newNames;
    }

    return(
        <AppBar
            position={'static'}
            elevation={0}
            sx={{ backgroundColor: 'white' }}
          >
            <Toolbar style={{ padding: 0 }}>
              <ChromeTabs
                style={{ alignSelf: 'flex-end' }}
                tabs={changeTabName(props.tabNames)}
                tabStyle={{
                  bgColor: '#E6E8EB',
                  selectedBgColor: '#272C34',
                }}
                tabProps={{
                  disableRipple: true,
                }}
                value={props.index}
                onChange={(e, i) => props.setIndex(i)}
              />
            </Toolbar>
          </AppBar>
    )
}

export default ChromeTabBar;