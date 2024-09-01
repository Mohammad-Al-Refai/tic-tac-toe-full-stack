import { CellState } from "../../service/Response";
import X from "../../assets/x.svg?react";
import O from "../../assets/o.svg?react";
import styled from "styled-components";
import { ThemeColors } from "../theme/ThemeTypes";
const StyledBoard = styled.div`
  display: flex;
  flex-wrap: wrap;
  width: 324px;
  height: 324px;
`;
const StyledCell = styled.div<{ $backgroundColor: ThemeColors }>`
  @keyframes animate-cell {
    from {
      scale: 0;
    }
    to {
      scale: 1;
    }
  }

  width: 100px;
  height: 100px;
  border: ${(props) => props.theme.Surroundings.S} solid white;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: ${(props) => props.theme.Surroundings.M};
  margin: ${(props) => props.theme.Surroundings.S};
  font-size: ${(props) => props.theme.fontSize.XL};
  background-color: ${(props) => props.theme.colors[props.$backgroundColor]};
  svg {
    animation: animate-cell forwards 0.5s ease-in-out;
  }
`;
export function Board({ board, onCellClicked, Iam }: BoardProps) {
  function renderCell(cell: CellState) {
    if (cell == "NONE") {
      return null;
    }
    if (cell == "X") {
      return <X />;
    }
    if (cell == "O") {
      return <O />;
    }
  }
  function getCellBackgroundColor(cell: CellState): ThemeColors {
    if (Iam == "X" && cell == "X") {
      return "secondary";
    }
    if (Iam == "X" && cell == "O") {
      return "tertiary";
    }
    if (Iam == "O" && cell == "O") {
      return "secondary";
    }
    if (Iam == "O" && cell == "X") {
      return "tertiary";
    }
    return "background";
  }
  return (
    <StyledBoard>
      {board.map((row, r) => {
        return row.map((cell, c) => {
          return (
            <StyledCell
              $backgroundColor={getCellBackgroundColor(cell)}
              key={c + r}
              onClick={() => onCellClicked(r, c)}
            >
              {renderCell(cell)}
            </StyledCell>
          );
        });
      })}
    </StyledBoard>
  );
}

interface BoardProps {
  board: CellState[][];
  onCellClicked: (row: number, column: number) => void;
  Iam: CellState;
}
