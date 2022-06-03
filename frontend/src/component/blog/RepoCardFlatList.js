import React, {useEffect} from "react";
import FlatList from 'flatlist-react';
import RepoCard from "./RepoCard";
import request from "../../api/request";
import {Avatar, Box, Button, Typography} from "@mui/material";
import api from "../../api/api";
//useEffect사용하여 getPosts 로 postList 받고, 렌더링.

export default function RepoCardFlatList({email, isOwner})
{
    const [jsonList,setJsonList] = React.useState([]);
    const [flag,setFlag] = React.useState([false]);
    const [userEmail,setUserEmail] = React.useState(email);

    const github_url = "https://github.com/login/oauth/authorize?client_id=105e0b50eefc27b4dc81&redirect_uri=http://3.35.188.47/login/redirect/github/connect";

    function getGitCode(){
        window.location.replace(github_url)

    }
    useEffect(()=>{
        api.getGithubRepositories(userEmail)
            .then( res =>{

                if(res?.status===200)   //github 연동 되어있는 경우
                {
                    setJsonList(res.data.data);
                    setFlag([true]);
                }
                else if(res?.status===204) //github 연동 안되어있는 경우
                {
                    setFlag([false]);
                    console.log(res.data.data);
                }
            })
            .catch( e=>{
                console.log(e)
            })

    },[]);

    const renderRepo = (repo) => {
        return (
                <RepoCard title={repo.name} content={repo.description} fullName={repo.full_name} htmlUrl={repo.html_url} createdAt={repo.created_at}
                          sx={{}}
                />
        );
    }

    if(flag[0]===true) {

        return (
            <ul>
                <FlatList
                    list={jsonList}
                    renderItem={renderRepo}
                    renderWhenEmpty={() => <div>List is empty!</div>}
                    display={{
                        grid: true,
                    }}
                    minColumnWidth={`${window.screen.width * 0.3}px`}
                />
            </ul>
        )
    }
    else if(flag[0]===false && isOwner)
    {
        return(
            <div>
                <Box sx={{
                    display:"flex",
                    alignItems:"center",
                    justifyContent:"center",
                    m:2
                }}>
                    <Typography variant="h5"
                            sx={{alignItems:"center"}}>
                        Github 연동이 필요합니다.
                    </Typography>
                </Box>
                <Box sx={{
                    display:"flex",
                    alignItems:"center",
                    justifyContent:"center",
                    m:3
                }}>
                    <Button
                        onClick={getGitCode} 
                        startIcon={<Avatar sx={{position : "absolute", left:5, alignSelf:"center", bottom:4.2}} src={require(`../../images/github_icon.png`)} alt='LoginButtonImage' width='50' height='50' />}
                        sx={{ textTransform: 'none' ,color: 'gray', width : 300, height: 50, fontSize:14 ,backgroundColor : "white" ,boxShadow:0 , borderColor:"gray", borderWidth:0.9 ,":hover":{ boxShadow:0, backgroundColor:"whitesmoke", borderColor:"gray"}}}
                        variant={"outlined"}
                    >
                        Github 연동하기
                    </Button>
                </Box>
            </div>
        )
    }
    else 
    {
        return(
            <div>
            </div>
        )
    }

}