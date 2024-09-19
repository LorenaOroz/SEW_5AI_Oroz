import axios from "axios";

const SONG_API_BASE = 'http://localhost:8080/api/songs'

class SongService{
    getSongs(){
        return axios.get(SONG_API_BASE);
    }
}

export default new SongService();