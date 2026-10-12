<script setup>
import { computed, ref } from 'vue'
import { RouterLink, useRoute } from 'vue-router'
import { findeListe, produktHinzufuegen, produktLoeschen } from '../data/store'
import ProduktDialog from '../components/ProduktDialog.vue'

const route = useRoute()
const liste = computed(() => findeListe(route.params.id))
const dialogOffen = ref(false)

function hinzufuegen(produkt) {
  produktHinzufuegen(liste.value.id, produkt)
  dialogOffen.value = false
}
</script>

<template>
  <RouterLink to="/listen" class="zurueck">← Einkaufslisten</RouterLink>

  <p v-if="!liste">Diese Liste gibt es nicht.</p>
  <template v-else>
    <div class="titelzeile">
      <h1>{{ liste.name }}</h1>
      <button class="primaer" @click="dialogOffen = true">+ Produkt</button>
    </div>

    <p v-if="liste.produkte.length === 0" class="gedimmt">Noch keine Produkte in dieser Liste.</p>

    <ul class="karten">
      <li v-for="produkt in liste.produkte" :key="produkt.id" class="karte produkt" :class="{ erledigt: produkt.erledigt }">
        <input v-model="produkt.erledigt" type="checkbox" />
        <div class="produkt-text">
          <strong>{{ produkt.menge }} × {{ produkt.name }}</strong>
          <span v-if="produkt.markt" class="markt-chip">{{ produkt.markt }}</span>
          <div v-if="produkt.notiz" class="gedimmt">{{ produkt.notiz }}</div>
        </div>
        <button class="gefahr" title="Produkt löschen" @click="produktLoeschen(liste.id, produkt.id)">✕</button>
      </li>
    </ul>

    <ProduktDialog v-if="dialogOffen" @hinzufuegen="hinzufuegen" @verwerfen="dialogOffen = false" />
  </template>
</template>
