<script setup>
import { RouterLink } from 'vue-router'
import { listen } from '../data/store'
</script>

<template>
  <RouterLink to="/" class="zurueck">← Hauptmenü</RouterLink>
  <h1>Märkte</h1>
  <p class="gedimmt">Wähle eine Liste, um ihre Produkte nach Markt sortiert zu sehen.</p>

  <ul class="karten">
    <li v-for="liste in listen" :key="liste.id" class="karte">
      <RouterLink :to="`/maerkte/${liste.id}`" class="karte-titel">
        {{ liste.name }}
        <span class="gedimmt">{{ liste.produkte.length }} Produkte</span>
      </RouterLink>
    </li>
  </ul>
</template>
