import React from "react";
import { Box ,Container ,styled ,Avatar ,Button ,Typography ,Divider ,
    TextField
} from "@mui/material";
import api from "../../api/api";

function GuestBooks({ userInfo }){
    const [newComment,setNewComment] = React.useState("하이하이👆");
    const [comments,setComments] = React.useState([]);
    const [refresh,setRefresh] = React.useState(false);
    React.useEffect(() => {

        api.getGuestBooks()
        .then( res => {
            
            setComments(res.reverse());
        })
        .catch( e => { })

    },[ refresh ])

    function registerGuestBook(){
        api.registerGuestBook({ targetUserEmail : userInfo.email ,content: newComment })
        .then( res => {
            setRefresh(!refresh)
        })
        .catch( e => { })
    }

    return(
        <Box>
            <Container sx={{ backgroundColor: 'rgb(240,240,240)' ,ml: 3 ,mr: 1 ,width: '95%' }}>
                <Container sx={{ display: 'flex' ,flexDirection: 'row' ,alignItems: 'center' }}> 
                    <Box 
                        sx={{ width: 150 ,height: 150 ,alignItems: 'center' ,justifyContent: 'center' ,display: 'flex' , mt: 2 ,mr: 2 ,backgroundColor: 'white' }}
                    >
                        <img src="https://picsum.photos/200/300?grayscale" width={150} height={150} />
                    </Box>
                    <TextArea 
                        sx={{ width: '100%' ,height: 150 ,borderColor: 'lightgray' , p: 2 ,mt: 2 }}
                        value={newComment}
                        onChange={ value => setNewComment( value.target.value )}
                    />
                </Container>
                {/* 방명록 작성요청 */}
                <div style={{ display: 'flex', justifyContent: 'flex-end'  }}>
                    <Button 
                        variant='outlined' sx={{ backgroundColor: 'white' ,mr: 3 ,mb: 1 ,mt: 2 ,color: 'black' ,borderColor: 'lightgray'  }}
                        onClick={registerGuestBook}
                    >
                        확인
                    </Button>
                </div>
            </Container>

            {/* 방명록 조회 */}
            <Container sx={{ ml: 3 ,mr: 1 ,width: '95%' }}>
            {
                comments.map( comment => {
                  return(
                    <RenderComment comment={comment} />
                  );
                })
            }
            </Container>
            

        </Box>
    )

    function RenderComment( {comment} ){
        const [reply,setReply] = React.useState("");
        return(
                <Container>
                    <Container sx={{ width: '100%' ,backgroundColor: 'rgb(240,240,240)' ,display: 'flex' ,flexDirection: 'row' ,alignItems: 'center' ,mt: 5 }}>
                        <Typography>No.{comment.id}</Typography> 
                        <Button sx={{ textTransform : 'none' }}>{comment.userName}</Button>
                        <Typography>( 2022.04.27 11:20 )</Typography>
    
                        <Button sx={{ marginLeft: 'auto' }}>비밀로 하기</Button>
                        <Divider orientation='vertical' flexItem sx={{ m: 1 }}/>
                        <Button sx={{ color: 'red' }}>삭제</Button>
                    </Container>
                    <Container sx={{ display: 'flex' ,flexDirection: 'row' ,alignItems: 'center' }}> 
                        <Box 
                            sx={{ width: 150 ,height: 150 ,alignItems: 'center' ,justifyContent: 'center' ,display: 'flex' , m: 2 ,backgroundColor: 'white' }}
                        >
                            <img src="https://picsum.photos/200/300?grayscale" width={150} height={150} />
                        </Box>
                        <Typography
                            sx={{ width: '100%' ,height: 150 ,borderColor: 'lightgray' , p: 2  }}
                        >
                            {comment.content}    
                        </Typography>    
                    </Container>
                    <Container sx={{ display: 'flex' ,flexDirection: 'row' ,alignItems: 'center' ,backgroundColor: 'rgb(240,240,240)', p: 2 }}>
                        <TextField 
                            value={reply}
                            onChange={value => setReply(value.target.value)}
                            sx={{ width: '100%' ,backgroundColor: 'white' }}
                        />
                        <Button 
                            variant='outlined' 
                            sx={{ backgroundColor: 'white' ,ml: 3 ,color: 'black' ,borderColor: 'lightgray'  }}
                            onClick={() => api.updateGuestBook({ id: comment.id , reply: reply})}
                        >
                            확인
                        </Button>
                    </Container>
                </Container>
        );
    }

}
export default GuestBooks;



const TextArea = styled('textarea')({
    
});