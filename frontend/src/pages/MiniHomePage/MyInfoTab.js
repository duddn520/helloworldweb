import React from "react";
import { Box ,Container ,Typography ,Button} from "@mui/material";
import User from "../../component/blog/User";
import api from "../../api/api";
import RepoCardFlatList from "../../component/blog/RepoCardFlatList";

function MyInfoTab({ userInfo }){
    // 탭
    const [tabIndex,setTabIndex] = React.useState(1);
    //
    const [qnas,setQnas] = React.useState([]);

    React.useEffect(()=>{
        
        // 이 유저가 올린 질문들 조회
        api.getUserQnas(userInfo.id)
        .then( res => {
            setQnas(res);
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
                        <Typography sx={{ color: 'lightgray',fontSize: 15}}>질문</Typography>
                    </Box>
                    <Box sx={{ m: 1 }}>
                        <Typography sx={{ fontSize: 15}}>{qnas.length}</Typography>
                        <Typography sx={{ color: 'lightgray',fontSize: 15}}>답변</Typography>
                    </Box>
            </Container>

            <Container sx={{ m: 2 ,display: 'flex' ,flexDirection: 'row' }}>
                <Button 
                    onClick={() => setTabIndex(1)}
                    variant="contained" 
                    sx={{ backgroundColor: tabIndex == 1 ? 'orange' : 'lightgray' ,boxShadow: 0 ,":hover":{ boxShadow:0, backgroundColor: tabIndex == 1 ? "orange" : "lightgray" } ,textTransform: 'none' }}>
                        Activity
                </Button>
                <Button 
                    onClick={() => setTabIndex(2)}
                    variant="contained" 
                    sx={{ backgroundColor: tabIndex == 2 ? 'orange' : 'lightgray' ,boxShadow: 0 ,ml: 2 ,":hover":{ boxShadow:0, backgroundColor: tabIndex == 2 ? "orange" : "lightgray"} ,textTransform: 'none' }}>
                        Github Repositories
                </Button>
            </Container>
            {
                tabIndex == 1 && <User qnas={qnas}/>
            }
            {
                tabIndex == 2 && <RepoCardFlatList />
            }
        </Box>
    )
}

export default MyInfoTab;