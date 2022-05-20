import React from "react";
import { Box ,Container ,Typography ,Button} from "@mui/material";
import { GitHub as GitHubIcon ,AccessTime as AccessTimeIcon } from "@mui/icons-material";
import User from "../../component/blog/User";
import api from "../../api/api";
import RepoCardFlatList from "../../component/blog/RepoCardFlatList";

function MyInfoTab({ userInfo }){
    // 탭
    const [tabIndex,setTabIndex] = React.useState(1);
    // 작성한 질문들
    const [qnas,setQnas] = React.useState([]);
    // 작성한 답변들
    const [comments,setComments] = React.useState([]);
    React.useEffect(()=>{

        // 이 유저가 올린 질문들 조회
        api.getUserQnas(userInfo.id)
        .then( res => {
            setQnas(res);
        })
        .catch( e => {
        })
        // 이 유저가 올린 답변들 조회
        api.getUserComments(userInfo.id)
        .then( res => {
           setComments(res);
        })
        .catch( e => {
        })
        

    },[]);

    return(
        <Box>
            <Container sx={{ border: 1,borderRadius: 3 ,borderColor: 'lightgray' ,width: 200 ,m: 4 ,justifyContent: 'start' }}>
                    <Typography sx={{ mt: 1 ,fontSize: 20 ,fontWeight: 'bold' }}>🏅 Stats</Typography>
                    <Box sx={{ m: 1 }}>
                        <Typography sx={{ fontSize: 15}}>{qnas.length}</Typography>
                        <Typography sx={{ color: 'gray',fontSize: 15}}>질문</Typography>
                    </Box>
                    <Box sx={{ m: 1 }}>
                        <Typography sx={{ fontSize: 15}}>{qnas.length}</Typography>
                        <Typography sx={{ color: 'gray',fontSize: 15}}>답변</Typography>
                    </Box>
            </Container>

            <Container sx={{ m: 2 ,display: 'flex' ,flexDirection: 'row' }}>
                <Button 
                    
                    onClick={() => setTabIndex(1)}
                    variant="contained" 
                    sx={{ backgroundColor: tabIndex == 1 ? 'orange' : 'lightgray' ,boxShadow: 0 ,":hover":{ boxShadow:0, backgroundColor: tabIndex == 1 ? "orange" : "lightgray" } ,textTransform: 'none' }}>
                        <AccessTimeIcon sx={{ mr: 1 }}/>
                        활동
                </Button>
                <Button 
                    onClick={() => setTabIndex(2)}
                    variant="contained" 
                    sx={{ backgroundColor: tabIndex == 2 ? 'orange' : 'lightgray' ,boxShadow: 0 ,ml: 2 ,":hover":{ boxShadow:0, backgroundColor: tabIndex == 2 ? "orange" : "lightgray"} ,textTransform: 'none' }}>
                        <GitHubIcon sx={{ mr: 1 }}/>
                        깃허브
                </Button>
            </Container>
            {
                tabIndex == 1 && <User qnas={qnas} comments={comments}/>
            }
            {
                tabIndex == 2 && <RepoCardFlatList email={userInfo.email} isOwner={userInfo.isOwner}/>
            }
        </Box>
    )
}

export default MyInfoTab;