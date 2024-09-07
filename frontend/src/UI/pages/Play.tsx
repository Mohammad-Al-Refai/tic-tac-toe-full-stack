import { useParams } from "react-router-dom";
import { Board } from "../components/Board";
import { validate } from "uuid";
import { useEffect, useState } from "react";
import { TextView } from "../components/Text";
import { useGame } from "../../hooks/useGame";
import { StatusConfetti } from "../components/StatusConfetti";
import { If } from "../components/If";
import { Button } from "../components/Button";
import { useProfile } from "../../hooks/useProfile";
import { useTheme } from "styled-components";
import { PersonCircle } from "react-bootstrap-icons";
import { toast } from "react-toastify";

export function PlayPage({ onCellClicked, join, quitGame }: PlayProps) {
  const [isGameIdValid, setIsGameIdValid] = useState(false);
  const { gameId } = useParams();
  const { name } = useProfile();
  const { opponent, board, Iam, winner, turn } = useGame();

  useEffect(() => {
    const isValid = validate(String(gameId));
    setIsGameIdValid(isValid);
    if (isValid) {
      join(String(gameId));
    }
  }, []);
  function onCopy() {
    window.navigator.clipboard.writeText(location.href);
    toast("Copied");
  }
  if (!isGameIdValid) {
    return <h1>Invalid gameId</h1>;
  }
  if (opponent.id == "") {
    return (
      <div className="flex col align-items-center justify-content-center mt-xl4">
        <div className="p-xl br-m mt-xl3 mb-xl3 bg-secondary">
          <TextView
            as="span"
            className="ml-xs mr-xs"
            fontSize="S"
            color="onTertiary"
          >
            invite your friend by
          </TextView>
          <TextView
            as="span"
            className="ml-xs mr-xs"
            fontSize="S"
            color="primary"
            bold
            underline
            pointer
            onClick={onCopy}
          >
            copy
          </TextView>
          <TextView
            as="span"
            className="ml-xs mr-xs"
            fontSize="S"
            color="onTertiary"
            onClick={onCopy}
          >
            game URL
          </TextView>
        </div>
        <TextView fontSize={"XL"} color={"secondary"}>
          Waiting for opponent...
        </TextView>
        <Button variant="primary" onClick={quitGame}>
          Quit Game
        </Button>
      </div>
    );
  }

  return (
    <>
      <If condition={winner.id != ""}>
        <StatusConfetti winner={winner} />
        <TextView
          color={winner.type == Iam ? "secondary" : "onBackground"}
          fontSize="XL3"
          className="mt-xl mb-xl"
        >
          {winner.type == Iam ? "You win" : "You lost"}
        </TextView>
      </If>
      <If condition={opponent.name != "" && winner.type == "NONE"}>
        <div className="flex justify-content-space-evenly w-100 mt-xl5">
          <PlayerProfile name={name} type="ME" />
          <PlayerProfile name={opponent.name} type="OPPONENT" />
        </div>
        <TextView color={"secondary"} fontSize="XL3" className="mt-xl mb-xl">
          {Iam == turn ? "Your turn" : `${opponent.name} turn`}
        </TextView>
      </If>
      <Board board={board} onCellClicked={onCellClicked} Iam={Iam} />
      <Button variant="primary" onClick={quitGame}>
        Quit Game
      </Button>
    </>
  );
}
function PlayerProfile({ name, type }: PlayerProfileProps) {
  const theme = useTheme();
  return (
    <div className="flex col align-items-center justify-content-center">
      <PersonCircle
        size={"50px"}
        color={type == "ME" ? theme.colors.secondary : theme.colors.tertiary}
      />
      <TextView color="onBackground" className="mt-l" fontSize="M">
        {type == "ME" ? "You" : name}
      </TextView>
    </div>
  );
}
interface PlayerProfileProps {
  name: string;
  type: "OPPONENT" | "ME";
}
interface PlayProps {
  onCellClicked: (row: number, column: number) => void;
  join: (gameId: string) => void;
  quitGame: () => void;
}
