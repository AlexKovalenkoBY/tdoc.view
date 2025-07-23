<template>
   <div class="admin-panel">
     <h2>Управление пользователями</h2>
     <p><strong>Вы вошли как:</strong> {{ user.fullName }} ({{ user.roles }})</p>
 
     <!-- 🔍 Поиск -->
     <input v-model="search" placeholder="Поиск по имени..." class="search-box" />
 
     <!-- 👤 Создание нового пользователя -->
     <form @submit.prevent="createUser" class="create-form">
       <input v-model="newUser.username" placeholder="Username" required />
       <input v-model="newUser.fullName" placeholder="Full Name" required />
       <input v-model="newUser.password" type="password" placeholder="Password" required />
 
       <div class="roles">
         <label><input type="checkbox" value="USER" v-model="newUser.rolesArr" /> USER</label>
         <label><input type="checkbox" value="ADMIN" v-model="newUser.rolesArr" /> ADMIN</label>
         <label><input type="checkbox" value="EDITOR" v-model="newUser.rolesArr" /> EDITOR</label>
       </div>
 
       <button type="submit">Создать</button>
     </form>
 
     <!-- 📊 Таблица пользователей -->
     <table class="user-table">
       <thead>
         <tr>
           <th @click="sortBy('username')">Логин</th>
           <th @click="sortBy('fullName')">Имя</th>
           <th>Роль</th>
           <th>Действия</th>
         </tr>
       </thead>
       <tbody>
         <tr v-for="userItem in filteredAndSortedUsers" :key="userItem.id">
           <td>{{ userItem.username }}</td>
           <td>{{ userItem.fullName }}</td>
           <td>{{ userItem.roles }}</td>
           <td>
             <button @click="startEdit(userItem)">✏️</button>
             <button @click="deleteUser(userItem.id)">🗑️</button>
           </td>
         </tr>
       </tbody>
     </table>
 
     <!-- ✨ Редактирование -->
     <div v-if="editingUser" class="edit-form">
       <h3>Редактировать пользователя</h3>
       <form @submit.prevent="updateUser">
         <input v-model="editingUser.username" placeholder="Username" required />
         <input v-model="editingUser.fullName" placeholder="Full Name" required />
         <input v-model="editingUser.password" type="password" placeholder="Новый пароль (опционально)" />
 
         <div class="roles">
           <label><input type="checkbox" value="USER" v-model="editingUser.rolesArr" /> USER</label>
           <label><input type="checkbox" value="ADMIN" v-model="editingUser.rolesArr" /> ADMIN</label>
           <label><input type="checkbox" value="EDITOR" v-model="editingUser.rolesArr" /> EDITOR</label>
         </div>
 
         <button type="submit">💾 Сохранить</button>
         <button type="button" @click="cancelEdit">❌ Отмена</button>
       </form>
     </div>
   </div>
 </template>
 
 <script setup>
 import { ref, computed, onMounted } from 'vue'
 import axios from 'axios'
 import { useRouter } from 'vue-router'
 
 const router = useRouter()
 const user = ref({})
 const users = ref([])
 const editingUser = ref(null)
 const search = ref('')
 const sortKey = ref('fullName')
 const sortAsc = ref(true)
 
 const newUser = ref({
   username: '',
   fullName: '',
   password: '',
   rolesArr: []
 })
 
 // 👉 Чтение текущего пользователя
 try {
   const stored = localStorage.getItem('userInfo')
   if (stored) {
     user.value = JSON.parse(stored)
   }
 } catch {
   router.push('/login')
 }
 
 const fetchUsers = async () => {
   try {
     const res = await axios.get('/api/users')
     users.value = res.data
   } catch (err) {
     console.error('Ошибка загрузки пользователей:', err)
   }
 }
 
 const createUser = async () => {
   try {
     const payload = {
       ...newUser.value,
       roles: newUser.value.rolesArr.join(',')
     }
     await axios.post('/api/users', payload)
     newUser.value = { username: '', fullName: '', password: '', rolesArr: [] }
     await fetchUsers()
   } catch (err) {
     alert('Ошибка создания')
   }
 }
 
 const deleteUser = async (id) => {
   if (confirm('Удалить пользователя?')) {
     try {
       await axios.delete(`/api/users/${id}`)
       await fetchUsers()
     } catch {
       alert('Ошибка удаления')
     }
   }
 }
 
 const startEdit = (userItem) => {
   const rolesArr = userItem.roles?.split(',') || []
   editingUser.value = { ...userItem, password: '', rolesArr }
 }
 
 const cancelEdit = () => {
   editingUser.value = null
 }
 
 const updateUser = async () => {
   try {
     const payload = {
       ...editingUser.value,
       roles: editingUser.value.rolesArr.join(',')
     }
     if (!payload.password) {
       delete payload.password
     }
     await axios.put(`/api/users/${editingUser.value.id}`, payload)
     editingUser.value = null
     await fetchUsers()
   } catch {
     alert('Ошибка обновления')
   }
 }
 
 const filteredAndSortedUsers = computed(() => {
   return [...users.value]
     .filter(u => u.fullName.toLowerCase().includes(search.value.toLowerCase()))
     .sort((a, b) => {
       const valA = a[sortKey.value]?.toLowerCase()
       const valB = b[sortKey.value]?.toLowerCase()
       if (valA < valB) return sortAsc.value ? -1 : 1
       if (valA > valB) return sortAsc.value ? 1 : -1
       return 0
     })
 })
 
 const sortBy = (key) => {
   if (sortKey.value === key) {
     sortAsc.value = !sortAsc.value
   } else {
     sortKey.value = key
     sortAsc.value = true
   }
 }
 
 onMounted(fetchUsers)
 </script>
 
 <style>
 .admin-panel {
   padding: 20px;
   max-width: 900px;
   margin: auto;
 }
 
 .search-box {
   padding: 8px;
   margin-bottom: 20px;
   width: 100%;
   max-width: 300px;
 }
 
 .create-form, .edit-form {
   margin-top: 20px;
   padding: 10px;
   border: 1px solid #ccc;
 }
 
 .roles {
   margin: 10px 0;
 }
 
 button {
   margin: 5px;
   padding: 6px 12px;
   cursor: pointer;
 }
 
 .user-table {
   width: 100%;
   border-collapse: collapse;
   margin-top: 20px;
 }
 
 .user-table th, .user-table td {
   border: 1px solid #ddd;
   padding: 8px;
 }
 
 .user-table th {
   cursor: pointer;
   background-color: #f0f0f0;
 }
 </style>
 