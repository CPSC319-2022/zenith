import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { getUser } from '../../api';

const isAuthenticated = localStorage.getItem('isAuthenticated') === 'true';

const initialState = {
  isAuthenticated,
  user: {
    clientId: null,
  },
  loading: false,
};

export const fetchCurrentUser = createAsyncThunk(
  'auth/fetchCurrentUser',
  async (_, { rejectWithValue }) => {
    try {
      const response = await getUser(); // Assuming this API call fetches the current user details
      return response;
    } catch (err) {
      return rejectWithValue(err.message);
    }
  }
);

// authSlice.jsx
const authSlice = createSlice({
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
      localStorage.setItem('userId', action.payload);
      console.log('clientID:', state.user.clientId);
    },
    resetAuth: () => {
      // Reset the auth state to the initial state
      localStorage.removeItem('isAuthenticated');
      localStorage.removeItem('userId');
      localStorage.removeItem('auth');
      return initialState;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchCurrentUser.pending, (state) => {
        state.loading = true;
      })
      .addCase(fetchCurrentUser.fulfilled, (state, action) => {
        state.user = action.payload;
        state.loading = false;
      })
      .addCase(fetchCurrentUser.rejected, (state, action) => {
        state.loading = false;
      });
  },
});

export const selectClientID = (state) => {
  if (state.auth.user) {
    return state.auth.user.clientID;
  } else {
    return undefined;
  }
};


export const getAccessToken = () => {
  const auth = JSON.parse(localStorage.getItem('auth'));
  return auth ? { ...auth, userId: localStorage.getItem('userId') } : null;
};

export const { setAuthenticated, setUser, setClientId, resetAuth } = authSlice.actions;
export const selectIsAuthenticated = (state) => state.auth.isAuthenticated;
export const selectUser = (state) => state.auth.user;
export const selectLoading = (state) => state.auth.loading;
export default authSlice.reducer;
