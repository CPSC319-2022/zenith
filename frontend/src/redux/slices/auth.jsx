import { createSlice } from '@reduxjs/toolkit';

const isAuthenticated = localStorage.getItem('isAuthenticated') === 'true';

const initialState = {
  isAuthenticated,
  user: {
    clientId: null,
  },
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
    setClientId: (state, action) => {
      state.user.clientId = action.payload;
      console.log('clientID:', state.user.clientId);
    },
  },
});

export const getAccessToken = () => {
  const auth = JSON.parse(localStorage.getItem('auth'));
  console.log('authAccTok:', auth);
  return auth ? auth : null;
};

export const { setAuthenticated, setUser, setClientId } = authSlice.actions;

export const selectIsAuthenticated = (state) => state.auth.isAuthenticated;
export const selectUser = (state) => state.auth.user;

export default authSlice.reducer;
