import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import basicSsl from '@vitejs/plugin-basic-ssl'
import scalaJSPlugin from '@scala-js/vite-plugin-scalajs'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    basicSsl(),
    scalaJSPlugin(),
  ],
  server: {
    https: true,
  },
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  }
})
