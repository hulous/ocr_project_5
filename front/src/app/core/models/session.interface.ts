
export interface Session {
  token: string;
  type?: string;
  id: number;
  username: string;
  admin?: boolean;
}
