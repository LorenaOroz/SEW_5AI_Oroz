<template>
  <div class="music-player">
    <audio ref="audioPlayer" controls :src="audioSrc"></audio>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "MusicPlayer",
  props: {
    songId: {
      type: Number,
      required: true,
    },
  },
  data() {
    return {
      audioSrc: "",
    };
  },
  methods: {
    fetchSongFile() {
      axios
          .get(`http://localhost:8080/api/songs/${this.songId}/play`)
          .then((response) => {
            this.audioSrc = response.data;
          })
          .catch((error) => {
            console.error("Error fetching song file:", error);
          });
    },
  },
  mounted() {
    this.fetchSongFile();
  },
};
</script>

<style scoped>
.music-player {
  margin: 1rem 0;
}
</style>
