<script setup lang="ts">
  import { onMounted } from 'vue'
  import { useRoute } from 'vue-router'
  import { useAuth } from '@/stores/auth'

  const route = useRoute();

  onMounted(() => {
    const code = route.query.code as string;
    useAuth().setAuthorizationCode(code);
  });
</script>
<template>
  <div>
    URI callback...
  </div>
</template>
<style scoped>
</style>
