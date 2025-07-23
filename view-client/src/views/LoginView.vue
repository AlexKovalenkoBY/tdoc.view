<template>
   <div class="login-container">
     <h2>Login</h2>
     <form @submit.prevent="handleSubmit">
       <div class="form-group">
         <label for="username">Username:</label>
         <input
           type="text"
           id="username"
           v-model="username"
           required
         />
       </div>
       <div class="form-group">
         <label for="password">Password:</label>
         <input
           type="password"
           id="password"
           v-model="password"
           required
         />
       </div>
       <div v-if="error" class="error-message">{{ error }}</div>
       <button type="submit">Login</button>
     </form>
   </div>
 </template>
 
 <script setup>
 import { ref } from 'vue';
 import { useRouter } from 'vue-router';
 import axios from 'axios';
 
 const username = ref('');
 const password = ref('');
 const error = ref('');
 const router = useRouter();
 
 const handleSubmit = async () => {
   try {
     const response = await axios.post('/api/login', {
       username: username.value,
       password: password.value
     }, {
       headers: {
         'Content-Type': 'application/json'
       }
     });
     
     // Сохраняем токен (пример для JWT)
     localStorage.setItem('authToken', response.data.token);
     
     // Перенаправляем на главную страницу
     router.push('/');
   } catch (err) {
     error.value = 'Invalid username or password';
     console.error('Login error:', err);
   }
 };
 </script>
 
 <style scoped>
 .login-container {
   max-width: 400px;
   margin: 0 auto;
   padding: 20px;
   border: 1px solid #ddd;
   border-radius: 5px;
   margin-top: 50px;
 }
 
 .form-group {
   margin-bottom: 15px;
 }
 
 label {
   display: block;
   margin-bottom: 5px;
 }
 
 input {
   width: 100%;
   padding: 8px;
   box-sizing: border-box;
 }
 
 button {
   padding: 10px 15px;
   background-color: #42b983;
   color: white;
   border: none;
   border-radius: 4px;
   cursor: pointer;
 }
 
 .error-message {
   color: red;
   margin-bottom: 15px;
 }
 </style>