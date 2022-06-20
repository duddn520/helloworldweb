import React from "react";
import { useNavigate } from 'react-router';
import { Button, Box, Pagination, Typography, Backdrop, CircularProgress } from "@mui/material";
import api from "../../api/api";
import BlogThumbnail from "../../component/miniHome/BlogThumbnail";
import LoadingSpinner from "../../component/LoadingSpinner";

function BlogsTab({ userInfo }){
    const navigate = useNavigate();
    const [posts, setPosts] = React.useState([]);
    const [isOwner, setIsOwner] = React.useState(false);
    const [page,setPage] = React.useState(1);
    const [pageCount,setPageCount] = React.useState();
    const [isLoading, setIsLoading] = React.useState(false);

    const handlePageChange = (event,value) => {

        setIsLoading(true);
        api.getBlogPosts(userInfo.email, value-1)
        .then(res => {
            setIsOwner(res.isOwner);
            setPosts(res.postResponseDtoWithUser);
        })
        .catch(e => {
            alert("게시물을 불러오는데 실패했습니다.");
        });

        setPage(value);
        setIsLoading(false);

    }

    function moveToWrite(){
        navigate("/minihome/write", {state: {targetEmail: userInfo.email}});
    }

    React.useEffect(()=>{
        setIsLoading(true);
        api.getBlogPosts(userInfo.email, 0)
        .then(res => {
            setIsOwner(res.isOwner);
            setPosts(res.postResponseDtoWithUser);
            setPageCount(res.pageNum === 0 ? 1 : res.pageNum);
            setIsLoading(false);
        })
        .catch(e => {
            alert("게시물을 불러오는데 실패했습니다.");
            setIsLoading(false);
        });
    },[]);

    return(
        <Box>
            <Box sx={{display: 'flex', alignItems: 'center', justifyContent: 'flex-end'}}>
                {isOwner && <Button onClick={moveToWrite} variant="outlined" sx={{width: 100}}>글쓰기</Button>}
            </Box>
            <Box>
                {posts.length !== 0 ?
                <div>
                    {posts.map((post)=>{
                        return(
                            <BlogThumbnail key={post.id} post={post} isOwner={isOwner}/>
                        )
                    })}
                </div> : 
                <Box sx={{flex: 1, alignItems: 'center', paddingTop: 10, paddingBottom: 10, justifyContent: 'center', display: 'flex'}}>
                    <Typography sx={{color: 'gray'}}>게시물이 아직 없습니다.</Typography>
                </Box>}
                <Box sx={{flex: 1, alignItems: 'center', justifyContent: 'center', display: 'flex'}}>
                    <Pagination 
                        page={page} 
                        onChange={handlePageChange} 
                        count={pageCount} 
                        showFirstButton showLastButton 
                    />
                </Box>
            </Box>
            <div>
                <LoadingSpinner open={isLoading}/>
            </div>
        </Box>

    )
}

export default BlogsTab;