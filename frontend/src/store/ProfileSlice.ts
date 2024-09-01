import { PayloadAction, createSlice } from "@reduxjs/toolkit";

interface InitialState {
  id: string;
  name: string;
}
const initialState: InitialState = {
  id: "",
  name: "",
};

export const ProfileSlice = createSlice({
  initialState: initialState,
  name: "ProfileSlice",
  reducers: {
    setProfile: (
      state,
      action: PayloadAction<{ id: string; name: string }>
    ) => {
      state = action.payload;
      return state;
    },
  },
});

export const ProfileSliceActions = ProfileSlice.actions;
