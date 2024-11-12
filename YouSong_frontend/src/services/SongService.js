import axios from "axios";

const SONG_API_BASE = 'http://localhost:8080/api/songs';

class SongService {
    getSongs() {
        return axios.get(SONG_API_BASE);
    }

    createSong(song) {
        return axios.post(SONG_API_BASE, song);
    }

    updateSong(id, song) {
        return axios.put(`${SONG_API_BASE}/${id}`, song);
    }

    deleteSong(id) {
        return axios.delete(`${SONG_API_BASE}/${id}`);
    }

    searchSongs(query) {
        return axios.get(`${SONG_API_BASE}/search`, {
            params: { query }
        });
    }
}

export default new SongService();
