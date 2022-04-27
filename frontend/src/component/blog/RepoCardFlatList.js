import React, {useEffect} from "react";
import FlatList from 'flatlist-react';
import RepoCard from "./RepoCard";
import request from "../../api/request";
import {Typography} from "@mui/material";
//useEffect사용하여 getPosts 로 postList 받고, 렌더링.

export default function RepoCardFlatList()
{
    const [jsonList,setJsonList] = React.useState([])
    const [flag,setFlag] = React.useState([])

    useEffect(()=>{

        request({
            method:"GET",
            url:"http://localhost:8080/user/repos_url",
        })
            .then( res =>{
                if(res.data.statusCode===211)
                {
                    setJsonList(res.data.data)
                    setFlag([true])
                }
                else if(res.data.statusCode===204)
                {
                    setFlag([false])
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
    else if(flag[0]===false)
    {
        return(
            <div>
                <Typography variant="h5">Github 연동이 필요합니다.</Typography>
            </div>
        )
    }
    else
    {
        return(
            <div>
                <Typography variant="h5">서버 오류.</Typography>
            </div>
        )
    }

}