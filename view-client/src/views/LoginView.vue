<template>
  <div class="login-page">
    <h2>Login</h2>
    <form @submit.prevent="handleLogin">
      <input 
        v-model="form.username" 
        placeholder="Username" 
        required
        autocomplete="username"
      >
      <input 
        v-model="form.password" 
        type="password" 
        placeholder="Password" 
        required
        autocomplete="current-password"
      >
      <button type="submit">Login</button>
      <p v-if="error" class="error">{{ error }}</p>
    </form>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const form = reactive({
  username: 'admin',
  password: 'admin'
})
const error = ref('')

const handleLogin = async () => {
  try {
    const response = await axios.post(
      '/api/login',
      new URLSearchParams({
        username: form.username,
        password: form.password
      }),
      {
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded'
        },
        withCredentials: true
      }
    );
    
    if (response.status === 200) {
      router.push('/');
    }
  } catch (err) {
    error.value = 'Invalid credentials';
    console.error('Login error:', err);
  }
}
</script>

<style>
.error { color: red; margin-top: 10px; }
</style>