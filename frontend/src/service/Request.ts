export enum ActionRequest {
  CREATE_GAME,
  JOIN_GAME,
  UPDATE_GAME,
  GET_AVAILABLE_GAMES,
  QUIT_GAME,
}
export type GameActionRequestType =
  | "CREATE_GAME"
  | "JOIN_GAME"
  | "UPDATE_GAME"
  | "GET_AVAILABLE_GAMES"
  | "QUIT_GAME"
  | "NONE";
export type CellIndex =
  | "Cell1"
  | "Cell2"
  | "Cell3"
  | "Cell4"
  | "Cell5"
  | "Cell6"
  | "Cell7"
  | "Cell8"
  | "Cell9";

function ActionRequestToString(action: ActionRequest): GameActionRequestType {
  switch (action) {
    case ActionRequest.CREATE_GAME:
      return "CREATE_GAME";
    case ActionRequest.JOIN_GAME:
      return "JOIN_GAME";
    case ActionRequest.UPDATE_GAME:
      return "UPDATE_GAME";
    case ActionRequest.GET_AVAILABLE_GAMES:
      return "GET_AVAILABLE_GAMES";
    case ActionRequest.QUIT_GAME:
      return "QUIT_GAME";
    default:
      return "NONE";
  }
}
export function CreateGamePayload(clientId: string) {
  return {
    action: ActionRequestToString(ActionRequest.CREATE_GAME),
    requestId: generateId(),
    clientId,
  };
}
export function JoinGamePayload(clientId: string, gameId: string) {
  return {
    action: ActionRequestToString(ActionRequest.JOIN_GAME),
    clientId,
    requestId: generateId(),
    gameId,
  };
}
export function QuitGamePayload(clientId: string, gameId: string) {
  return {
    action: ActionRequestToString(ActionRequest.QUIT_GAME),
    clientId,
    requestId: generateId(),
    gameId,
  };
}
export function UpdateGamePayload(
  clientId: string,
  gameId: string,
  row: number,
  column: number
) {
  return {
    action: ActionRequestToString(ActionRequest.UPDATE_GAME),
    clientId,
    gameId,
    row,
    requestId: generateId(),
    column,
  };
}
export function GetAvailableGamesPayload(clientId: string) {
  return {
    action: ActionRequestToString(ActionRequest.GET_AVAILABLE_GAMES),
    clientId,
    requestId: generateId(),
  };
}

function generateId() {
  return Math.random().toString(36).substr(2, 9);
}
