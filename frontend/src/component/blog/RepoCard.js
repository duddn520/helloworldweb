import React, {useEffect} from "react";
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';

export default function RepoCard({title,content,fullName,createdAt,htmlUrl}) {

    const [description,setDescription] = React.useState([])
    const [createdDate,setCreatedDate] = React.useState([])

    useEffect(()=>{
        if(content== null)
        {
            setDescription(" ")
        }
        else{
            setDescription(content)
        }
        setCreatedDate(createdAt.substr(0,10))

    },[]);
    return(
    <Card
        sx={{ m : 1
    }}>
        <CardContent>
            <Typography sx={{fontSize: 14}} color="text.secondary" gutterBottom>
                {`${fullName}`}
            </Typography>
            <Typography variant="h5" component="div">
                {`${title}`}
            </Typography>
            <Typography sx={{mb: 1.5}} color="text.secondary">
                {createdDate}
            </Typography>
            <Typography variant="body2">
                {/*내용  들어갈 곳*/}
                {description}
            </Typography>
        </CardContent>
        <CardActions>
            <Button size="small" onClick={()=>window.open(htmlUrl,'_blank')}>Github에서 보기</Button>
        </CardActions>
    </Card>)

}