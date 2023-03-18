import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { getComments, getComment, createComment as createCommentAPI } from '../../api';
import {
    upvoteComment as upvoteCommentApi,
    downvoteComment as downvoteCommentApi,
    deleteComment as deleteCommentApi,
    editComment as editCommentApi
} from '../../api';


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

// Async thunk for deleting a comment
export const deleteComment = createAsyncThunk(
    'comment/delete',
    async ({ postID, commentID }, { rejectWithValue }) => {
        try {
            await deleteCommentApi({ postID, commentID });
            return { postID, commentID };
        } catch (error) {
            return rejectWithValue(error.message);
        }
    }
);

// Async thunk for editing a comment
export const editComment = createAsyncThunk(
    'comment/edit',
    async ({ postID, commentID, content }, { rejectWithValue }) => {
        console.log('within thunk', postID, commentID, content);
        try {
            await editCommentApi({ postID, commentID, content });
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
            })
            .addCase(deleteComment.fulfilled, (state, action) => {
                state.comments = state.comments.filter((comment) => comment.commentID !== action.payload.commentID);
            })
    },
});

export const commentSliceActions = {
    ...commentSlice.actions,
    fetchComments,
    fetchComment,
    createComment,
    deleteComment
};

export default commentSlice.reducer;
