import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { getUser } from '../../api';

const isAuthenticated = localStorage.getItem('isAuthenticated') === 'true';

const initialState = {
  isAuthenticated,
  user: {
    clientId: null,
  },
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
      localStorage.setItem('userId', action.payload);
      console.log('clientID:', state.user.clientId);
    },
  },
  extraReducers: (builder) => {
    builder.addCase(fetchCurrentUser.fulfilled, (state, action) => {
      state.user = action.payload;
    });
  },
});

export const getAccessToken = () => {
  const auth = JSON.parse(localStorage.getItem('auth'));
  console.log('authAccTok:', auth);
  return auth ? { ...auth, userId: localStorage.getItem('userId') } : null;
};


export const { setAuthenticated, setUser, setClientId } = authSlice.actions;

export const selectIsAuthenticated = (state) => state.auth.isAuthenticated;
export const selectUser = (state) => state.auth.user;

export default authSlice.reducer;
