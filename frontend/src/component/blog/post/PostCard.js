import React from "react";
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';

export default function PostCard({title,content}) {

    return(
    <Card
        sx={{ m : 2
    }}>
        <CardContent>
            <Typography sx={{fontSize: 14}} color="text.secondary" gutterBottom>
                {/*날짜 들어갈곳*/} hihihihihihih
            </Typography>
            <Typography variant="h5" component="div">
                {/*제목 들어갈 곳*/} {`${title}`}
            </Typography>
            <Typography sx={{mb: 1.5}} color="text.secondary">
                56789
            </Typography>
            <Typography variant="body2">
                {/*내용  들어갈 곳*/}
                {`${content}`}
                <br/>
                {'"a benevolent smile"'}
            </Typography>
        </CardContent>
        <CardActions>
            <Button size="small">Learn More</Button>
        </CardActions>
    </Card>)

}