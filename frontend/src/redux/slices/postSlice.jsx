import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
//import apiName from

const getAllPosts = createAsyncThunk(
    'posts/getAll',
    async (input, thunkAPI) => {
        try {
            //const res = await apiName.getAllPosts();
            //return res.data;
        } catch (e) {
            return thunkAPI.rejectWithValue(e);
        }
    }
);

const createPost = createAsyncThunk(
    'posts/create',
    async (input, thunkAPI) => {
        try {
            const { body } = input;
            const title = body.get('title');
            const text = body.get('body');
            //formfile?
            //const res = await apiName.createPost(title, text);
            //return res.data;
        } catch (e) {
            return thunkAPI.rejectWithValue(e);
        }
    }
);

const deletePostByID = createAsyncThunk(
    'posts/deleteByID',
    async (input, thunkAPI) => {
        try {
            const { _id } = input;
            //const res = await apiName.deletePostByID(_id);
            //return res.data;
        } catch (e) {
            return thunkAPI.rejectWithValue(e);
        }
    }
);

const postSlice = createSlice({
    name: 'postSlice',
    initialState: {
        posts: [],
    },
    reducers: {
    },
    extraReducers: (builder) => {
        builder.addCase(getAllPosts.fulfilled, (state, action) => {
            state.posts = action.payload;
        });
        builder.addCase(createPost.fulfilled, (state, action) => {
            state.posts.push(action.payload);
        });
        builder.addCase(deletePostByID.fulfilled, (state, action) => {
            state.posts = state.posts.filter((posts) => posts._id !== action.payload);
        });
    },
});

export const postSliceActions = {
    getAllPosts,
    createPost,
    deletePostByID,
    ...postSlice.actions,
};
export default postSlice.reducer;