import { PayloadAction, createSlice } from "@reduxjs/toolkit";
import { CellState } from "../service/Response";

interface InitialState {
  turn: CellState;
  isJoinedGame: boolean;
  gameId: string;
  board: CellState[][];
  Iam: CellState;
  opponent: { id: string; name: string };
  winner: { id: string; name: string; type: CellState };
}
const initialState: InitialState = {
  isJoinedGame: false,
  gameId: "",
  board: [],
  opponent: { id: "", name: "" },
  turn: "NONE",
  Iam: "NONE",
  winner: { id: "", name: "", type: "NONE" },
};

export const GameSlice = createSlice({
  initialState: initialState,
  name: "GameSlice",
  reducers: {
    setBoard: (state, action: PayloadAction<CellState[][]>) => {
      state.board = action.payload;
      return state;
    },
    setGameId: (state, action: PayloadAction<string>) => {
      state.gameId = action.payload;
      return state;
    },
    setTurn: (state, action: PayloadAction<CellState>) => {
      state.turn = action.payload;
      return state;
    },
    setIam: (state, action: PayloadAction<CellState>) => {
      state.Iam = action.payload;
      return state;
    },
    setIsJoinedGame: (state, action: PayloadAction<boolean>) => {
      state.isJoinedGame = action.payload;
      return state;
    },
    setWinner: (
      state,
      action: PayloadAction<{ id: string; name: string; type: CellState }>
    ) => {
      state.winner = action.payload;
      return state;
    },
    setOpponent: (
      state,
      action: PayloadAction<{ id: string; name: string }>
    ) => {
      state.opponent = action.payload;
      return state;
    },
    clear: () => {
      return initialState;
    },
  },
});

export const GameSliceActions = GameSlice.actions;
