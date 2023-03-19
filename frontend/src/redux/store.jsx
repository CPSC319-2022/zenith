import { configureStore } from '@reduxjs/toolkit';
import postReducer from './slices/postSlice';
import commentReducer from './slices/commentSlice';
import authReducer from './slices/auth';

export const store = configureStore({
  reducer: {
    posts: postReducer,
    comments: commentReducer,
    auth: authReducer,
  },
});
