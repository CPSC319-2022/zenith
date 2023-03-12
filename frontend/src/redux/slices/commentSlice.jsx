import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
//import apiName from

const getAllComments = createAsyncThunk(
    'comments/getAll',
    async (input, thunkAPI) => {
        try {
            //const res = await apiName.getAllComments();
            //return res.data;
        } catch (e) {
            return thunkAPI.rejectWithValue(e);
        }
    }
);

const createComment = createAsyncThunk(
    'comments/create',
    async (input, thunkAPI) => {
        try {
            const { body } = input;
            const text = body.get('body');
            //const res = await apiName.createComment(text);
            //return res.data;
        } catch (e) {
            return thunkAPI.rejectWithValue(e);
        }
    }
);

const deleteCommentByID = createAsyncThunk(
    'comments/deleteByID',
    async (input, thunkAPI) => {
        try {
            const { _id } = input;
            //const res = await apiName.deleteCommentByID(_id);
            //return res.data;
        } catch (e) {
            return thunkAPI.rejectWithValue(e);
        }
    }
);

const commentSlice = createSlice({
    name: 'commentSlice',
    initialState: {
        comments: [],
    },
    reducers: {
    },
    extraReducers: (builder) => {
        builder.addCase(getAllComments.fulfilled, (state, action) => {
            state.comments = action.payload;
        });
        builder.addCase(createComment.fulfilled, (state, action) => {
            state.comments.push(action.payload);
        });
        builder.addCase(deleteCommentByID.fulfilled, (state, action) => {
            state.comments = state.comments.filter((comments) => comments._id !== action.payload);
        });
    },
});

export const commentSliceActions = {
    getAllComments,
    createComment,
    deleteCommentByID,
    ...commentSlice.actions,
};
export default commentSlice.reducer;