import React from "react";
import { useNavigate } from 'react-router';
import { Button, Box, Typography } from "@mui/material";
import api from "../../api/api";
import PostThumbnail from "../../component/miniHome/PostThumbnail";

function Posts(){
    const navigate = useNavigate();
    const [posts, setPosts] = React.useState([]);

    function moveToWrite(){
        navigate("/minihome/write");
    }

    React.useEffect(()=>{
        api.getMyBlogPosts()
        .then(res => {
            setPosts(res);
        })
        .catch(e => {
            alert("게시물을 불러오는데 실패했습니다.");
        });
    },[])

    return(
        <div>
            <Box sx={{display: 'flex', alignItems: 'center', justifyContent: 'space-between'}}>
                <Typography variant='h5'>내 게시물</Typography>
                <Button onClick={moveToWrite} variant="outlined" sx={{width: 100}}>글쓰기</Button>
            </Box>
            {posts.map((post)=>{
                return(
                    <PostThumbnail key={post.id} id={post.id} title={post.title}/>
                )
            })}
        </div>
    )
}

export default Posts;