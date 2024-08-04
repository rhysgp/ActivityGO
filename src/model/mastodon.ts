
export interface ClientApplication {
  error: boolean;
  id: string;
  name: string;
  website: string | null;
  redirect_uri: string;
  client_id: string;
  client_secret: string;
  vapid_key: string;
}

export interface Token {
  error: false;
  access_token: string;
  token_type: string;
  scope: string;
  created_at: number;
}

export interface ApiError {
  error: true;
  status: number;
  errorMessage: string;
}
