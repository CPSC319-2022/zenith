import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import {getPosts, getPost, getPostsByUser, createPost as createPostAPI, filterPosts as filterPostsAPI} from '../../api';
import { editPost as editPostAPI } from '../../api'
import { upvotePost as upvotePostApi, downvotePost as downvotePostApi } from '../../api';
import { deletePost as deletePostAPI } from '../../api';

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

export const fetchPostsByUser = createAsyncThunk(
    'posts/byUser',
    async ({ userID, count }, { rejectWithValue }) => {
        try {
            const response = await getPostsByUser({ userID, count });
            return response;
        } catch (err) {
            return rejectWithValue(err.message);
        }
    }
);

export const createPost = createAsyncThunk(
    'posts/createPost',
    async ({ postData, file }, { rejectWithValue }) => {
        try {
            await createPostAPI({ ...postData, image: file });
            return 'Post created successfully';
        } catch (err) {
            return rejectWithValue(err.message);
        }
    }
);

// Async thunk for editing a post
export const editPost = createAsyncThunk(
    'posts/editPost',
    async ({ postID, title, content, allowComments }, { rejectWithValue }) => {
        console.log('within thunk', postID, title, content, allowComments);
        try {
            const editedPost = await editPostAPI({postID, title, content, allowComments});
            return editedPost;
        } catch (err) {
            return rejectWithValue(err.message);
        }
    }
);

// Async thunk for upvoting a post
export const upvotePost = createAsyncThunk(
    'posts/upvote',
    async (postID, { rejectWithValue }) => {
        try {
            await upvotePostApi({ postID });
            return { postID };
        } catch (error) {
            return rejectWithValue(error.message);
        }
    }
);

// Async thunk for downvoting a post
export const downvotePost = createAsyncThunk(
    'posts/downvote',
    async (postID, { rejectWithValue }) => {
        try {
            await downvotePostApi({ postID });
            return { postID };
        } catch (error) {
            return rejectWithValue(error.message);
        }
    }
);


export const filterPosts = createAsyncThunk(
    'posts/filter',
    async ({ searchQuery, sortBy }, { rejectWithValue }) => {
        try {
            const response = await filterPostsAPI({ searchQuery, sortBy })
            return response;
        } catch (err) {
            return rejectWithValue(err.message);
        }
    }
);

// Async thunk for deleting a post
export const deletePost = createAsyncThunk(
    'posts/deletePost',
    async (postID, { rejectWithValue }) => {
      try {
        await deletePostAPI({ postID });
        return { postID };
      } catch (error) {
        return rejectWithValue(error.message);
      }
    }
  );
  

const initialState = {
    posts: [],
    post: null,
    status: 'idle',
    error: null,
    searchResults: [],
    userPosts: [],
};

const postSlice = createSlice({
    name: 'posts',
    initialState,
    reducers: { 
        resetStatus: (state) => {
        state.status = 'idle';
    },
},
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
            .addCase(fetchPostsByUser.pending, (state) => {
                state.status = 'loading';
            })
            .addCase(fetchPostsByUser.fulfilled, (state, action) => {
                state.status = 'succeeded';
                state.userPosts = action.payload;
            })
            .addCase(fetchPostsByUser.rejected, (state, action) => {
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
            })
            .addCase(filterPosts.pending, (state) => {
                state.status = 'loading';
            })
            .addCase(filterPosts.fulfilled, (state, action) => {
                state.status = 'succeeded';
                state.searchResults = action.payload;
            })
            .addCase(filterPosts.rejected, (state, action) => {
                state.status = 'failed';
                state.posts = action.payload;
            })
            .addCase(createPost.pending, (state) => {
                state.status = 'loading';
            })
            .addCase(createPost.fulfilled, (state) => {
                state.status = 'succeeded';
            })
            .addCase(createPost.rejected, (state, action) => {
                state.status = 'failed';
                state.error = action.payload;
            })
            .addCase(editPost.pending, (state) => {
                state.status = 'loading';
            })
            .addCase(editPost.fulfilled, (state, action) => {
                state.status = 'succeeded';
                state.post = action.payload;
            })
            .addCase(editPost.rejected, (state, action) => {
                state.status = 'failed';
                state.error = action.payload;
            })
            .addCase(deletePost.pending, (state) => {
                state.loading = true;
              });
              builder.addCase(deletePost.fulfilled, (state, action) => {
                state.loading = false;
                state.posts = state.posts.filter((post) => post.id !== action.payload.postID);
              });
              builder.addCase(deletePost.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload;
              });
    },
});

export const postSliceActions = {
    ...postSlice.actions,
    fetchPosts,
    fetchPost,
    fetchPostsByUser,
    createPost,
    editPost,
    upvotePost,
    downvotePost,
    filterPosts,
    deletePost,
    resetStatus: postSlice.actions.resetStatus,
};

export default postSlice.reducer;
