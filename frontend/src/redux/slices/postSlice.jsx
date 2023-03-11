import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
//import apiName from

const getAllPosts = createAsyncThunk(
    'chat/getAll',
    async (input, thunkAPI) => {
        try {
            //const res = await apiName.getAllPosts();
            //return res.data;
        } catch (e) {
            return thunkAPI.rejectWithValue(e);
        }
    }
)

const createPost = createAsyncThunk(
    'post/create',
    async (input, thunkAPI) => {
        try {
            const { body } = input;
            const title = body.get('title');
            const text = body.get('editor');
            //formfile?
            //const res = await apiName.createPost(title, text);
            //return res.data;
        } catch (e) {
            return thunkAPI.rejectWithValue(e);
        }
    }
)

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
    },
});

export const postSliceActions = {
    getAllPosts,
    createPost,
    ...postSlice.actions,
};
export default postSlice.reducer;