<template>
  <div v-if="visible" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
    <div class="bg-primary p-8 rounded-lg shadow-lg w-full max-w-md relative">
      <button class="absolute top-2 right-2 text-gray-600 hover:text-gray-900" @click="closeForm">✖</button>
      <h2 class="text-2xl font-bold mb-6 text-center">Edit Song</h2>
      <form @submit.prevent="submitForm">
        <div class="mb-4">
          <label for="title" class="block text-sm font-medium text-black">Song Title</label>
          <input type="text" id="title" v-model="song.title" class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm" placeholder="Enter song title">
        </div>
        <div class="mb-4">
          <label for="artist" class="block text-sm font-medium text-black">Artist</label>
          <input type="text" id="artist" v-model="song.artist" class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm" placeholder="Enter artist name">
        </div>
        <div class="mb-4">
          <label for="genre" class="block text-sm font-medium text-black">Genre</label>
          <input id="genre" v-model="song.genre" class="mt-1 block w-full px-3 py-2 border rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm" placeholder="Pop, Country, Deutschrap .....">
        </div>
        <div class="mb-4">
          <label for="length" class="block text-sm font-medium text-black">Length (in minutes)</label>
          <input type="text" id="length" v-model="song.length" class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm" placeholder="Length in double">
        </div>
        <div>
          <button type="submit" class="btn">Save</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import SongService from "@/services/SongService.js";

export default {
  name: "EditSong",
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    song: {
      type: Object,
      required: true,
    }
  },
  methods: {
    submitForm() {
      SongService.updateSong(this.song.id, this.song)
          .then(response => {
            this.$emit('song-updated', response.data);
            this.closeForm();
          })
          .catch(error => {
            console.error("Error updating song:", error);
          });
    },
    closeForm() {
      this.$emit('close');
    }
  }
};
</script>

<style scoped>
</style>
