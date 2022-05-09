import React from "react";
import { Box } from "@mui/material";
import RepoCardFlatList from "../../component/blog/RepoCardFlatList";

function MyInfoTab({userInfo}){
    
    return(
        <Box>
            <RepoCardFlatList email={userInfo.email} isOwner={userInfo.isOwner}/>
        </Box>
    )
}

export default MyInfoTab;