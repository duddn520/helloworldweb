import React from 'react';
import { Box , Typography ,Button 
        ,Card ,TextField
} from '@mui/material';
import CommentTextArea from '../../component/questions/CommentTextArea';
import CustomAppBar from '../../component/appbar/CustomAppBar';
import { useNavigate } from 'react-router';
import api from '../../api/api';

export default function(){
    const navigate = useNavigate();
    const [tags,setTags] = React.useState("");
    const [buttonTags,setButtonTags] = React.useState([]);
    const [title,setTitle] = React.useState("");
    const [content,setContent] = React.useState("");

    // 입력된 태그들 입력창 앞에 정렬하여 보여줌.
    const renderTags = () => {
        return(
            <Box sx={{ flexDirection: 'row', display: 'flex' }}>
                {
                    buttonTags.map( tag => {
                        return(
                            <Button sx={{ whiteSpace: 'nowrap' ,textTransform: 'none' }}>{tag}</Button>
                        )
                    })
                }
            </Box>
        );
    }
    // 태그에 글자 입력될때마다 호출하여 확인.
    const changeTags = (value) => {

        // 새로운 태그가 입력되었을 때 입력창 앞에 추가
        if( value.endsWith(',') ){
            let newTag = value.split(',')[0];
            let tmp = buttonTags;
            tmp.push(newTag);
            setButtonTags(tmp);
            setTags("");
        } else {
            setTags(value);
        }

    }

    function handleSubmit(){
        let tmp = ""
        buttonTags.map( tag => {
            tmp += tag + ','
        },[]);
        console.log(content);
        api.registerQnA({ title: title ,content: content , type: "QNA" ,tags:  tmp }) 
        .then( res => {
            navigate('/');
        })
        .catch( e => {
            alert("다시 시도해주세요.");
        })
    }

    return(
        <Box sx={{ backgroundColor: 'rgb(240,240,240)'}}>
            
            <CustomAppBar />

            <Typography variant='h4' sx={{ p : 2 , pt: 5 , ml: 1 }}>
                질문 작성하기
            </Typography>

            <Box sx={{ display: 'flex' , flexDirection: 'row'}}>
                <Card sx={{ m: 2 ,flex: 3 }}>
                    <Typography sx={{ p: 2 ,fontWeight: 'bold' }}>
                        제목
                    </Typography>
                    <Typography variant='body2' sx={{ paddingLeft : 2 ,fontSize: 12 }}>
                        구체적으로 말하고 다른 사람에게 질문을 하고 있다고 상상해 보세요.                    
                    </Typography>
                    <TextField 
                        sx={{ width: '90%' , m: 2 }}
                        size='small'
                        value={title}
                        onChange={ value => setTitle(value.target.value) }
                        placeholder='e.g. 리액트 질문'
                    />

                    <Typography sx={{ p: 2 ,fontWeight: 'bold' }}>
                        내용
                    </Typography>
                    <Typography variant='body2' sx={{ paddingLeft : 2 ,fontSize: 12 }}>
                        누군가가 귀하의 질문에 대답하는 데 필요한 모든 정보를 포함하세요.
                    </Typography>

                    <CommentTextArea content={content} setContent={setContent}/>

                    <Typography sx={{ p: 2 ,fontWeight: 'bold' }}>
                        태그
                    </Typography>
                    <Typography variant='body2' sx={{ paddingLeft : 2 }}>
                        질문의 내용을 설명하는 최대 5개의 태그를 추가하세요.
                    </Typography>
                    <TextField 
                        sx={{ width: '90%' ,m:2 }}
                        value={tags}
                        onChange ={ (value) => { changeTags(value.target.value) } }
                        InputProps={{ startAdornment: renderTags() }}
                        size='small'
                        placeholder='e.g. React'
                    >
                    </TextField>

                    <Button 
                        variant='contained' 
                        sx={{ m: 2 }}
                        onClick={() => handleSubmit()}
                    >
                        작성하기
                    </Button>
                </Card>

            </Box>

        </Box>
    );
}
