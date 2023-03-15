import { configureStore } from '@reduxjs/toolkit';
import postReducer from './slices/postSlice';

export const store = configureStore({
  reducer: {
    posts: postReducer,
  },
});
