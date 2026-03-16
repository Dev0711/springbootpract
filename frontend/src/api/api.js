// API Service Layer — talks to auth-service (8080) and user-service (8081)

const AUTH_BASE = 'http://localhost:8080/api/auth';
const USER_BASE = 'http://localhost:8081/api/users';

// ——— Helpers ———

function getAccessToken() {
  return localStorage.getItem('accessToken');
}

function getRefreshToken() {
  return localStorage.getItem('refreshToken');
}

function saveTokens({ accessToken, refreshToken }) {
  if (accessToken) localStorage.setItem('accessToken', accessToken);
  if (refreshToken) localStorage.setItem('refreshToken', refreshToken);
}

function clearTokens() {
  localStorage.removeItem('accessToken');
  localStorage.removeItem('refreshToken');
  localStorage.removeItem('user');
}

function saveUser(user) {
  localStorage.setItem('user', JSON.stringify(user));
}

function getSavedUser() {
  try {
    const raw = localStorage.getItem('user');
    return raw ? JSON.parse(raw) : null;
  } catch {
    return null;
  }
}

async function request(url, options = {}) {
  const headers = {
    'Content-Type': 'application/json',
    ...options.headers,
  };

  const token = getAccessToken();
  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }

  const response = await fetch(url, {
    ...options,
    headers,
  });

  // Try to parse JSON (some responses may be empty)
  let data = null;
  const text = await response.text();
  if (text) {
    try {
      data = JSON.parse(text);
    } catch {
      data = text;
    }
  }

  if (!response.ok) {
    // Extract error message from various response formats
    const errorMessage =
      data?.message ||
      data?.error ||
      (typeof data === 'string' ? data : `Request failed (${response.status})`);
    const error = new Error(errorMessage);
    error.status = response.status;
    error.data = data;
    throw error;
  }

  return data;
}

// ——— Auth Service APIs ———

export async function register({ email, password, firstName, lastName, phoneNumber }) {
  const data = await request(`${AUTH_BASE}/register`, {
    method: 'POST',
    body: JSON.stringify({ email, password, firstName, lastName, phoneNumber }),
  });

  // Auth-service returns ApiResponse<AuthResponse>
  const authResponse = data?.data || data;

  if (authResponse?.accessToken) {
    saveTokens(authResponse);
  }
  if (authResponse?.user) {
    saveUser(authResponse.user);
  }

  return authResponse;
}

export async function login({ email, password }) {
  const data = await request(`${AUTH_BASE}/login`, {
    method: 'POST',
    body: JSON.stringify({ email, password }),
  });

  const authResponse = data?.data || data;

  if (authResponse?.accessToken) {
    saveTokens(authResponse);
  }
  if (authResponse?.user) {
    saveUser(authResponse.user);
  }

  return authResponse;
}

export async function refreshAccessToken() {
  const refreshToken = getRefreshToken();
  if (!refreshToken) throw new Error('No refresh token');

  const data = await request(`${AUTH_BASE}/refresh-token`, {
    method: 'POST',
    body: JSON.stringify({ refreshToken }),
  });

  const tokenResponse = data?.data || data;

  if (tokenResponse?.accessToken) {
    saveTokens(tokenResponse);
  }

  return tokenResponse;
}

export async function logout() {
  const refreshToken = getRefreshToken();
  try {
    if (refreshToken) {
      await request(`${AUTH_BASE}/logout`, {
        method: 'POST',
        body: JSON.stringify({ refreshToken }),
      });
    }
  } catch {
    // Ignore logout errors — clear locally regardless
  } finally {
    clearTokens();
  }
}

export async function forgotPassword(email) {
  return request(`${AUTH_BASE}/forgot-password`, {
    method: 'POST',
    body: JSON.stringify({ email }),
  });
}

export async function resetPassword({ token, newPassword }) {
  return request(`${AUTH_BASE}/reset-password`, {
    method: 'POST',
    body: JSON.stringify({ token, newPassword }),
  });
}

// ——— User Service APIs ———

export async function getUserById(id) {
  return request(`${USER_BASE}/${id}`);
}

export async function getUserByEmail(email) {
  return request(`${USER_BASE}/email/${email}`);
}

export async function updateUser(id, userData) {
  return request(`${USER_BASE}/${id}`, {
    method: 'PUT',
    body: JSON.stringify(userData),
  });
}

export async function getAllUsers(page = 0, size = 10) {
  return request(`${USER_BASE}/paginated?page=${page}&size=${size}`);
}

// ——— Utility Exports ———

export {
  getAccessToken,
  getRefreshToken,
  saveTokens,
  clearTokens,
  saveUser,
  getSavedUser,
};
