import { useAuth } from '@/stores/auth'

export const listConversations = async () => {
  const authStore = useAuth();

  const url = new URL('https://mastodonapp.uk/api/v1/conversations');
  url.searchParams.append('limit', '40');
  const result = await fetch(
    url, {
      method: 'GET',
      headers: { 'Authorization': `Bearer ${authStore.authorizationCode}` },
    }
  );

  if (result.ok) {
    console.log('OK');
    console.log(await result.text());
  } else {
    console.log('NOT OK');
  }

  const response = await result.text();
  console.log(response);
};
