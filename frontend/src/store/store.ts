import { configureStore } from "@reduxjs/toolkit";
import { TypedUseSelectorHook, useDispatch, useSelector } from "react-redux";
import { ProfileSlice } from "./ProfileSlice";
import { GameSlice } from "./GameSlice";
const store = configureStore({
  reducer: {
    [ProfileSlice.name]: ProfileSlice.reducer,
    [GameSlice.name]: GameSlice.reducer,
  },
});

export type AppDispatch = typeof store.dispatch;
export const useAppDispatch = useDispatch.withTypes<AppDispatch>();
type RootState = ReturnType<typeof store.getState>;
export const useAppSelector: TypedUseSelectorHook<RootState> = useSelector;
export default store;
