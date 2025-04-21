<template>
  <div class="flex flex-col items-center justify-center min-h-screen ">
    <div class="w-full max-w-md bg-white p-8 rounded-xl shadow-lg">
      <h2 class="text-3xl font-bold text-center mb-6">Login</h2>
      <form @submit.prevent="login" class="space-y-4">
        <div>
          <label for="username" class="block text-gray-700 font-medium mb-1">Username</label>
          <input
              v-model="username"
              type="text"
              id="username"
              class="input input-bordered w-full"
              placeholder="Enter your username"
          />
        </div>
        <div>
          <label for="password" class="block text-gray-700 font-medium mb-1">Password</label>
          <input
              v-model="password"
              type="password"
              id="password"
              class="input input-bordered w-full"
              placeholder="Enter your password"
          />
        </div>
        <button
            type="submit"
            class="btn btn-primary w-full mt-4"
        >
          Login
        </button>
      </form>
      <div v-if="error" class="text-red-500 text-center mt-4">
        {{ error }}
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'LoginComponent',
  data() {
    return {
      username: '',
      password: '',
      error: null,
    };
  },
  methods: {
    async login() {
      try {
        const { data } = await axios.post(
            'http://localhost:8080/api/login',
            { username: this.username, password: this.password },
            { headers: { 'Content-Type': 'application/json' } }
        );

        const token = data.token;
        localStorage.setItem('token', token);
        localStorage.setItem('isLoggedIn', 'true');
        localStorage.setItem('username', this.username);

        this.$router.push('/songs');
      } catch (err) {
        console.error('Login failed:', err);
        this.error = 'Login failed. Please check your credentials.';
        localStorage.removeItem('isLoggedIn');
        localStorage.removeItem('username');
        localStorage.removeItem('token');
      }
    }
  }
};
</script>

<style scoped>
</style>
