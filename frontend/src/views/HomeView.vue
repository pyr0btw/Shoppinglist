<script setup>
import { RouterLink } from 'vue-router'
</script>

<template>
  <h1>Hauptmenü</h1>
  <nav class="menue">
    <RouterLink to="/listen" class="menue-kachel">
      <span class="menue-icon">📝</span>
      <strong>Einkaufslisten</strong>
      <span class="gedimmt">Listen anlegen, ändern und löschen</span>
    </RouterLink>
    <RouterLink to="/maerkte" class="menue-kachel">
      <span class="menue-icon">🏪</span>
      <strong>Märkte</strong>
      <span class="gedimmt">Produkte einer Liste nach Markt sortiert</span>
    </RouterLink>
  </nav>
</template>
