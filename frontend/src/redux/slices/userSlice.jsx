import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { getUsers, getUser, editUser as editUserAPI } from '../../api';


export const fetchUsers = createAsyncThunk(
    'users/fetchUsers',
    async (params, { rejectWithValue }) => {
        try {
            const response = await getUsers(params);
            return response;
        } catch (err) {
            return rejectWithValue(err.message);
        }
    }
);

export const fetchUser = createAsyncThunk(
    'users/fetchUser',
    async (id, { rejectWithValue }) => {
        try {
            const response = await getUser(id);
            return response;
        } catch (err) {
            return rejectWithValue(err.message);
        }
    }
);

export const fetchCurrentUserByToken = createAsyncThunk(
    'users/fetchCurrentUserByToken',
    async (id, { rejectWithValue }) => {
        try {
            const response = await getUser();
            return response;
        } catch (err) {
            return rejectWithValue(err.message);
        }
    }
);

export const editUser = createAsyncThunk(
    'users/updateUser',
    async ({ userID, name, email }, { rejectWithValue }) => {
        try {
            const updatedUser = await editUserAPI({ userID, name, email });
            return updatedUser;
        } catch (err) {
            return rejectWithValue(err.message);
        }
    }
);

const initialState = {
    users: [],
    user: null,
    currentUser: null,
    status: 'idle',
    error: null,
};

const userSlice = createSlice({
    name: 'users',
    initialState,
    reducers: { setCurrentUser: (state, action) => {
        state.currentUser = action.payload;
    },
},
    extraReducers: (builder) => {
        builder
            .addCase(fetchUsers.pending, (state) => {
                state.status = 'loading';
            })
            .addCase(fetchUsers.fulfilled, (state, action) => {
                state.status = 'succeeded';
                state.users = action.payload;
                state.user.userLevel = action.payload.userLevel;
            })
            .addCase(fetchUsers.rejected, (state, action) => {
                state.status = 'failed';
                state.error = action.payload;
            })
            .addCase(fetchUser.pending, (state) => {
                state.status = 'loading';
            })
            .addCase(fetchUser.fulfilled, (state, action) => {
                state.status = 'succeeded';
                state.user = action.payload;
            })
            .addCase(fetchUser.rejected, (state, action) => {
                state.status = 'failed';
                state.error = action.payload;
            })
            .addCase(editUser.pending, (state) => {
                state.status = 'loading';
            })
            .addCase(editUser.fulfilled, (state, action) => {
                state.status = 'succeeded';
                state.user = action.payload;
            })
            .addCase(editUser.rejected, (state, action) => {
                state.status = 'failed';
                state.error = action.payload;
            })
            .addCase(fetchCurrentUserByToken.fulfilled, (state, action) => {
                state.user = action.payload;
                state.user.userLevel = action.payload.userLevel;
              });
    },
});

export const userSliceActions = {
    ...userSlice.actions,
    fetchUsers,
    fetchUser,
    editUser,
};

export default userSlice.reducer;
