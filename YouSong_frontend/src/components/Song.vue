<template>
  <div class="alles flex flex-col items-center">
  <p class="text-center text-6xl mb-16 mt-3 font-bold">Song List</p>
  <div class = "container overflow-x-auto  m-auto w-1/3">
    <table class="table table-zebra">
      <thead>
      <tr>
        <th>Song Id</th>
        <th>Title</th>
        <th>Artist</th>
        <th>Genre</th>
        <th>Length</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for = "song in songs" v-bind:key = "song.id">
        <td> {{song.id}} </td>
        <td> {{song.title}} </td>
        <td> {{song.artist}} </td>
        <td> {{song.genre}} </td>
        <td> {{song.length}} </td>
      </tr>
      </tbody>
    </table>
  </div>

    <div>
      <button class="btn btn-outline m-10">Add Song</button>
    </div>
  </div>
</template>

<script>
import SongService from "@/services/SongService.js";
export default {
  name: 'Songs',
  data() {
    return {
      songs: [],
    };
  },
  methods: {
    getSongs() {
      SongService.getSongs().then((response) => {
        this.songs = response.data;
      }).catch((error) => {
        console.error("Error fetching songs:", error);
      });
    },
  },
  created() {
    this.getSongs();
  },
};
</script>
<style scoped>

</style>