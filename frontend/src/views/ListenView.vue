<script setup>
import { ref } from 'vue'
import { RouterLink } from 'vue-router'
import { listen, listeAnlegen, listeUmbenennen, listeLoeschen } from '../data/store'

const neuerName = ref('')
const bearbeiteId = ref(null)
const bearbeiteName = ref('')

function anlegen() {
  if (!neuerName.value.trim()) return
  listeAnlegen(neuerName.value.trim())
  neuerName.value = ''
}

function umbenennenStarten(liste) {
  bearbeiteId.value = liste.id
  bearbeiteName.value = liste.name
}

function umbenennenSpeichern() {
  if (bearbeiteName.value.trim()) listeUmbenennen(bearbeiteId.value, bearbeiteName.value.trim())
  bearbeiteId.value = null
}

function loeschen(liste) {
  if (confirm(`Liste „${liste.name}“ wirklich löschen?`)) listeLoeschen(liste.id)
}
</script>

<template>
  <RouterLink to="/" class="zurueck">← Hauptmenü</RouterLink>
  <h1>Einkaufslisten</h1>

  <form class="zeile" @submit.prevent="anlegen">
    <input v-model="neuerName" placeholder="Name der neuen Liste" />
    <button type="submit" class="primaer">Anlegen</button>
  </form>

  <p v-if="listen.length === 0" class="gedimmt">Noch keine Einkaufslisten vorhanden.</p>

  <ul class="karten">
    <li v-for="liste in listen" :key="liste.id" class="karte">
      <form v-if="bearbeiteId === liste.id" class="zeile" @submit.prevent="umbenennenSpeichern">
        <input v-model="bearbeiteName" />
        <button type="submit" class="primaer">Speichern</button>
        <button type="button" @click="bearbeiteId = null">Abbrechen</button>
      </form>
      <template v-else>
        <RouterLink :to="`/listen/${liste.id}`" class="karte-titel">
          {{ liste.name }}
          <span class="gedimmt">{{ liste.produkte.length }} Produkte</span>
        </RouterLink>
        <div class="aktionen">
          <button @click="umbenennenStarten(liste)">Umbenennen</button>
          <button class="gefahr" @click="loeschen(liste)">Löschen</button>
        </div>
      </template>
    </li>
  </ul>
</template>
