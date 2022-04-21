import React from 'react';
import { Box , Typography ,Button 
        ,Card ,TextField 
} from '@mui/material';
import { styled } from '@mui/material';
import CustomAppBar from '../component/appbar/CustomAppBar';

export default function(){
    const [tags,setTags] = React.useState("");
    const [buttonTags,setButtonTags] = React.useState([]);

    // 입력된 태그들 입력창 앞에 정렬하여 보여줌.
    const showTags = () => {
        return(
            <Box sx={{ flexDirection: 'row', display: 'flex' }}>
                {
                    buttonTags.map( tag => {
                        return(
                            <Button>{tag}</Button>
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

    return(
        <Box sx={{ backgroundColor: 'rgb(240,240,240)'}}>
            
            <CustomAppBar />

            <Typography variant='h4' sx={{ p : 2 , pt: 5 }}>
                질문 작성하기
            </Typography>

            <Card sx={{ m: 2 }}>
                <Typography sx={{ p: 2 ,fontWeight: 'bold' }}>
                    제목
                </Typography>
                <Typography variant='body2' sx={{ paddingLeft : 2 }}>
                    Be specific and imagine you’re asking a question to another person
                </Typography>
                <TextField 
                    sx={{ width: '90%' , m: 2 }}
                    size='small'
                    placeholder='e.g. 리액트 질문'
                />

                <Typography sx={{ p: 2 ,fontWeight: 'bold' }}>
                    내용
                </Typography>
                <Typography variant='body2' sx={{ paddingLeft : 2 }}>
                    Be specific and imagine you’re asking a question to another person
                </Typography>

                <TextArea
                    sx={{ width: '90%' ,m: 2 ,height: 300 , p : 2 ,borderColor: 'lightgray' }}
                    size='small'
                    placeholder='e.g. 리액트 질문'
                />

                <Typography sx={{ p: 2 ,fontWeight: 'bold' }}>
                    Tags
                </Typography>
                <Typography variant='body2' sx={{ paddingLeft : 2 }}>
                    Add up to 5 tags to describe what your question is about
                </Typography>
                <TextField 
                    sx={{ width: '90%' ,m:2 }}
                    value={tags}
                    onChange ={ (value) => { changeTags(value.target.value) } }
                    InputProps={{ startAdornment: showTags() }}
                    size='small'
                    placeholder='e.g. React'
                >
                </TextField>

                <Button variant='contained' sx={{ m: 2}}>
                    작성하기
                </Button>
            </Card>

        </Box>
    );
}

const TextArea = styled('textarea')({  

});
const StyledForm = styled('form')({
    flexGrow: 1 ,
});