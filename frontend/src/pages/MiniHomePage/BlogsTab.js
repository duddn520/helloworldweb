import React from "react";
import { useNavigate } from 'react-router';
import { Button, Box, List } from "@mui/material";
import api from "../../api/api";
import BlogThumbnail from "../../component/miniHome/BlogThumbnail";

function BlogsTab({ userInfo }){
    const navigate = useNavigate();
    const [posts, setPosts] = React.useState([]);
    const [isOwner, setIsOwner] = React.useState(false);

    function moveToWrite(){
        navigate("/minihome/write", {state: {targetEmail: userInfo.email}});
    }

    React.useEffect(()=>{
        api.getBlogPosts(userInfo.email)
        .then(res => {
            setIsOwner(res.isOwner);
            setPosts(res.postResponseDtos);
        })
        .catch(e => {
            alert("게시물을 불러오는데 실패했습니다.");
        });
    },[]);

    return(
        <Box>
            <Box sx={{display: 'flex', alignItems: 'center', justifyContent: 'flex-end'}}>
                {/* <Typography variant='h5'>내 게시물</Typography> */}
                {isOwner && <Button onClick={moveToWrite} variant="outlined" sx={{width: 100}}>글쓰기</Button>}
            </Box>
            <Box>
                {posts.map((post)=>{
                    return(
                        <BlogThumbnail key={post.id} post={post} isOwner={isOwner}/>
                    )
                })}
            </Box>
        </Box>

    )
}

export default BlogsTab;