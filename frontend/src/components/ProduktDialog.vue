<script setup>
import { computed, ref } from 'vue'
import { bekannteMaerkte, marktAbgleichen } from '../data/store'

const emit = defineEmits(['hinzufuegen', 'verwerfen'])

const name = ref('')
const menge = ref(1)
const markt = ref('')
const notiz = ref('')

const nameFehlt = computed(() => !name.value.trim())
const mengeUngueltig = computed(() => !Number.isInteger(menge.value) || menge.value < 1)

// Hinweis, ob der eingegebene Markt schon bekannt ist oder neu angelegt wird
const marktHinweis = computed(() => {
  if (!markt.value.trim()) return ''
  return bekannteMaerkte.value.includes(marktAbgleichen(markt.value)) ? 'Bekannter Markt' : 'Neuer Markt'
})

function hinzufuegen() {
  if (nameFehlt.value || mengeUngueltig.value) return
  emit('hinzufuegen', {
    name: name.value.trim(),
    menge: menge.value,
    markt: marktAbgleichen(markt.value),
    notiz: notiz.value.trim(),
  })
}
</script>

<template>
  <div class="overlay" @click.self="emit('verwerfen')">
    <form class="dialog" @submit.prevent="hinzufuegen">
      <h2>Produkt hinzufügen</h2>

      <label>
        Name *
        <input v-model="name" autofocus required />
      </label>

      <label>
        Menge *
        <input v-model.number="menge" type="number" min="1" step="1" required />
      </label>

      <label>
        Markt
        <input v-model="markt" list="maerkte-liste" placeholder="z. B. Rewe" />
        <datalist id="maerkte-liste">
          <option v-for="m in bekannteMaerkte" :key="m" :value="m" />
        </datalist>
        <small v-if="marktHinweis" class="gedimmt">{{ marktHinweis }}</small>
      </label>

      <label>
        Notiz
        <textarea v-model="notiz" rows="2" placeholder="z. B. 500 g, Bio, Sorte …"></textarea>
      </label>

      <div class="aktionen">
        <button type="button" @click="emit('verwerfen')">Verwerfen</button>
        <button type="submit" class="primaer" :disabled="nameFehlt || mengeUngueltig">Hinzufügen</button>
      </div>
    </form>
  </div>
</template>
