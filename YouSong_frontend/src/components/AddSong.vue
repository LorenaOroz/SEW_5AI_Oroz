<template>
  <div v-if="visible" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
    <div class="bg-primary p-8 rounded-lg shadow-lg w-full max-w-md relative">
      <button class="absolute top-2 right-2 text-gray-600 hover:text-gray-900" @click="closeForm">✖</button>
      <h2 class="text-2xl font-bold mb-6 text-center">Add Song</h2>
      <form @submit.prevent="submitForm">
        <div class="mb-4">
          <label class="block text-sm font-medium text-black">Song Title</label>
          <input v-model="song.title" @blur="v$.title.$touch()"
                 class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm"
                 placeholder="Enter song title">
          <span v-if="v$.title.$error" class="text-red-600 text-sm">Title required</span>
        </div>

        <div class="mb-4">
          <label class="block text-sm font-medium text-black">Artist</label>
          <input v-model="song.artist" @blur="v$.artist.$touch()"
                 class="mt-1 block w-full px-3 py-2 border rounded-md"
                 placeholder="Enter artist name">
          <span v-if="v$.artist.$error" class="text-red-600 text-sm">Artist required</span>
        </div>

        <div class="mb-4">
          <label class="block text-sm font-medium text-black">Genre</label>
          <input v-model="song.genre" @blur="v$.genre.$touch()"
                 class="mt-1 block w-full px-3 py-2 border rounded-md"
                 placeholder="Pop, Country, etc.">
          <span v-if="v$.genre.$error" class="text-red-600 text-sm">Genre required</span>
        </div>

        <div class="mb-4">
          <label class="block text-sm font-medium text-black">Length (minutes)</label>
          <input v-model="song.length" @blur="v$.length.$touch()"
                 class="mt-1 block w-full px-3 py-2 border rounded-md"
                 placeholder="Length in minutes">
          <span v-if="v$.length.$error" class="text-red-600 text-sm">Length required.</span>
        </div>

        <div class="mb-4">
          <label class="block text-sm font-medium text-black">Upload File</label>
          <input type="file" @change="handleFileUpload"
                 class="mt-1 block w-full px-3 py-2 border rounded-md shadow-sm"/>
        </div>

        <!-- Error messages block -->
        <div v-if="errorMessages && Object.keys(errorMessages).length" class="mb-4">
          <span v-for="(msg, field) in errorMessages" :key="field" class="text-red-600 block">{{ field }}: {{
              msg
            }}</span>
        </div>

        <button type="submit" class="btn">Add</button>
      </form>
    </div>
  </div>
</template>

<script>
import {reactive, ref} from 'vue';
import useVuelidate from '@vuelidate/core';
import {required, numeric, minValue} from '@vuelidate/validators';
import SongService from '@/services/SongService.js';

export default {
  props: {visible: Boolean},
  emits: ['close', 'song-added'],

  setup(_, {emit}) {
    const song = reactive({
      title: '',
      artist: '',
      genre: '',
      length: null,
      fileData: ''
    });

    const errorMessages = ref({});

    const rules = {
      title: {required},
      artist: {required},
      genre: {required},
      length: {required, numeric}
    };

    const v$ = useVuelidate(rules, song);

    const handleFileUpload = (event) => {
      const file = event.target.files[0];
      const reader = new FileReader();
      reader.onload = e => song.fileData = e.target.result;
      reader.readAsDataURL(file);
    };

    const submitForm = async () => {
      errorMessages.value = {};
      v$.value.$touch();

      if (v$.value.$invalid) {
        return;
      }

      try {
        const response = await SongService.createSong(song);
        emit('song-added', response.data);
        closeForm();
      } catch (error) {
        errorMessages.value = error.response?.data || {general: 'Failed to add song.'};
      }
    };

    const closeForm = () => emit('close');

    return {song, errorMessages, handleFileUpload, submitForm, closeForm, v$};
  }
};
</script>