import axios from 'axios';

const baseUrl = "http://localhost:8080"

//   GET - /api/guestbook
function getGuestBooks(){
    axios.get(baseUrl+'/api/guestbook')
        .then( res => {
            return res.data.data;
        })
        .catch( e => {
            console.log(e);
        })

}
function B(){

}

export default { getGuestBooks } ;