import { useEffect, useState } from "react";
import { useWindow } from "../../hooks/useWindow";
import Confetti from "react-confetti";
import { useProfile } from "../../hooks/useProfile";

export function StatusConfetti({ winner }: StatusConfettiProps) {
  const { w, h } = useWindow();
  const { id } = useProfile();
  const [isIamWinner, setIsIamWinner] = useState(false);
  useEffect(() => {
    setIsIamWinner(winner.id == id);
  }, []);
  if (isIamWinner) {
    return <Confetti width={w} height={h} colors={["#035cc2", "#f22853"]} />;
  }
  return <Confetti width={w} height={h} colors={["gray", "black"]} />;
}
interface StatusConfettiProps {
  winner: { id: string; name: string };
}
