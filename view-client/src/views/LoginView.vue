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
    console.log('Login success', response.data);
    router.push('/');
  } catch (error) {
    console.error('Login failed', error);
  }
};
</script>