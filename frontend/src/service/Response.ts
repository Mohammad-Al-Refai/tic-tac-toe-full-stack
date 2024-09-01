export type GameActionResponseType =
  | "ERROR"
  | "CONNECTED"
  | "GAME_CREATED"
  | "JOINED_GAME"
  | "UPDATE_GAME"
  | "NEW_PLAYER_JOINED"
  | "PLAYER_QUIT"
  | "AVAILABLE_GAMES"
  | "DRAW"
  | "WIN";
export type CellState = "NONE" | "X" | "O";
export interface GameResponse {
  action: GameActionResponseType;
}
interface Game {
  createdAt: string;
  currentCellType: CellState;
  id: string;
  isDone: false;
  isPrivate: false;
  player1Name: string;
  player2Name: string | null;
  playerId1: string;
  playerId2: string | null;
  playerIdTurn: CellState | null;
  board: CellState[][];
}
export interface ServerError extends GameResponse {
  action: "ERROR";
  errorMessage: string;
}
export interface IPlayerConnected extends GameResponse {
  action: "CONNECTED";
  clientId: string;
  name: string;
}
export interface IGameCreated extends GameResponse {
  action: "GAME_CREATED";
  game: Game;
}
export interface IGameWin extends GameResponse {
  action: "WIN";
  gameId: string;
  playerId: string;
  winner: CellState;
}
export interface IAvailableGames {
  games: { id: string; name: string }[];
}
export interface IJoinedGame extends GameResponse {
  action: "JOINED_GAME";
  game: Game;
}

export interface IUpdateGame extends GameResponse {
  action: "UPDATE_GAME";
  gameId: string;
  playerIdTurn: string;
  turn: CellState;
  board: CellState[][];
}
export interface INewPlayerJoinedGame extends GameResponse {
  action: "NEW_PLAYER_JOINED";
  game: Game;
  playerId: string;
  playerName: string;
}
export interface IPlayerQuit extends GameResponse {
  action: "PLAYER_QUIT";
  gameId: string;
  playerId: string;
  playerName: string;
}
