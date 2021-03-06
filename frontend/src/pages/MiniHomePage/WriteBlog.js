import React from "react";
import { Box, Button, Typography, TextField, Backdrop, CircularProgress } from "@mui/material";
import api from "../../api/api";
import { useNavigate, useLocation } from 'react-router';
import axios from "axios";
import strToHTML from "../../function/strToHTML";
import LoadingSpinner from "../../component/LoadingSpinner";
import ReactQuill, { Quill } from 'react-quill';
import 'react-quill/dist/quill.snow.css';
import ImageResize from 'quill-image-resize';
import { ContentCutSharp } from "@mui/icons-material";
Quill.register('modules/ImageResize', ImageResize);

function WriteBlog(){
    const navigate = useNavigate();
    const { state } = useLocation();
    const [targetEmail, setTargetEmail] = React.useState(state.targetEmail);
    const [isLoading, setIsLoading] = React.useState(false);
    const [post, setPost] = React.useState(state?.post);
    const [title, setTitle] = React.useState(state.post?.title === null || state.post?.title === undefined ? null : state.post.title );
    const [tags, setTags] = React.useState(state.post?.tags === null || state.post?.tags === undefined ? null : state.post.tags );
    const [blogContent, setBlogContent] = React.useState(state.post?.content === null || state.post?.content === undefined ? null : state.post.content);
    const [urls, setUrls] = React.useState([]);
    const [recentUrl, setRecentUrl]= React.useState("");
    const quillRef = React.useRef(null);

    React.useEffect(()=>{
        if(state.post?.postImageResponseDtos !== null && state.post?.postImageResponseDtos !== undefined){
            let imgArray = state.post.postImageResponseDtos;
            let newImgArray = [];
            imgArray.map((item) => {
                newImgArray.push(item.storedUrl);
            })
            console.log(newImgArray);
            setUrls(newImgArray);
        }
    },[])

    React.useEffect(()=>{
        if(recentUrl !== ""){
            let newArray = [...urls];
            newArray.push(recentUrl);
            setUrls(newArray);
        }
    },[recentUrl]);

    const imageHandler = () => { 
        const input = document.createElement("input"); 
        input.setAttribute("type", "file"); 
        input.setAttribute("accept", "image/*"); 
        input.click(); 

        let formdata = new FormData();
        input.onchange = async () => { const file = input.files[0]; 
            formdata.append('file', file);
            // ?????? ?????? ?????? ?????? 
            const range = quillRef.current.getEditor().getSelection(true); 
            // ????????? ?????????????????? ????????? ?????? placeholder ?????? 
            quillRef.current.getEditor().insertEmbed(range.index, "image", require('../../images/Loading.gif')); 
            try { 
                // ??? ????????? ???????????? ???????????? url ??????
                const res = await api.getImgUrl(formdata)
                let url = res;
                setRecentUrl(res);
                // // ??????????????? ????????? ????????? ?????? placeholder ?????? 
                quillRef.current.getEditor().deleteText(range.index, 1); 
                // ????????? url??? ????????? ????????? ?????? 
                quillRef.current.getEditor().insertEmbed(range.index, "image", url); 
                // ????????? ????????? ?????? ?????? ????????? ??????????????? ?????? 
                quillRef.current.getEditor().setSelection(range.index + 1);
            } catch (e) { 
                console.log(e);
                quillRef.current.getEditor().deleteText(range.index, 1); 
            } 
        }; 
    };


    // useMemo??? ???????????? ?????? handler??? ????????? ?????? ????????? ???????????? focus??? ?????????
    const modules = React.useMemo(() => ({
        toolbar: {
            // container??? ???????????? ???????????? tool ??????
            container: [
                [{ 'font': [] }], // font ??????
                [{ 'header': [1, 2, 3, 4, 5, 6, false] }], // header ??????
                ['bold', 'italic', 'underline','strike', 'blockquote', 'code-block', 'formula'], // ??????, ?????????, ?????? ??? ?????? tool ??????
                [{'list': 'ordered'}, {'list': 'bullet'}, {'indent': '-1'}, {'indent': '+1'}], // ?????????, ????????? ??????
                ['link', 'image'], // ??????, ????????? ????????? ??????
                [{ 'align': [] }, { 'color': ['#000000', '#ff0000', '#0000ff'] }, { 'background': [] }], // ??????, ?????? ??????, ?????? ????????? ??????
                ['clean'], // toolbar ?????? ????????? ??????
            ],

            // custom ????????? ??????
            handlers: {
                image: imageHandler, // ????????? tool ????????? ?????? ????????? ??????
            }
        },
        syntax: true,
        ImageResize: {
            parchment: Quill.import('parchment')
        }

    }), []);
    // toolbar??? ???????????? tool format
    const formats = [
        'font',
        'header',
        'bold', 'italic', 'underline', 'strike', 'blockquote', 'code-block', 'formula',
        'list', 'bullet', 'indent',
        'link', 'image',
        'align', 'color', 'background',        
    ];
    
    //OCR ??????
    function InvokeKakaoOcrApi(e){
        const range = quillRef.current.getEditor().getSelection(true); 
        // ????????? ?????????????????? ????????? ?????? placeholder ?????? 
        quillRef.current.getEditor().insertEmbed(range.index, "image", require('../../images/Loading.gif')); 
        const file = e.target.files[0];
        var total = ""
        let formData = new FormData();
        formData.append('image',file);
        axios.post("https://dapi.kakao.com/v2/vision/text/ocr",formData,{
            headers:{
                'Content-Type':'multipart/form-data',
                'Authorization':'KakaoAK c4a54b62084183bdcbb9b92b09a21e32'
            }
        }).then(res=>{

            console.log(res.data.result)
            const recognizedResult = res.data.result;
            
            for(let i = 0 ; i<recognizedResult.length ; i++)
            {
                let word = recognizedResult[i].recognition_words
                total += word
            }
            
            try { 
                // // ??????????????? ????????? ????????? ?????? placeholder ?????? 
                quillRef.current.getEditor().deleteText(range.index, 1); 
                // ????????? url??? ????????? ????????? ?????? 
                quillRef.current.getEditor().insertText(range.index, total, true);
                // ????????? ????????? ?????? ?????? ????????? ??????????????? ?????? 
                quillRef.current.getEditor().setSelection(range.index + 1);
            } catch (e) { 
                console.log(e);
                quillRef.current.getEditor().deleteText(range.index, 1); 
            } 

        }).catch(e =>{
            quillRef.current.getEditor().deleteText(range.index, 1);
            alert("????????? ??????????????????.");
        })
        
    }

    async function savePost(){
        setIsLoading(true);
        let content = strToHTML(blogContent);
        //NodeList??? Array??? ??????
        const contents = Array.from(content);
        //????????? ?????? ?????????
        let imageArray = [];
        contents.map((item)=>{
            if(item.getElementsByTagName('IMG').length !== 0){
                let images = Array.from(item.getElementsByTagName('IMG'));
                images.map((img)=>{
                    imageArray.push(img.src);
                })
            }
        });

        // allUploadUrl - imageArray ????????? ????????? ?????? ???????????? ?????? ??? ????????? ??????
        let difference = urls.filter(x => !imageArray.includes(x));
        console.log(difference);

        try{
            if(difference.length !== 0) await api.deleteImgUrl(difference);

            if(post === null || post === undefined){
                api.registerBlog(title, blogContent, tags, imageArray)
                .then(res => {
                        console.log('????????? ?????? ??????');
                        navigate("/minihome", {replace: true, state: {tabIndex: 1, targetEmail: targetEmail}});
                        setIsLoading(false);
                    })
                .catch(e => {
                    console.log('????????? ?????? ??????');
                    alert("?????? ??????");
                    setIsLoading(false);
                });
            }
            else{
                api.updateBlog(post.id, title, blogContent, tags, imageArray)
                .then(res => {
                    console.log('????????? ?????? ??????');
                    navigate("/minihome", {replace: true, state: {tabIndex: 1, targetEmail: targetEmail}});
                    setIsLoading(false);
                })
                .catch(e => {
                    console.log('????????? ?????? ??????');
                    alert("?????? ??????");
                    setIsLoading(false);
                });
            }
    
        }
        catch (e) {
            alert("??????");
        }
    }

    return(
        <div>
            <Box sx={{paddingTop: 3, paddingLeft: 2, paddingRight: 2, paddingBottom: 3, height: '90vh'}}>
                <Typography sx={{fontWeight: 'bold', marginBottom: 1 }}>??????</Typography>
                <Box sx={{flex: 1, display: 'flex', marginBottom: 2}}>
                    {/* <TitleInput id="Title" type="text" name="title"/> */}
                    <TextField 
                        id="Title"
                        sx={{ flex: 1, color: 'black' }}
                        size='small'
                        value={title}
                        onChange={(value)=>{setTitle(value.target.value)}}
                        placeholder='????????? ???????????????.'
                    />
                </Box>
                <Typography sx={{fontWeight: 'bold', marginBottom: 1}}>??????</Typography>
                <ReactQuill 
                theme="snow" 
                value={blogContent} 
                onChange={value => setBlogContent(value)}
                modules={modules} 
                formats={formats} 
                placeholder='????????? ???????????????.'
                style={{minHeight: '47vh'}}
                ref={quillRef}/>
                
                <Box sx={{marginBottom: 2, marginTop: 2}}>
                    <Button variant="outlined" component="label" sx={{height: 30}}> OCR
                        <input 
                        type="file"
                        accept="image/png, image/jpeg, image/jpg"
                        multiple={false}
                        onChange={InvokeKakaoOcrApi} 
                        hidden/>
                    </Button>
                </Box>
                <Typography sx={{fontWeight: 'bold', marginBottom: 1 }}>??????</Typography>
                <Box sx={{flex: 1, display: 'flex', marginBottom: 2}}>
                    <TextField 
                        id="Tags"
                        sx={{ flex: 1, color: 'black'}}
                        size='small'
                        placeholder='e.g. Java, Spring'
                        value={tags}
                        onChange={(value)=>{setTags(value.target.value)}}
                    />
                </Box>
                <Box sx={{flex: 1, justifyContent: 'flex-end', display: 'flex'}}>
                    <Button onClick={()=>{savePost()}} variant={'contained'}>??????</Button>
                </Box>
            </Box>
            <LoadingSpinner open={isLoading}/>
        </div>
    )
}

export default WriteBlog;