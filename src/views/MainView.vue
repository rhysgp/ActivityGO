<script setup lang="ts">

import { registerApp } from '@/net/auth_api_client'
import { listConversations } from '@/net/mastodon_api'
import { useAuth } from '@/stores/auth'

const authStore = useAuth();

const onCreateApplication = async () => {
  await authStore.loadOrRegisterApp();
};

const onAuthorise = async () => {
  authStore.authorise();
};

const onListInbox = async () => {
  await listConversations();
};

</script>

<template>
  <main>
    <h1>ActivityGo</h1>
    <div>
      <button  @click="onCreateApplication">Create application</button>
      <button @click="onAuthorise">Authorise</button>
      <button @click="onListInbox">List inbox</button>
    </div>
  </main>
</template>
