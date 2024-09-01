import styled from "styled-components";
import { TextView } from "./Text";
import { Button } from "./Button";

const StyledListContainer = styled.div`
  display: flex;
  align-items: center;
  flex-direction: column;
`;
const StyledList = styled.div`
  display: flex;
  align-items: center;
  flex-direction: column;
`;

export function AvailableGamesList({
  games,
  onRefresh,
  onSelectGame,
}: AvailableGamesListProps) {
  return (
    <StyledListContainer>
      <TextView fontSize="M" color="onBackground">
        Available Games
      </TextView>
      <Button variant="secondary" onClick={onRefresh}>
        Refresh
      </Button>
      <StyledList>
        {games.map((game) => {
          return (
            <Button
              variant="tertiary"
              key={game.id}
              onClick={() => onSelectGame(game.id)}
            >
              {game.name}
            </Button>
          );
        })}
      </StyledList>
    </StyledListContainer>
  );
}

interface AvailableGamesListProps {
  onRefresh: () => void;
  games: { id: string; name: string }[];
  onSelectGame: (id: string) => void;
}
