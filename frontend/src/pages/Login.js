import React from "react";
import api from "../api/api";

export default function (){

    const [data,setData] = React.useState([]);

    React.useEffect(() => {
        setData( api.getGuestBooks() ) ;
    },[]);


    return(
        <div>
            <h1>Login</h1>
            {
                data.map( item => {
                    return(
                      <Card>

                      </Card>
                    );
                })
            }
        </div>
    )   ;
}