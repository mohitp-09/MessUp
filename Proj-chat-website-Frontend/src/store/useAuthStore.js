import { create } from "zustand";
import { getCurrentUser, logout as apiLogout } from "../lib/api";

const useAuthStore = create((set, get) => ({
  isAuthenticated: false,
  user: null,
  isLoading: false,

  // Check authentication status by calling the backend
  checkAuthStatus: async () => {
    try {
      set({ isLoading: true });
      const user = await getCurrentUser();
      
      set({
        isAuthenticated: true,
        user: user,
        isLoading: false
      });
      
      return true;
    } catch (error) {
      console.log('Not authenticated or session expired');
      set({
        isAuthenticated: false,
        user: null,
        isLoading: false
      });
      return false;
    }
  },

  login: async (userData) => {
    // After successful login, get the current user data
    try {
      const user = await getCurrentUser();
      set({
        isAuthenticated: true,
        user: user
      });
    } catch (error) {
      console.error('Failed to get user data after login:', error);
      // Still set as authenticated since login was successful
      set({
        isAuthenticated: true,
        user: userData || null
      });
    }
  },

  logout: async () => {
    console.log('Logging out user...');

    try {
      // Call backend logout endpoint to clear cookie
      await apiLogout();
    } catch (error) {
      console.error('Backend logout failed:', error);
      // Continue with frontend logout even if backend fails
    }

    console.log('Updating state...');

    // Update state
    set({
      isAuthenticated: false,
      user: null
    });

    console.log('Logout complete');
  },

  updateUser: (userData) => {
    set({ user: userData });
  },

  // Refresh user data from backend
  refreshUser: async () => {
    try {
      const user = await getCurrentUser();
      set({ user: user });
      return user;
    } catch (error) {
      console.error('Failed to refresh user data:', error);
      // If refresh fails, user might be logged out
      get().logout();
      return null;
    }
  }
}));

export { useAuthStore };