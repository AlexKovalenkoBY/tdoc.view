<template>
  <div class="login-page">
    <h2>Login</h2>
    <form @submit.prevent="handleLogin">
      <input 
        v-model="form.username" 
        placeholder="Username" 
        required
        autocomplete="username"
      />
      <input 
        v-model="form.password" 
        type="password" 
        placeholder="Password" 
        required
        autocomplete="current-password"
      />
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
  username: '',
  password: ''
})
const error = ref('')

const handleLogin = async () => {
  try {
    const response = await axios.post(
      '/api/login',
      new URLSearchParams(form),
      {
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded'
        }
      }
    )

    const token = response.data.token
    localStorage.setItem('token', token)

    // 👉 После успешного входа — получаем данные пользователя
    const userInfo = await axios.get('/api/me', {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })

    console.log('User:', userInfo.data)
    router.push('/')
  } catch (err) {
    error.value = 'Invalid credentials'
    console.error('Login error:', err)
  }
}
</script>

<style>
.error {
  color: red;
  margin-top: 10px;
}
</style>
