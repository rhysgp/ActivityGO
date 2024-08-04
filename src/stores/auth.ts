import { defineStore } from 'pinia'
import { authorise, fetchToken, registerApp } from '@/net/auth_api_client'
import type { ClientApplication } from '@/model/mastodon'

interface AuthState {
  authorizationCode: string | undefined;
  authorizationToken: string | undefined;
  application: ClientApplication | undefined;
}

export const useAuth = defineStore('auth', {
  state: (): AuthState => ({
    authorizationCode: undefined,
    authorizationToken: undefined,
    application: undefined,
  }),
  getters: {
    authorized: (state): boolean =>
      state.authorizationCode !== undefined,
  },
  actions: {
    async loadRegisteredApp() {
      const clientAppItem = sessionStorage.getItem('registered_mastodon_application');
      if (clientAppItem) {
        this.application = JSON.parse(clientAppItem) as ClientApplication;
        return true;
      }
      return false;
    },
    async loadOrRegisterApp() {
      const loaded = await this.loadRegisteredApp();
      if (!loaded) {
        const clientApp = await registerApp();
        if (clientApp.error) {
          alert('Failed to register app');
          return false;
        }
        sessionStorage.setItem('registered_mastodon_application', JSON.stringify(clientApp))
        this.application = clientApp;
      }
      return true;
    },
    authorise() {
      if (this.application) {
        authorise(this.application.client_id);
      }
    },
    async setAuthorizationCode(code: string) {
      this.authorizationCode = code;
      if (!this.application) {
        if (!await this.loadRegisteredApp()) {
          console.log('auth: Failed to load registered app from session store');
          return false;
        }
      }
      return await this.fetchToken();
    },
    async fetchToken() {
      if (this.application && this.authorizationCode) {
        const result = await fetchToken(
          this.application.client_id, this.application.client_secret, this.authorizationCode
        );
        if (!result.error) {
          this.authorizationToken = result.access_token;
          console.log(`Token type: ${result.access_token}`);
          return true;
        } else {
          console.log(result);
          alert('Failed to authorize.');
        }
      } else {
        console.log("authStore(): Either client application or authorization code were not defined");
      }
      return false;
    },
  },
});
