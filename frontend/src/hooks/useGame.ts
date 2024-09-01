import { useAppSelector } from "../store/store";

export function useGame() {
  const game = useAppSelector((store) => store.GameSlice);
  return game;
}
