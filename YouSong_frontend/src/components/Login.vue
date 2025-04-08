<template>
  <div class="login">
    <h2>Login</h2>
    <form @submit.prevent="login">
      <div class="form-group">
        <label for="username">Username:</label>
        <input v-model="username" type="text" id="username" />
      </div>
      <div class="form-group">
        <label for="password">Password:</label>
        <input v-model="password" type="password" id="password" />
      </div>
      <button type="submit">Login</button>
    </form>
    <div v-if="error" class="error">{{ error }}</div>
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

        const response = await axios.post('http://localhost:8080/api/login', {
          username: this.username,
          password: this.password
        }, {
          headers: {
            'Content-Type': 'application/json'
          },
        });

        console.log("Login successful", response.data);
        this.error = null;

        localStorage.setItem('isLoggedIn', 'true');
        localStorage.setItem('username', this.username);

        this.$router.push('/songs');
      } catch (error) {
        console.error("Login failed:", error);
        this.error = 'Login failed. Please check your credentials.';

        localStorage.removeItem('isLoggedIn');
        localStorage.removeItem('username');
      }
    }
  }
};
</script>

<style scoped>
.form-group {
  margin-bottom: 1rem;
}

label {
  display: block;
  margin-bottom: 0.5rem;
}

input {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 4px;
}

button {
  background-color: #4CAF50;
  color: white;
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #45a049;
}

.error {
  color: red;
  margin-top: 1rem;
}
</style>