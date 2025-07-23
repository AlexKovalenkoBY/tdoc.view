<template>
  <div class="login-page">
    <h2>Login</h2>
    <form @submit.prevent="handleLogin">
      <input v-model="form.username" placeholder="Username" required>
      <input v-model="form.password" type="password" placeholder="Password" required>
      <button type="submit">Login</button>
    </form>
  </div>
</template>

<script>
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

export default {
  setup() {
    const router = useRouter()
    const form = reactive({
      username: '',
      password: ''
    })

    const handleLogin = async () => {
      try {
        await axios.post('/api/login', form)
        router.push('/')
      } catch (error) {
        console.error('Login failed', error)
      }
    }

    return { form, handleLogin }
  }
}
</script>