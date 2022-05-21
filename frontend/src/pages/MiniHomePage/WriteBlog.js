import React from "react";
import { Box, Button, Typography, TextField } from "@mui/material";
import api from "../../api/api";
import { useNavigate, useLocation } from 'react-router';
import styled from "@emotion/styled";
import imageCompression from "browser-image-compression";
import axios from "axios";
import strToHTML from "../../function/strToHTML";
import { convertBase64IntoFile, extractOnlyFilename } from "../../function/aboutFile";
import LoadingSpinner from "../../component/LoadingSpinner";
import ReactQuill, { Quill } from 'react-quill';
import 'react-quill/dist/quill.snow.css';
import ImageResize from 'quill-image-resize';
Quill.register('modules/ImageResize', ImageResize);

const WriteSpace = styled.div`
    padding: 10px; 
    border: 1px solid lightgray;
    border-radius: 4px;
    height: 47vh;
    overflow: auto;
    margin-bottom: 10px;
`;


function WriteBlog(){
    const navigate = useNavigate();
    const { state } = useLocation();
    const [targetEmail, setTargetEmail] = React.useState(state.targetEmail);
    const [isLoading, setIsLoading] = React.useState(false);
    const [post, setPost] = React.useState(state?.post);
    const [title, setTitle] = React.useState(state.post?.title === null || state.post?.title === undefined ? null : state.post.title );
    const [tags, setTags] = React.useState(state.post?.tags === null || state.post?.tags === undefined ? null : state.post.tags );
    const [value, setValue] = React.useState(" ");
    const quillRef = React.useRef<ReactQuill>(null);

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

            // // custom 핸들러 설정
            // handlers: {
            //     image: imageHandler, // 이미지 tool 사용에 대한 핸들러 설정
            // }
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
    ]

    React.useEffect(()=>{

        let divC = document.getElementById('Content');
        let imageIndex = 0;
        if(post !== null && post !== undefined){
            const contentArray = post.content.split("\n");
            for (let i = 0 ; i < contentArray.length ; i += 1) {
                let currentLine = contentArray[i];

                if(currentLine === '&&&&'){
                    let currentImg = post.postImageResponseDtos[imageIndex];
                    let imgNode = document.createElement('img');
                    imgNode.setAttribute("src", currentImg.storedUrl);
                    imgNode.setAttribute("name", currentImg.originalFileName);
                    imgNode.setAttribute("base64", currentImg.base64); // base64 인코딩된 값
                    imgNode.setAttribute("variant", "contained");
                    imgNode.setAttribute("alt", currentImg.storedUrl);
                    imgNode.style.maxWidth = '200px';
                    imgNode.style.maxHeight = 'auto';
                    divC.appendChild(imgNode);
                    imageIndex += 1;
                }
                else {
                    if(i === 0){
                        divC.append(contentArray[i]);
                    }
                    else{
                        let divNode = document.createElement('div');
                        divNode.append(contentArray[i]);
                        divC.appendChild(divNode);
                    }        
                }
            }
        }
    }, []);

    const imageReader = async function(e) {
        const selectedImage = e.target.files[0];
        const options = { 
            maxSizeMB: 10, 
            maxWidthOrHeight: 400
        }
        const compressedFile = await imageCompression(selectedImage, options);
        //blob 생성 
        let blob = new Blob([compressedFile], { type: ["image/png", "image/jpeg", "image/jpg"] });
        //url 생성
        let url = window.URL.createObjectURL(blob);

        var fileReader = new FileReader();
        fileReader.readAsDataURL(selectedImage);
    
        fileReader.onload = function(e) { 
            let imgNode = document.createElement('img');
            imgNode.setAttribute("src", url);
            imgNode.setAttribute("name", extractOnlyFilename(selectedImage.name)+"."+selectedImage.type.split("/")[1]);
            imgNode.setAttribute("base64", e.target.result); // base64 인코딩된 값
            imgNode.setAttribute("variant", "contained");
            imgNode.setAttribute("alt", selectedImage.name);
            imgNode.style.maxWidth = '200px';
            imgNode.style.maxHeight = 'auto';

            // document.getElementById('Content').removeChild(document.getElementById('Content').lastElementChild);
            document.getElementById('Content').appendChild(imgNode);

            const selection = window.getSelection();
            const newRange = document.createRange();
            newRange.selectNodeContents(document.getElementById('Content'));
            newRange.collapse(false);
            selection?.removeAllRanges();
            selection?.addRange(newRange);
        }
    }
    
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

    //마지막으로 서버에 보낼 정보 만들기
    function savePost(){
        setIsLoading(true);
        const divC = document.getElementById('Content');
        let content = strToHTML(divC.innerHTML);

        let formdata = new FormData();
        let totalContent = '';

        for(let i = 0; i < content.length; i++){
            if(content[i].tagName === 'IMG'){
                let imgUri = content[i].currentSrc;
                let imgBase64 = content[i].getAttribute('base64');
                let name = content[i].getAttribute('name');
                let nameTempArray = name.split(".");
                let NewName = name.replace('.'+name.split('.')[ nameTempArray.length - 1 ], i+'.'+name.split('.')[ nameTempArray.length - 1 ]);
                formdata.append('files', convertBase64IntoFile(imgBase64, NewName));

                totalContent += "&&&&\n";
                if(imgUri.split('.') !== 'https://helloworldweb-fileserver' ){
                // url 사용 후에 메모리에서 제거하기
                window.URL.revokeObjectURL(imgUri);

                console.log("&&&&\n");
                }            
            }
            else{
                if( i === 0) {
                    totalContent += content[i].textContent === '' ? '\n' : content[i].textContent+'\n';
                }
                else {
                    totalContent += content[i].innerHTML === '<br>' ? content[i+1].tagName === 'IMG' ? '' : '\n' : content[i].innerText+'\n' ;
                }
            }
        }
        // console.log(totalContent);
        if(post === null || post === undefined){
            api.registerPost(formdata, title, totalContent, tags)
            .then(res => {
                console.log('블로그 게시 성공');
                navigate("/minihome", {replace: true, state: {tabIndex: 1, targetEmail: targetEmail}});
                setIsLoading(false);
            })
            .catch(e => {
                console.log('블로그 게시 실패');
                alert("작성 실패");
                setIsLoading(false);
            });
        }
        else{
            api.updatePost(post.id, formdata, title, totalContent, tags)
            .then(res => {
                console.log('블로그 수정 성공');
                navigate("/minihome", {replace: true, state: {tabIndex: 1, targetEmail: targetEmail}});
                setIsLoading(false);
            })
            .catch(e => {
                console.log('블로그 수정 실패');
                alert("수정 실패");
                setIsLoading(false);
            });
        }
       
    }

    function tempSave(){
        let formdata = new FormData();
        api.registerBlog(formdata, title, value, tags)
        .then(res => {
                console.log('블로그 게시 성공');
                navigate("/minihome", {replace: true, state: {tabIndex: 1, targetEmail: targetEmail}});
                setIsLoading(false);
            })
        .catch(e => {
            console.log('블로그 게시 실패');
            alert("작성 실패");
            setIsLoading(false);
        });
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
                value={value} 
                onChange={setValue}
                modules={modules} 
                formats={formats} 
                placeholder='내용을 입력하세요.'
                style={{height: '55vh'}}/>
                
                <Box sx={{marginBottom: 2, marginTop: 8}}>
                    <Button component="label" variant="outlined" sx={{height: 30, marginRight: 2}}> 이미지 업로드
                    <input 
                        type="file"
                        id="avatar" name="avatar"
                        accept="image/png, image/jpeg, image/jpg"
                        multiple={false}
                        onChange={imageReader} hidden/>
                    </Button>

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
                    <Button onClick={()=>{tempSave()}} variant={'contained'}>저장</Button>
                </Box>
            </Box>
            {isLoading && <LoadingSpinner/>}
        </div>
    )
}

export default WriteBlog;