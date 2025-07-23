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
    });
    router.push('/'); // Перенаправление после успешного входа
  } catch (err) {
    error.value = 'Ошибка входа';
  }
};
</script>