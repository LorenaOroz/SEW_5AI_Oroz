<template>
  <div class="alles flex flex-col items-center">
    <p class="text-center text-6xl mb-12 mt-12 font-bold">Song List</p>

    <label class="input input-bordered flex items-center gap-2 mb-12">
      <input v-model="search" type="text" class="grow" placeholder="Search for title or artist" />
      <svg
          xmlns="http://www.w3.org/2000/svg"
          viewBox="0 0 16 16"
          fill="currentColor"
          class="h-4 w-4 opacity-70">
        <path
            fill-rule="evenodd"
            d="M9.965 11.026a5 5 0 1 1 1.06-1.06l2.755 2.754a.75.75 0 1 1-1.06 1.06l-2.755-2.754ZM10.5 7a3.5 3.5 0 1 1-7 0 3.5 3.5 0 0 1 7 0Z"
            clip-rule="evenodd" />
      </svg>
    </label>

    <div v-if="songs.length === 0 && search" class="text-center text-red-500 mb-12">
      No songs can be found. Please adjust your search.
    </div>

    <div class="container overflow-x-auto m-auto w-1/3">
      <table class="table table-zebra">
        <thead>
        <tr>
          <th>Song Id</th>
          <th>Title</th>
          <th>Artist</th>
          <th>Genre</th>
          <th>Length</th>
          <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="song in songs" :key="song.id">
          <td>{{ song.id }}</td>
          <td>{{ song.title }}</td>
          <td>{{ song.artist }}</td>
          <td>{{ song.genre }}</td>
          <td>{{ song.length }}</td>
          <td class="">
            <button class="btn btn-outline mr-6 mb-4" @click="editSong(song)">Edit</button>
            <button class="btn btn-outline btn-error" @click="deleteSong(song.id)">Delete</button>
            <button class="btn btn-primary" @click="playSong(song)">Play</button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>

    <div>
      <button class="btn btn-outline mt-6 mb-8" @click="toggleCreateForm">Add Song</button>
      <AddSong v-if="createFormVisible" :visible="createFormVisible" @close="createFormVisible = false"
               @song-added="addNewSong"/>
      <EditSong v-if="editFormVisible" :visible="editFormVisible" :song="currentSong" @close="editFormVisible = false"
                @song-updated="updateSongInList"/>
    </div>


    <div class="mt-6 mb-32" v-if="currentPlayingSong">
      <p class="text-center text-lg mb-4">jetzt spieeeelt: {{ currentPlayingSong.title }} von {{ currentPlayingSong.artist }}</p>
      <audio controls :src="currentPlayingSong.fileData" class="w-full">
        Your browser does not support the audio element.
      </audio>
    </div>

    <div class="mb-6 flex items-center justify-between w-1/3">
      <button class="btn" :disabled="currentPage === 0" @click="goToFirstPage">First Page</button>
      <button class="btn" :disabled="currentPage === 0" @click="goToPreviousPage">Previous</button>
      <p class="text-lg">Page {{ currentPage + 1 }} of {{ totalPages }}</p>
      <button class="btn" :disabled="currentPage === totalPages - 1" @click="goToNextPage">Next</button>
      <button class="btn" :disabled="currentPage === totalPages - 1" @click="goToLastPage">Last Page</button>
    </div>


  </div>
</template>

<script>
import SongService from "@/services/SongService.js";
import AddSong from "@/components/AddSong.vue";
import EditSong from "@/components/EditSong.vue";

export default {
  name: "Songs",
  components: {
    AddSong,
    EditSong,
  },
  data() {
    return {
      songs: [],
      search: "",
      createFormVisible: false,
      editFormVisible: false,
      currentSong: null,
      currentPlayingSong: null,
      currentPage: 0,
      totalPages: 0
    };
  },
  methods: {

    fetchSongs(page) {
      SongService.getPaginatedSongs(page)
          .then((response) => {
            this.songs = response.data.content;
            this.currentPage = response.data.number;
            this.totalPages = response.data.totalPages;
          })
          .catch((error) => {
            console.error("Error fetching paginated songs:", error);
          });
    },
    goToFirstPage() {
      this.fetchSongs(0);
    },
    goToPreviousPage() {
      if (this.currentPage > 0) {
        this.fetchSongs(this.currentPage - 1);
      }
    },
    goToNextPage() {
      if (this.currentPage < this.totalPages - 1) {
        this.fetchSongs(this.currentPage + 1);
      }
    },
    goToLastPage() {
      this.fetchSongs(this.totalPages - 1);
    },
  },
  created() {
    this.fetchSongs(0);
  },

  /*
    getSongs() {
      SongService.getSongs()
          .then((response) => {
            this.songs = response.data;
          })
          .catch((error) => {
            console.error("Error fetching songs:", error);
          });
    },*/
    searchSongs() {
      SongService.searchSongs(this.search)
          .then((response) => {
            this.songs = response.data;
          })
          .catch((error) => {
            console.error("Error searching songs:", error);
          });
    },
    toggleCreateForm() {
      this.createFormVisible = !this.createFormVisible;
    },
    editSong(song) {
      this.currentSong = { ...song };
      this.editFormVisible = true;
    },
    addNewSong(newSong) {
      this.songs.push(newSong);
    },
    updateSongInList(updatedSong) {
      const index = this.songs.findIndex((song) => song.id === updatedSong.id);
      if (index !== -1) {
        this.songs.splice(index, 1, updatedSong);
      }
    },
    deleteSong(id) {
      SongService.deleteSong(id)
          .then(() => {
            this.songs = this.songs.filter((song) => song.id !== id);
          })
          .catch((error) => {
            console.error("Error deleting song:", error);
            alert("Failed to delete the song.");
          });
    },
    playSong(song) {
      SongService.getSongFile(song.id)
          .then((response) => {
            this.currentPlayingSong = {
              ...song,
              fileData: response.data,
            };
          })
          .catch((error) => {
            console.error("Error fetching song file:", error);
            alert("Failed to load the song file.");
          });
    },

  watch: {
    search(newSearch) {
      if (newSearch) {
        this.searchSongs();
      } else {
        this.getSongs();
      }
    },
  },
};
</script>

<style scoped>
</style>
