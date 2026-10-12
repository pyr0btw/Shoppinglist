<script setup>
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <header class="kopfzeile">
    <RouterLink to="/" class="logo">🛒 Einkaufsliste</RouterLink>
  </header>
  <main class="inhalt">
    <RouterView />
  </main>
</template>
