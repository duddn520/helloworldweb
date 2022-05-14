import React from "react";
import { Box, Button, Typography, TextField } from "@mui/material";
import api from "../../api/api";
import { useNavigate, useLocation } from 'react-router';
import styled from "@emotion/styled";
import imageCompression from "browser-image-compression";
import axios from "axios";
import strToHTML from "../../function/strToHTML";
import { convertBase64IntoFile, extractOnlyFilename } from "../../function/aboutFile";

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

    const imageReader = async function(e) {
        const selectedImage = e.target.files[0];
        const options = { 
            maxSizeMB: 2, 
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
            imgNode.setAttribute("maxWidth", 800);
            imgNode.setAttribute("maxHeight", 400);
            imgNode.setAttribute("variant", "contained");
            imgNode.setAttribute("alt", selectedImage.name);

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
        const divC = document.getElementById('Content');
        const TitleElement = document.getElementById('Title');
        const TagsElement = document.getElementById('Tags');
        let content = strToHTML(divC.innerHTML);
        let title = TitleElement.value;
        let tags = TagsElement.value;

        let formdata = new FormData();
        let totalContent = '';

        for(let i = 0; i < content.length; i++){
            if(content[i].tagName === 'IMG'){
                let imgUri = content[i].currentSrc;
                let imgBase64 = content[i].getAttribute('base64');
                let name = content[i].getAttribute('name');
                let nameTempArray = name.split(".");
                let NewName = name.replace('.'+name.split('.')[ nameTempArray.length - 1 ], i+'.'+name.split('.')[ nameTempArray.length - 1 ]);
                console.log(convertBase64IntoFile(imgBase64, NewName));
                formdata.append('files', convertBase64IntoFile(imgBase64, NewName));

                totalContent += "&&&&\n";

                // url 사용 후에 메모리에서 제거하기
                window.URL.revokeObjectURL(imgUri);
            }
            else{
                if( i === 0) {
                    totalContent += content[i].textContent === '' ? '\n' : content[i].textContent+'\n';
                }
                else {
                    totalContent += content[i].innerHTML === '<br>' ? '\n' : content[i].innerText+'\n';
                }
            }
        }
        api.registerPost(formdata, title, totalContent, tags)
        .then(res => {
            console.log('블로그 게시 성공');
            navigate("/minihome", {replace: true, state: {tabIndex: 1, targetEmail: targetEmail}});
        })
        .catch(e => {
            console.log('블로그 게시 실패');
            alert("작성 실패");
        });
    }

    return(
        <Box sx={{paddingTop: 3, paddingLeft: 2, paddingRight: 2, paddingBottom: 3, height: '90vh'}}>
            <Typography sx={{fontWeight: 'bold', marginBottom: 2 }}>제목</Typography>
            <Box sx={{flex: 1, display: 'flex', marginBottom: 3}}>
                {/* <TitleInput id="Title" type="text" name="title"/> */}
                <TextField 
                    id="Title"
                    sx={{ flex: 1, color: 'black' }}
                    size='small'
                />
            </Box>

            {/* <div class="editor-menu"> 
                <button id="btn-bold"> 
                    <b>B</b> 
                </button> 
                <button id="btn-italic"> 
                    <i>I</i> 
                </button> 
                <button id="btn-underline"> 
                    <u>U</u>
                </button> 
                <button id="btn-strike"> 
                    <s>S</s> 
                </button> 
                <button id="btn-ordered-list"> OL </button> 
                <button id="btn-unordered-list"> UL </button> 
                <button id="btn-image"> IMG </button> 
            </div> */}
            <Typography sx={{fontWeight: 'bold', marginBottom: 2 }}>내용</Typography>
            <WriteSpace id="Content" contentEditable="true"/>
            
            <Box sx={{marginBottom: 2}}>
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
            <Typography sx={{fontWeight: 'bold', marginBottom: 2 }}>태그</Typography>
            <Box sx={{flex: 1, display: 'flex', marginBottom: 2}}>
                <TextField 
                    id="Tags"
                    sx={{ flex: 1, color: 'black'}}
                    size='small'
                    placeholder='e.g. Java, Spring'
                />
            </Box>
            <Box sx={{flex: 1, justifyContent: 'flex-end', display: 'flex', marginBottom: 2}}>
                <Button onClick={()=>{savePost()}} variant={'contained'}>저장</Button>
            </Box>
            
        </Box>
    )
}

export default WriteBlog;