import React from "react";
import { useNavigate } from 'react-router';
import { Button, Box, Pagination } from "@mui/material";
import api from "../../api/api";
import BlogThumbnail from "../../component/miniHome/BlogThumbnail";

function BlogsTab({ userInfo }){
    const navigate = useNavigate();
    const [posts, setPosts] = React.useState([]);
    const [isOwner, setIsOwner] = React.useState(false);
    const [page,setPage] = React.useState(1);
    const [pageCount,setPageCount] = React.useState();

    const handlePageChange = (event,value) => {

        api.getBlogPosts(userInfo.email, value-1)
        .then(res => {
            setIsOwner(res.isOwner);
            setPosts(res.postResponseDtos);
        })
        .catch(e => {
            alert("게시물을 불러오는데 실패했습니다.");
        });

        setPage(value);

    }

    function moveToWrite(){
        navigate("/minihome/write", {state: {targetEmail: userInfo.email}});
    }

    React.useEffect(()=>{
        api.getBlogPosts(userInfo.email, 0)
        .then(res => {
            setIsOwner(res.isOwner);
            setPosts(res.postResponseDtos);
            setPageCount(res.pageNum);
        })
        .catch(e => {
            alert("게시물을 불러오는데 실패했습니다.");
        });
    },[]);

    return(
        <Box>
            <Box sx={{display: 'flex', alignItems: 'center', justifyContent: 'flex-end'}}>
                {isOwner && <Button onClick={moveToWrite} variant="outlined" sx={{width: 100}}>글쓰기</Button>}
            </Box>
            <Box>
                {posts.map((post)=>{
                    return(
                        <BlogThumbnail key={post.id} post={post} isOwner={isOwner}/>
                    )
                })}
                <Box sx={{ justifyContent : 'center' ,display: 'flex'}}>
                    <Box sx={{ flex: 1 }}/>
                    <Pagination 
                        page={page} 
                        onChange={handlePageChange} 
                        count={pageCount} 
                        showFirstButton showLastButton 
                        sx={{ flex: 3 ,m: 2}}
                    />
                    <Box sx={{ flex: 1 }} />
                </Box>
            </Box>
        </Box>

    )
}

export default BlogsTab;