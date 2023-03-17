import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { getComments, getComment, createComment as createCommentAPI } from '../../api';
import { upvoteComment as upvoteCommentApi, downvoteComment as downvoteCommentApi } from '../../api';


export const fetchComments = createAsyncThunk(
  'comments/fetchComments',
  async (params, { rejectWithValue }) => {
    try {
      const response = await getComments(params);
      return response;
    } catch (err) {
      return rejectWithValue(err.message);
    }
  }
);

export const fetchComment = createAsyncThunk(
  'comments/fetchComment',
  async (id, { rejectWithValue }) => {
    try {
      const response = await getComment(id);
      return response;
    } catch (err) {
      return rejectWithValue(err.message);
    }
  }
);

export const createComment = createAsyncThunk(
  'comments/createComment',
  async (commentData, { rejectWithValue }) => {
    try {
      await createCommentAPI(commentData);
      return 'Comment created successfully';
    } catch (err) {
      return rejectWithValue(err.message);
    }
  }
);

// Async thunk for upvoting a comment
export const upvoteComment = createAsyncThunk(
  'comments/upvote',
  async ({ postID, commentID }, { rejectWithValue }) => {
    console.log('postID:', postID);
    console.log('commentID:', commentID);
    try {
      await upvoteCommentApi({ postID, commentID });
      return { postID, commentID };
    } catch (error) {
      return rejectWithValue(error.message);
    }
  }
);

// Async thunk for downvoting a comment
export const downvoteComment = createAsyncThunk(
  'comments/downvote',
  async ({ postID, commentID }, { rejectWithValue }) => {
    try {
      await downvoteCommentApi({ postID, commentID });
      return { postID, commentID };
    } catch (error) {
      return rejectWithValue(error.message);
    }
  }
);



const initialState = {
  comments: [],
  comment: null,
  status: 'idle',
  error: null,
};

const commentSlice = createSlice({
  name: 'comments',
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(fetchComments.pending, (state) => {
        state.status = 'loading';
      })
      .addCase(fetchComments.fulfilled, (state, action) => {
        state.status = 'succeeded';
        state.comments = action.payload;
      })
      .addCase(fetchComments.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.payload;
      })
      .addCase(fetchComment.pending, (state) => {
        state.status = 'loading';
      })
      .addCase(fetchComment.fulfilled, (state, action) => {
        state.status = 'succeeded';
        state.comment = action.payload;
      })
      .addCase(fetchComment.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.payload;
      })
      .addCase(createComment.pending, (state) => {
        state.status = 'loading';
      })
      .addCase(createComment.fulfilled, (state) => {
        state.status = 'succeeded';
      })
      .addCase(createComment.rejected, (state, action) => {
        state.status = 'failed';
        state.error = action.payload;
      });
  },
});

export const commentSliceActions = {
  ...commentSlice.actions,
  fetchComments,
  fetchComment,
  createComment,
};

export default commentSlice.reducer;
