import { useNavigate } from "react-router-dom";
import { useEffect } from "react";
import { useAppDispatch } from "../../store/store";
import { GameSliceActions } from "../../store/GameSlice";
import { Button } from "../components/Button";
import { AvailableGamesList } from "../components/AvailableGamesList";

export function LandingPage({
  availableGames,
  onRefresh,
  onCreateClicked,
}: LandingPageProps) {
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  function handelOnSelectGame(id: string) {
    navigate("/play/" + id, { replace: true });
  }
  useEffect(() => {
    dispatch(GameSliceActions.clear());
    onRefresh();
  }, []);
  return (
    <div className="flex mt-xl3 align-items-center justify-content-space-evenly w-100">
      <AvailableGamesList
        onSelectGame={handelOnSelectGame}
        games={availableGames}
        onRefresh={onRefresh}
      />
      <Button variant="secondary" onClick={onCreateClicked}>
        New Game
      </Button>
    </div>
  );
}

interface LandingPageProps {
  availableGames: { id: string; name: string }[];
  onRefresh: () => void;
  onCreateClicked: () => void;
}
