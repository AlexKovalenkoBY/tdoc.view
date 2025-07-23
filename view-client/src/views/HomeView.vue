<template>
   <div class="home">
     <h2>Добро пожаловать!</h2>
     <div v-if="user.username">
       <p><strong>Имя пользователя:</strong> {{ user.username }}</p>
       <p><strong>Полное имя:</strong> {{ user.fullName || '—' }}</p>
       <p><strong>Роль:</strong> {{ formattedRoles }}</p>
       <button @click="logout">Выйти</button>
     </div>
     <div v-else>
       <p>Данные пользователя не найдены.</p>
       <button @click="logout">Выйти</button>
     </div>
   </div>
 </template>
 
 <script setup>
 import { ref, computed } from 'vue'
 import { useRouter } from 'vue-router'
 
 const user = ref({})
 const router = useRouter()
 
 try {
   const stored = localStorage.getItem('userInfo')
   if (stored) {
     user.value = JSON.parse(stored)
   }
 } catch (err) {
   console.error('Ошибка загрузки userInfo:', err)
 }
 
 const formattedRoles = computed(() => {
   const roles = user.value.roles
   if (!roles) return 'не указана'
   if (Array.isArray(roles)) return roles.join(', ')
   return roles.toString()
 })
 
 const logout = () => {
   localStorage.removeItem('token')
   localStorage.removeItem('userInfo')
   router.push('/login')
 }
 </script>
 
 <style>
 .home {
   padding: 20px;
 }
 button {
   margin-top: 20px;
   padding: 8px 16px;
   background-color: #e74c3c;
   color: #fff;
   border: none;
   cursor: pointer;
 }
 </style>
 