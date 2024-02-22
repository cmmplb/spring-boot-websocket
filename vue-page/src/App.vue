<template>
  <div id="app">
    <Chat/>
  </div>
</template>

<script>

import Chat from '@/layout/index.vue';

export default {
  name: 'App',
  components: {
    Chat: Chat,
  },
};
</script>