import React from "react";
import { Box, Button, Typography, TextField } from "@mui/material";
import api from "../../api/api";
import { useNavigate, useLocation } from 'react-router';
import axios from "axios";
import strToHTML from "../../function/strToHTML";
import LoadingSpinner from "../../component/LoadingSpinner";
import ReactQuill, { Quill } from 'react-quill';
import 'react-quill/dist/quill.snow.css';
import ImageResize from 'quill-image-resize';
Quill.register('modules/ImageResize', ImageResize);



function WriteBlog(){
    const navigate = useNavigate();
    const { state } = useLocation();
    const [targetEmail, setTargetEmail] = React.useState(state.targetEmail);
    const [isLoading, setIsLoading] = React.useState(false);
    const [post, setPost] = React.useState(state?.post);
    const [title, setTitle] = React.useState(state.post?.title === null || state.post?.title === undefined ? null : state.post.title );
    const [tags, setTags] = React.useState(state.post?.tags === null || state.post?.tags === undefined ? null : state.post.tags );
    const [blogContent, setBlogContent] = React.useState(" ");
    const [urls, setUrls] = React.useState([]);
    const [recentUrl, setRecentUrl]= React.useState("");
    const quillRef = React.useRef(null);

    React.useEffect(()=>{
        if(recentUrl !== ""){
            let newArray = [...urls];
            console.log(recentUrl);
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
            // 현재 커서 위치 저장 
            const range = quillRef.current.getEditor().getSelection(true); 
            // 서버에 올려질때까지 표시할 로딩 placeholder 삽입 
            quillRef.current.getEditor().insertEmbed(range.index, "image", require('../../images/Loading.gif')); 
            try { 
                // 이 부분에 서버에서 리턴받은 url 얻기
                const res = await api.getImgUrl(formdata)
                let url = res;
                setRecentUrl(res);
                // // 정상적으로 업로드 됐다면 로딩 placeholder 삭제 
                quillRef.current.getEditor().deleteText(range.index, 1); 
                // 받아온 url을 이미지 태그에 삽입 
                quillRef.current.getEditor().insertEmbed(range.index, "image", url); 
                // 사용자 편의를 위해 커서 이미지 오른쪽으로 이동 
                quillRef.current.getEditor().setSelection(range.index + 1);
            } catch (e) { 
                console.log(e);
                quillRef.current.getEditor().deleteText(range.index, 1); 
            } 
        }; 
    };


    // useMemo를 사용하지 않고 handler를 등록할 경우 타이핑 할때마다 focus가 벗어남
    const modules = React.useMemo(() => ({
        toolbar: {
            // container에 등록되는 순서대로 tool 배치
            container: [
                [{ 'font': [] }], // font 설정
                [{ 'header': [1, 2, 3, 4, 5, 6, false] }], // header 설정
                ['bold', 'italic', 'underline','strike', 'blockquote', 'code-block', 'formula'], // 굵기, 기울기, 밑줄 등 부가 tool 설정
                [{'list': 'ordered'}, {'list': 'bullet'}, {'indent': '-1'}, {'indent': '+1'}], // 리스트, 인덴트 설정
                ['link', 'image'], // 링크, 이미지 업로드 설정
                [{ 'align': [] }, { 'color': ['#000000', '#ff0000', '#0000ff'] }, { 'background': [] }], // 정렬, 글씨 색깔, 글씨 배경색 설정
                ['clean'], // toolbar 설정 초기화 설정
            ],

            // custom 핸들러 설정
            handlers: {
                image: imageHandler, // 이미지 tool 사용에 대한 핸들러 설정
            }
        },
        syntax: true,
        ImageResize: {
            parchment: Quill.import('parchment')
        }

    }), []);
    // toolbar에 사용되는 tool format
    const formats = [
        'font',
        'header',
        'bold', 'italic', 'underline', 'strike', 'blockquote', 'code-block', 'formula',
        'list', 'bullet', 'indent',
        'link', 'image',
        'align', 'color', 'background',        
    ];
    
    //OCR 기능
    function InvokeKakaoOcrApi(e){
        const file = e.target.files[0];
        var total = ""
        let formData = new FormData();
        formData.append('image',file);
        console.log(file)
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
                console.log(word)
                total += word
            }

            const newDiv = document.createElement('div');
            const newText = document.createTextNode(`${total}`);
            newDiv.appendChild(newText);
            document.getElementById('Content').appendChild(newDiv);

        }).catch(e =>{
            console.log(e)
        })
        
    }

    async function savePost(){
        let content = strToHTML(blogContent);

        //NodeList를 Array로 변환
        const contents = Array.from(content);
        //이미지 배열 만들기
        let imageArray = [];
        contents.map((item)=>{
            if(item.getElementsByTagName('IMG').length !== 0){
                let images = Array.from(item.getElementsByTagName('IMG'));
                images.map((img)=>{
                    imageArray.push(img.currentSrc);
                })
            }
        });

        // allUploadUrl - imageArray 차집합 구해서 파일 서버에서 삭제 후 게시물 저장
        let difference = urls.filter(x => !imageArray.includes(x));
        console.log(difference);

        try{
            await api.deleteImgUrl(difference);

            // if(post === null || post === undefined){
            //     api.registerBlog(title, blogContent, tags, imageArray)
            //     .then(res => {
            //             console.log('블로그 게시 성공');
            //             navigate("/minihome", {replace: true, state: {tabIndex: 1, targetEmail: targetEmail}});
            //             setIsLoading(false);
            //         })
            //     .catch(e => {
            //         console.log('블로그 게시 실패');
            //         alert("작성 실패");
            //         setIsLoading(false);
            //     });
            // }
            // else{
            //     api.updatePost(post.id, title, blogContent, tags, imageArray)
            //     .then(res => {
            //         console.log('블로그 수정 성공');
            //         navigate("/minihome", {replace: true, state: {tabIndex: 1, targetEmail: targetEmail}});
            //         setIsLoading(false);
            //     })
            //     .catch(e => {
            //         console.log('블로그 수정 실패');
            //         alert("수정 실패");
            //         setIsLoading(false);
            //     });
            // }
    
        }
        catch (e) {
            alert("삭제 실패");
        }
        

       
    }

    return(
        <div>
            <Box sx={{paddingTop: 3, paddingLeft: 2, paddingRight: 2, paddingBottom: 3, height: '90vh'}}>
                <Typography sx={{fontWeight: 'bold', marginBottom: 1 }}>제목</Typography>
                <Box sx={{flex: 1, display: 'flex', marginBottom: 2}}>
                    {/* <TitleInput id="Title" type="text" name="title"/> */}
                    <TextField 
                        id="Title"
                        sx={{ flex: 1, color: 'black' }}
                        size='small'
                        value={title}
                        onChange={(value)=>{setTitle(value.target.value)}}
                        placeholder='제목을 입력하세요.'
                    />
                </Box>
                <Typography sx={{fontWeight: 'bold', marginBottom: 1}}>내용</Typography>
                {/* <WriteSpace id="Content" contentEditable="true"/> */}
                <ReactQuill 
                theme="snow" 
                value={blogContent} 
                onChange={value => setBlogContent(value)}
                modules={modules} 
                formats={formats} 
                placeholder='내용을 입력하세요.'
                style={{height: '55vh'}}
                ref={quillRef}/>
                
                <Box sx={{marginBottom: 2, marginTop: 8}}>
                    <Button variant="outlined" component="label" sx={{height: 30}}> OCR

                        <input 
                        type="file"
                        accept="image/png, image/jpeg, image/jpg"
                        multiple={false}
                        onChange={InvokeKakaoOcrApi} hidden/>
                    </Button>
                </Box>
                <Typography sx={{fontWeight: 'bold', marginBottom: 1 }}>태그</Typography>
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
                    <Button onClick={()=>{savePost()}} variant={'contained'}>저장</Button>
                </Box>
            </Box>
            {isLoading && <LoadingSpinner/>}
        </div>
    )
}

export default WriteBlog;