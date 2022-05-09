import React from "react";
import { Box, Button, Typography, TextField } from "@mui/material";
import api from "../../api/api";
import { useNavigate, useLocation } from 'react-router';
import styled from "styled-components";
import imageCompression from "browser-image-compression";
import axios from "axios";

const WriteSpace = styled.div`
    padding: 10px; 
    border: 1px solid lightgray;
    border-radius: 4px;
    height: 47vh;
    overflow: auto;
    margin-bottom: 10px;
`;

const Content = styled('div')({
});


function WriteBlog(){
    const navigate = useNavigate();
    const { state } = useLocation();
    const [targetEmail, setTargetEmail] = React.useState(state.targetEmail);

    function strToHTML(str){
        const parser = new DOMParser();
        const doc = parser.parseFromString(str, 'text/html')
        const element = doc.body.childNodes;
        return element;
    }

    function extractOnlyFilename(original){
        let stringArray = original.split(".")
        return original.replace("."+stringArray[ stringArray.length -1 ], "");
    }

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

    /**
     * 파일을 Base64 형식에서 > File 형식으로 (디코딩)변환합니다
     * @param { String } image Base64 형식의 String
     * @param { String } fileName Base64 에서는 파일 명을 저장할 수 없습니다.
     */
    function convertBase64IntoFile (image, fileName) {
        const mimeType = image?.match(/[^:]\w+\/[\w-+\d.]+(?=;|,)/)[0]   // image/jpeg
        const realData = image.split(',')[1]   // 이 경우에선 /9j/4AAQSkZJRgABAQAAAQABAAD...
    
        const blob = b64toBlob(realData, mimeType)
        const raw = new File([blob], fileName, { type: mimeType })
        
        return raw;
    }

    /**
     * base64 를 Blob 오브젝트로 만드는 함수
     * @param { String } b64Data
     * @param { String } contentType mimeType
     * @param { Number } sliceSize 쪼개는 사이즈
     */
    function b64toBlob (b64Data, contentType = '', sliceSize = 512) {
        if (b64Data === '' || b64Data === undefined) return
    
        const byteCharacters = atob(b64Data)
        const byteArrays = []
    
        for (let offset = 0; offset < byteCharacters.length; offset += sliceSize) {
        const slice = byteCharacters.slice(offset, offset + sliceSize)
        const byteNumbers = new Array(slice.length)
        for (let i = 0; i < slice.length; i++) {
            byteNumbers[i] = slice.charCodeAt(i)
        }
        const byteArray = new Uint8Array(byteNumbers)
        byteArrays.push(byteArray)
        }
    
        const blob = new Blob(byteArrays, { type: contentType })
        return blob
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