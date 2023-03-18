import { createSlice } from '@reduxjs/toolkit';

const isAuthenticated = localStorage.getItem('isAuthenticated') === 'true';

const initialState = {
  isAuthenticated,
  user: null,
};

export const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    setAuthenticated: (state, action) => {
      localStorage.setItem('isAuthenticated', action.payload);
      state.isAuthenticated = action.payload;
    },
    setUser: (state, action) => {
      state.user = action.payload;
    },
  },
});

export const { setAuthenticated, setUser } = authSlice.actions;

export const selectIsAuthenticated = (state) => state.auth.isAuthenticated;
export const selectUser = (state) => state.auth.user;

export default authSlice.reducer;
