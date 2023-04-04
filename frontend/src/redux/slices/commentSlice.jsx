import {createAsyncThunk, createSlice} from '@reduxjs/toolkit';
import {
    createComment as createCommentAPI,
    deleteComment as deleteCommentApi,
    downvoteComment as downvoteCommentApi,
    editComment as editCommentApi,
    getComment,
    getComments,
    upvoteComment as upvoteCommentApi
} from '../../api';
import { userSliceActions } from './userSlice';



export const fetchComments = createAsyncThunk(
  'comments/fetchComments',
  async (params, { dispatch, rejectWithValue }) => {
    try {
      const response = await getComments(params);
      const comments = response;
      const commentsWithAuthors = await Promise.all(
        comments.map(async (comment) => {
          const author = await dispatch(userSliceActions.fetchUser(comment.authorID)).then((response) => response.payload);
          return {
            ...comment,
            authorUsername: author.username,
            authorProfilePicture: author.profilePicture,
          };
        }),
      );
      return commentsWithAuthors;
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
    async ({ postID, commentID, content }, { rejectWithValue, getState }) => {
        try {
            await editCommentApi({ postID, commentID, content });
            const comments = getState().comments.comments;
            const commentIndex = comments.findIndex((comment) => comment.commentID === commentID);
            if (commentIndex >= 0) {
                const updatedComment = { ...comments[commentIndex], content };
                return { postID, commentID, comment: updatedComment };
            }
            throw new Error('Comment not found');
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
                state.comments = action.payload.reverse();
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
            .addCase(editComment.fulfilled, (state, action) => {
                state.status = 'succeeded';
                const { postID, commentID, comment } = action.payload;
                const comments = state.comments.slice();
                const commentIndex = comments.findIndex((comment) => comment.commentID === commentID);
                if (commentIndex >= 0) {
                    comments[commentIndex] = comment;
                }
                state.comments = comments;
                state.comment = comment;
            })
    },
});

export const commentSliceActions = {
    ...commentSlice.actions,
    fetchComments,
    fetchComment,
    createComment,
    deleteComment,
    editComment
};

export default commentSlice.reducer;
