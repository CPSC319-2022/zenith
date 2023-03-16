import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { getPosts, getPost } from '../../api';

export const fetchPosts = createAsyncThunk(
  'posts/fetchPosts',
  async (params, { rejectWithValue }) => {
    try {
      const response = await getPosts(params);
      return response;
    } catch (err) {
      return rejectWithValue(err.message);
    }
  }
);

export const fetchPost = createAsyncThunk(
  'posts/fetchPost',
  async (id, { rejectWithValue }) => {
    try {
      const response = await getPost(id);
      return response;
    } catch (err) {
      return rejectWithValue(err.message);
    }
  }
);

const initialState = {
  posts: [],
  post: null,
  status: 'idle',
  error: null,
};

const postSlice = createSlice({
  name: 'posts',
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(fetchPosts.pending, (state) => {
        state.status = 'loading';
      })
      .addCase(fetchPosts.fulfilled, (state, action) => {
        state.status = 'succeeded';
        state.posts = action.payload;
      })
      .addCase(fetchPosts.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.payload;
      })
      .addCase(fetchPost.pending, (state) => {
        state.status = 'loading';
      })
      .addCase(fetchPost.fulfilled, (state, action) => {
        state.status = 'succeeded';
        state.post = action.payload;
      })
      .addCase(fetchPost.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.payload;
      });
  },
});

export const postSliceActions = {
  ...postSlice.actions,
  fetchPosts,
  fetchPost,
};

export default postSlice.reducer;
