import { configureStore } from '@reduxjs/toolkit';
import postReducer from './slices/postSlice';
import commentReducer from './slices/commentSlice';
import authReducer from './slices/auth';
import userReducer from './slices/userSlice';

export const store = configureStore({
  reducer: {
    posts: postReducer,
    comments: commentReducer,
    auth: authReducer,
    users: userReducer,
  },
});
