<script setup>
import { computed } from 'vue'
import { RouterLink, useRoute } from 'vue-router'
import { findeListe } from '../data/store'

const OHNE_MARKT = 'Ohne Markt'

const route = useRoute()
const liste = computed(() => findeListe(route.params.id))

// Produkte nach Markt gruppieren, "Ohne Markt" kommt immer ans Ende
const gruppen = computed(() => {
  const nachMarkt = {}
  for (const produkt of liste.value?.produkte ?? []) {
    const markt = produkt.markt || OHNE_MARKT
    ;(nachMarkt[markt] ??= []).push(produkt)
  }
  return Object.entries(nachMarkt)
    .map(([markt, produkte]) => ({ markt, produkte }))
    .sort((a, b) => {
      if (a.markt === OHNE_MARKT) return 1
      if (b.markt === OHNE_MARKT) return -1
      return a.markt.localeCompare(b.markt)
    })
})
</script>

<template>
  <RouterLink to="/maerkte" class="zurueck">← Märkte</RouterLink>

  <p v-if="!liste">Diese Liste gibt es nicht.</p>
  <template v-else>
    <h1>{{ liste.name }}</h1>
    <p v-if="gruppen.length === 0" class="gedimmt">Noch keine Produkte in dieser Liste.</p>

    <section v-for="gruppe in gruppen" :key="gruppe.markt" class="gruppe">
      <h2>{{ gruppe.markt }} <span class="gedimmt">({{ gruppe.produkte.length }})</span></h2>
      <ul class="karten">
        <li v-for="produkt in gruppe.produkte" :key="produkt.id" class="karte produkt" :class="{ erledigt: produkt.erledigt }">
          <input v-model="produkt.erledigt" type="checkbox" />
          <div class="produkt-text">
            <strong>{{ produkt.menge }} × {{ produkt.name }}</strong>
            <div v-if="produkt.notiz" class="gedimmt">{{ produkt.notiz }}</div>
          </div>
        </li>
      </ul>
    </section>
  </template>
</template>
