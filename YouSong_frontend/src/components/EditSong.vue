<template>
  <div v-if="visible" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
    <div class="bg-primary p-8 rounded-lg shadow-lg w-full max-w-md relative">
      <button class="absolute top-2 right-2 text-gray-600 hover:text-gray-900" @click="closeForm">✖</button>
      <h2 class="text-2xl font-bold mb-6 text-center">Edit Song</h2>
      <form @submit.prevent="submitForm">
        <div class="mb-4">
          <label class="block text-sm font-medium text-black">Song Title</label>
          <input v-model="editedSong.title" @blur="v$.title.$touch()"
                 class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm"
                 placeholder="Enter song title">
          <span v-if="v$.title.$error" class="text-red-600 text-sm">Title required</span>
        </div>

        <div class="mb-4">
          <label class="block text-sm font-medium text-black">Artist</label>
          <input v-model="editedSong.artist" @blur="v$.artist.$touch()"
                 class="mt-1 block w-full px-3 py-2 border rounded-md"
                 placeholder="Enter artist name">
          <span v-if="v$.artist.$error" class="text-red-600 text-sm">Artist required</span>
        </div>

        <div class="mb-4">
          <label class="block text-sm font-medium text-black">Genre</label>
          <input v-model="editedSong.genre" @blur="v$.genre.$touch()"
                 class="mt-1 block w-full px-3 py-2 border rounded-md"
                 placeholder="Pop, Country, etc.">
          <span v-if="v$.genre.$error" class="text-red-600 text-sm">Genre required</span>
        </div>

        <div class="mb-4">
          <label class="block text-sm font-medium text-black">Length (minutes)</label>
          <input v-model="song.length" @blur="v$.length.$touch()"
                 class="mt-1 block w-full px-3 py-2 border rounded-md"
                 placeholder="Length in minutes">
          <span v-if="v$.length.$error" class="text-red-600 text-sm">
            <span v-if="!v$.length.required">Length required.</span>
            <span v-else-if="!v$.length.numeric">Must be numeric.</span>
            <span v-else-if="!v$.length.minValue">Must be greater than 0.</span>
          </span>
        </div>

        <div class="mb-4">
          <label class="block text-sm font-medium text-black">Upload Song File</label>
          <input type="file" @change="handleFileUpload"
                 class="mt-1 block w-full px-3 py-2 border rounded-md shadow-sm">
          <div v-if="fileName" class="text-sm text-gray-600 mt-2">
            Selected File: <strong>{{ fileName }}</strong>
          </div>
        </div>

        <div v-if="errorMessages && Object.keys(errorMessages).length" class="mb-4">
          <span v-for="(msg, field) in errorMessages" :key="field" class="text-red-600 block">
            {{ field }}: {{ msg }}
          </span>
        </div>

        <button type="submit" class="btn">Save</button>
      </form>
    </div>
  </div>
</template>

<script>
import {reactive, ref, watch} from 'vue';
import useVuelidate from '@vuelidate/core';
import {required, numeric, minValue} from '@vuelidate/validators';
import SongService from '@/services/SongService.js';

export default {
  props: {
    visible: Boolean,
    song: {type: Object, required: true}
  },
  emits: ['close', 'song-updated'],

  setup(props, {emit}) {
    const editedSong = reactive({...props.song});
    const fileName = ref('');
    const errorMessages = ref({});

    watch(() => props.song, (newSong) => {
      Object.assign(editedSong, newSong);
    });

    const rules = {
      title: {required},
      artist: {required},
      genre: {required},
      length: {required, numeric, minValue: minValue(0.01)}
    };

    const v$ = useVuelidate(rules, editedSong);

    const handleFileUpload = (event) => {
      const file = event.target.files[0];
      fileName.value = file.name;
      const reader = new FileReader();
      reader.onload = e => editedSong.fileData = e.target.result;
      reader.readAsDataURL(file);
    };

    const submitForm = async () => {
      errorMessages.value = {};
      v$.value.$touch();
      if (v$.value.$invalid) return;

      try {
        const response = await SongService.updateSong(editedSong.id, editedSong);
        emit('song-updated', response.data);
        closeForm();
      } catch (error) {
        errorMessages.value = error.response?.data || {general: 'Failed to update song.'};
      }
    };

    const closeForm = () => emit('close');

    return {
      editedSong,
      fileName,
      errorMessages,
      handleFileUpload,
      submitForm,
      closeForm,
      v$: useVuelidate(rules, editedSong)
    };
  }
};
</script>