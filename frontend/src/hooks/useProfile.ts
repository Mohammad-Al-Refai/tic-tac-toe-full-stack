import { useAppSelector } from "../store/store";

export function useProfile() {
  const profile = useAppSelector((store) => store.ProfileSlice);
  return profile;
}
