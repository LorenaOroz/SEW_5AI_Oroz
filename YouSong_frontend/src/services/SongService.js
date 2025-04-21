import axios from "axios";

const SONG_API_BASE = 'http://localhost:8080/api/songs';

class SongService {
    getAuthHeaders() {
        const token = localStorage.getItem('token');
        return {
            headers: {
                'Content-Type': 'application/json',
                'X-API-KEY': token
            }
        };
    }

    getSongs() {
        return axios.get(SONG_API_BASE, this.getAuthHeaders());
    }

    createSong(song) {
        return axios.post(SONG_API_BASE, song, this.getAuthHeaders());
    }

    updateSong(id, song) {
        return axios.put(`${SONG_API_BASE}/${id}`, song, this.getAuthHeaders());
    }

    deleteSong(id) {
        return axios.delete(`${SONG_API_BASE}/${id}`, this.getAuthHeaders());
    }

    searchSongs(query) {
        return axios.get(`${SONG_API_BASE}/search`, {
            ...this.getAuthHeaders(),
            params: {query}
        });
    }

    getSongFile(songId) {
        return axios.get(`${SONG_API_BASE}/${songId}/play`, this.getAuthHeaders());
    }
}

export default new SongService();