import type { ApiError, ClientApplication, Token } from '@/model/mastodon'

const redirectTo = 'https://www.rhyssoft.com:5173/auth_redirect';

export const registerApp = async (): Promise<ClientApplication | ApiError> => {
  const response = await fetch(
    'https://mastodonapp.uk/api/v1/apps', {
      method: 'POST',
      body: new URLSearchParams({
        'client_name': 'ActivityGo_0_0_1',
        'redirect_uris': redirectTo,
      }),
    }
  );

  if (response.ok) {
    const result = await response.json();
    return { error: false, ...result } as ClientApplication;
  } else {
    return await buildError(response);
  }
};

/**
 * Get authorization from the user.
 */
export const authorise = (clientId: string) => {
  window.location.href = `https://mastodonapp.uk/oauth/authorize?client_id=${clientId}&redirect_uri=${redirectTo}&response_type=code`;
};

/**
 * Authorize the current user to use the app. The user must be logged in (I
 * think)
 *
 * We can use the token from this to make future calls as that user.
 */
export const fetchToken = async (clientId: string, clientSecret: string, authCode: string): Promise<Token | ApiError> => {
  const url = new URL('https://mastodonapp.uk/oauth/token');
  const response = await fetch(url, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      client_id: clientId,
      client_secret: clientSecret,
      code: authCode,
      grant_type: 'authorization_code',
      redirect_uri: redirectTo,
      scope: "read",
    }),
  });

  if (response.ok) {
    const result = await response.json();
    console.log(result);
    return { error: false, ...result } as Token;
  } else {
    return buildError(response);
  }
}
// export const fetchToken = async (): Promise<Token | ApiError> => {
//   const url = new URL('https://mastodonapp.uk/oauth/token');
//   url.searchParams.append('client_id', clientApplication.client_id);
//   url.searchParams.append('redirect_uri', redirectTo);
//   url.searchParams.append('response_type', 'code');
//   const authStore = useAuth();
//   const response = await fetch(
//     url, {
//       headers: { 'Authorization': `Bearer ${authStore.authorizationCode}` },
//     }
//   );
//
//   if (response.ok) {
//     const result = await response.json();
//     console.log(result);
//     return { error: false, ...result } as Token;
//   } else {
//     console.log(`Failed to authorize: ${await response.text()}`);
//     return buildError(response);
//   }
// }

const buildError = async (response: Response): Promise<ApiError> => {
  return {
    error: true,
    status: response.status,
    errorMessage: await response.text(),
  } as ApiError;
}
