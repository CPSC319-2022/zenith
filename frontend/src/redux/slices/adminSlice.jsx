// adminSlice.jsx
import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { getPromotionRequests, promoteUserByAdmin, deletePromotionRequest } from '../../api';

const initialState = {
  promotionRequests: [],
  loading: false,
  error: null,
};

// Async actions
export const fetchPromotionRequests = createAsyncThunk(
  'admin/fetchPromotionRequests',
  async ({requestIDStart, count, reverse}) => {
    const response = await getPromotionRequests(requestIDStart, count, reverse);
    return response;
  }
);

export const promoteUser = createAsyncThunk(
  'admin/promoteUser',
  async (body) => {
    const response = await promoteUserByAdmin(body);
    return response;
  }
);

export const deleteRequest = createAsyncThunk(
  'admin/deleteRequest',
  async (requestID) => {
    const response = await deletePromotionRequest(requestID);
    return response;
  }
);

export const adminSlice = createSlice({
  name: 'admin',
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    // Fetch promotion requests
    builder
      .addCase(fetchPromotionRequests.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchPromotionRequests.fulfilled, (state, action) => {
        state.loading = false;
        state.promotionRequests = action.payload;
      })
      .addCase(fetchPromotionRequests.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message;
      })

      // Promote user
      .addCase(promoteUser.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(promoteUser.fulfilled, (state) => {
        state.loading = false;
      })
      .addCase(promoteUser.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message;
      })

      // Delete promotion request
      .addCase(deleteRequest.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(deleteRequest.fulfilled, (state) => {
        state.loading = false;
      })
      .addCase(deleteRequest.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message;
      });
  },
});

export default adminSlice.reducer;
