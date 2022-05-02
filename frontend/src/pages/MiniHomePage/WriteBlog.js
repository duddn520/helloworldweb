import React from "react";
import { Box, Button } from "@mui/material";
import { styled } from '@mui/system';
import api from "../../api/api";
import { useNavigate } from 'react-router';
import axios from "axios";

const TitleInput = styled('input')({
    flex: 1,
    padding: 8,
    borderRadius: 4,
});
const Content = styled('div')({
})


function WriteBlog(){
    const navigate = useNavigate();
    const [TitleValue, setTitleValue] = React.useState(""); 
    const onTitleChange = (event) => { 
        setTitleValue(event.currentTarget.value); 
    };

    function strToHTML(str){
        const parser = new DOMParser();
        const doc = parser.parseFromString(str, 'text/html')
        const element = doc.body.childNodes;
        return element;
    }

    const WriteSpace = () => {
        return(
            <Content id="Container" contentEditable="true" sx={{border: 2, borderColor: 'gray', borderRadius: 1, padding: 1, height: '75vh', overflow: 'auto'}}></Content>
        );
    }

    function loadImage(e){
        const selectedImages = e.target.files; //선택한 이미지 리스트

        for (let i = 0; i < selectedImages.length; i++){
            //blob 생성
            let blob = new Blob([selectedImages[i]], { type: "image/png" });
            console.log(blob);
            //url 생성
            let url = window.URL.createObjectURL(blob);

            //image로 img HTML element를 만들어줌 
            let imgNode = document.createElement('img');
            imgNode.setAttribute("src", url);
            imgNode.setAttribute("width", 300);
            imgNode.setAttribute("height", 300);
            imgNode.setAttribute("variant", "contained");
            imgNode.setAttribute("alt", "test image");

            document.getElementById('Container').appendChild(imgNode);
            window.URL.revokeObjectURL(url);
        }


        // url 사용 후에 메모리에서 제거하기
        //window.URL.revokeObjectURL(url);
    }

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
            document.getElementById('Container').appendChild(newDiv);

        }).catch(e =>{
            console.log(e)
        })
        

    }


    function savePost(){
        const divC = document.getElementById('Container');
        let content = strToHTML(divC.innerHTML);

        let formdata = new FormData();
        let totalContent = '';
        for(let i = 0; i < content.length; i++){
            if(content[i].tagName === 'IMG'){
                let imgUrl = content[i].currentSrc;
                let name = imgUrl.split('/')[3];
                let type = "multipart/form-data";
                let imgUri = imgUrl;
                formdata.append("files", { name: name , type: type, uri: imgUri });

                totalContent += "&&&&&\n";
            }
            else{
                if( i === 0) {
                    totalContent += content[i].textContent+'\n';
                }
                else {
                    totalContent += content[i].innerText+'\n';
                }
            }
        }

        // console.log(formdata);
        // console.log(totalContent);

        api.registerPost(formdata, TitleValue, totalContent)
        .then(res => {
            console.log('블로그 게시 성공');
            navigate("/minihome", {replace: true, state: {tabIndex: 1}});
        })
        .catch(e => {
            console.log('블로그 게시 실패');
        });
    }

    return(
        <Box sx={{paddingTop: 3, paddingLeft: 2, paddingRight: 3, paddingBottom: 5, height: '100vh'}}>
            <Box sx={{display: 'flex', width: '100%', justifyContent: 'space-between', alignItems: 'center', marginBottom: 5}}>
                <label>제목: </label> 
                <TitleInput onChange={onTitleChange} value={TitleValue} type="text" name="title"/>
            </Box>
            <WriteSpace/>
            <Box sx={{display: 'flex', justifyContent: 'space-between', marginTop: 2}}>
                <input 
                    type="file"
                    id="avatar" name="avatar"
                    accept="image/png, image/jpeg, image/jpg"
                    multiple={true}
                    onChange={loadImage}/>
                    <Button variant="contained" component="label" color="primary"> OCR
                    <input 
                    type="file"
                    accept="image/png, image/jpeg, image/jpg"
                    multiple={false}
                    onChange={InvokeKakaoOcrApi} hidden/>
                    </Button>
                <Button onClick={()=>{savePost()}} variant={'contained'}>저장</Button>
            </Box>
            
        </Box>
    )
}

export default WriteBlog;