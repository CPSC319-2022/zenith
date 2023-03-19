import { configureStore } from '@reduxjs/toolkit';
import postReducer from './slices/postSlice';
import commentReducer from './slices/commentSlice';

export const store = configureStore({
  reducer: {
    posts: postReducer,
    comments: commentReducer,
  },
});
