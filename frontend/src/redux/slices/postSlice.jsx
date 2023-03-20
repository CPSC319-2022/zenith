import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import {getPosts, getPost, createPost as createPostAPI, editComment as editCommentApi} from '../../api';
import { editPost as editPostAPI } from '../../api'
import { upvotePost as upvotePostApi, downvotePost as downvotePostApi } from '../../api';


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

export const createPost = createAsyncThunk(
    'posts/createPost',
    async (postData, { rejectWithValue }) => {
        try {
            await createPostAPI(postData);
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
// export const editPost = createAsyncThunk(
//     'posts/editPost',
//     async ({ postID, title, content, allowComments }, { rejectWithValue }) => {
//         console.log('within thunk', postID, title, content, allowComments);
//         try {
//             await editPostAPI({postID, title, content, allowComments});
//             return 'Post created successfully';
//         } catch (err) {
//             return rejectWithValue(err.message);
//         }
//     }
// );

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
            });
    },
});

export const postSliceActions = {
    ...postSlice.actions,
    fetchPosts,
    fetchPost,
    createPost,
    editPost,
    upvotePost,
    downvotePost,
};

export default postSlice.reducer;
