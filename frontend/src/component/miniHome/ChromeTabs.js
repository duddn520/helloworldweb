import * as React from 'react';
import { styled } from '@mui/material/styles';
import Tabs from '@mui/material/Tabs';
import Tab from '@mui/material/Tab';
import Box from '@mui/material/Box';
import Color from 'color';
import PropTypes from 'prop-types';

const TabHeight = 40;
const TabWidth = 70;

const TotalTabs = styled(Tabs)({
  borderBottom: '1px solid #e8e8e8',
  '& .MuiTabs-indicator': {
    backgroundColor: '#272C34',
    display: 'none'
  },
  minHeight: TabHeight,
});

const ChromeTab = styled((props) => <Tab disableRipple {...props} />)(({ theme }) => ({
    backgroundColor: theme.palette.grey[300],
    opacity: 1,
    overflow: 'initial',
    paddingLeft: theme.spacing(2),
    paddingRight: theme.spacing(2),
    borderTopLeftRadius: theme.spacing(1),
    borderTopRightRadius: theme.spacing(1),
    color: Color(theme.palette.grey[300]).isLight() ? theme.palette.text.primary : theme.palette.common.white,
    transition: '0.2s',
    textTransform: 'none',
    minWidth: 0,
    [theme.breakpoints.up('md')]: {
        minWidth: TabWidth,
    },
    minHeight: TabHeight,
    fontWeight: theme.typography.fontWeightRegular,
    fontFamily: [
        '-apple-system',
        'BlinkMacSystemFont',
        '"Segoe UI"',
        'Roboto',
        '"Helvetica Neue"',
        'Arial',
        'sans-serif',
        '"Apple Color Emoji"',
        '"Segoe UI Emoji"',
        '"Segoe UI Symbol"',
    ].join(','),
    '&:hover': {
        opacity: 1,
        backgroundColor: Color(theme.palette.grey[300])
        .whiten(0.6)
        .hex(),
    },
    '&.Mui-selected': {
        backgroundColor: '#272C34',
        color: Color('#272C34').isLight() ? theme.palette.text.primary : theme.palette.common.white,
        '& + $root': {
          zIndex: 1,
        },
        '& + $root:before': {
          opacity: 0,
        },
        fontWeight: theme.typography.fontWeightMedium,
    },
    '&.Mui-focusVisible': {
        backgroundColor: '#d1eaff',
    },
    '&:before': {
        transition: '0.2s',
    },
}));

function ChromeTabs({ tabs, tabStyle, tabProps, ...props }) {

  return (
    <Box sx={{ width: '100%' }}>
        <TotalTabs {...props} >
            {tabs.map(tab => (
              <ChromeTab key={tab.label} {...tabProps} {...tab}/>
            ))}
        </TotalTabs>
    </Box>
  );
}

ChromeTabs.propTypes = {
    tabs: PropTypes.arrayOf(
      PropTypes.shape({
        label: PropTypes.node.isRequired,
      }),
    ),
    tabStyle: PropTypes.shape({
      bgColor: PropTypes.string,
      minWidth: PropTypes.shape({}),
    }),
    tabProps: PropTypes.shape({}),
  };
  ChromeTabs.defaultProps = {
    tabs: [],
    tabStyle: {},
    tabProps: {},
  };

export default ChromeTabs;

