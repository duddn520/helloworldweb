import React from 'react';
import MDEditor from '@uiw/react-md-editor';
import { Box } from '@mui/material';

export default function CommentTextArea({content,setContent}){

    return(
            <Box>
                <MDEditor
                    value={content}
                    onChange={setContent}
                    height = {500}
                    style={{ margin: 10 }}
                />
                {/* <MDEditor.Markdown source={value} /> */}
            </Box>
    );
}
