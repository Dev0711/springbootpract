import { createContext, useContext, useState, useEffect, useCallback } from 'react';
import {
  login as apiLogin,
  register as apiRegister,
  logout as apiLogout,
  getSavedUser,
  getAccessToken,
  clearTokens,
  saveUser,
} from '../api/api';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  // On mount, restore user from localStorage
  useEffect(() => {
    const token = getAccessToken();
    const savedUser = getSavedUser();
    if (token && savedUser) {
      setUser(savedUser);
    }
    setLoading(false);
  }, []);

  const login = useCallback(async (credentials) => {
    const response = await apiLogin(credentials);
    const userData = response?.user || getSavedUser();
    if (userData) {
      setUser(userData);
      saveUser(userData);
    }
    return response;
  }, []);

  const register = useCallback(async (data) => {
    const response = await apiRegister(data);
    const userData = response?.user || getSavedUser();
    if (userData) {
      setUser(userData);
      saveUser(userData);
    }
    return response;
  }, []);

  const logout = useCallback(async () => {
    await apiLogout();
    setUser(null);
  }, []);

  const updateUserState = useCallback((userData) => {
    setUser(userData);
    saveUser(userData);
  }, []);

  const isAuthenticated = !!user && !!getAccessToken();

  const value = {
    user,
    loading,
    isAuthenticated,
    login,
    register,
    logout,
    updateUserState,
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within AuthProvider');
  return ctx;
}
