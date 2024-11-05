<template>
  <div class="alles flex flex-col items-center">
    <p class="text-center text-6xl mb-12 mt-12 font-bold">Song List</p>
    <!-- <img class="w-1/6" src="https://cdn.pixabay.com/photo/2023/02/24/07/40/spiderman-7810368_1280.png" alt="spiderman"/> -->
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
          </td>
        </tr>
        </tbody>
      </table>
    </div>
    <div>
      <button class="btn btn-outline mt-6" @click="toggleCreateForm">Add Song</button>
      <AddSong v-if="createFormVisible" :visible="createFormVisible" @close="createFormVisible = false" @song-added="addNewSong" />
      <EditSong v-if="editFormVisible" :visible="editFormVisible" :song="currentSong" @close="editFormVisible = false" @song-updated="updateSongInList" />
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
      createFormVisible: false,
      editFormVisible: false,
      currentSong: null,
    };
  },
  methods: {
    getSongs() {
      SongService.getSongs()
          .then((response) => {
            this.songs = response.data;
          })
          .catch((error) => {
            console.error("Error fetching songs:", error);
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
      const index = this.songs.findIndex(song => song.id === updatedSong.id);
      if (index !== -1) {
        this.songs.splice(index, 1, updatedSong);
      }
    },
    deleteSong(id) {
        SongService.deleteSong(id)
            .then(() => {
              this.songs = this.songs.filter(song => song.id !== id);
            })
            .catch((error) => {
              console.error("Error deleting song:", error);
              alert("Failed to delete the song.");
            });
      }
  },
  created() {
    this.getSongs();
  },
};
</script>

<style scoped>
</style>
