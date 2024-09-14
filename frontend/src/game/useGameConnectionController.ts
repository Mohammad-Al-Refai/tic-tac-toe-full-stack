import { useEffect, useState } from "react";
import {
  GameActionResponseType,
  IAvailableGames,
  IGameCreated,
  IJoinedGame,
  INewPlayerJoinedGame,
  IPlayerConnected,
  ServerError,
  IUpdateGame,
  IGameWin,
  IPlayerQuit,
} from "../service/Response";
import useWebSocket from "react-use-websocket";
import {
  GameActionRequestType,
  CreateGamePayload,
  UpdateGamePayload,
  JoinGamePayload,
  GetAvailableGamesPayload,
} from "../service/Request";
import { toast } from "react-toastify";
import React from "react";
import { useNavigate } from "react-router-dom";
const actionsStack = new Map<string, GameActionRequestType>();
export function useGameConnectionController({
  onResponseConnected,
  onResponseGameCreated,
  onResponseRetrieveAvailableGames,
  onResponseJoinedGame,
  onResponseUpdateGame,
  onResponseNewPlayerJoinedGame,
  onResponseOpponentQuit,
  onResponseWin,
  onResponseDraw,
}: useGameConnectionControllerProps) {
  const [isConnected, setIsConnected] = useState(false);
  const [isSending, setIsSending] = useState(false);
  const navigate = useNavigate();
  const toastId = React.useRef<null | ReturnType<typeof toast>>(null);
  const { lastJsonMessage, sendJsonMessage } = useWebSocket(
    import.meta.env.VITE_APP_API,
    {
      reconnectAttempts: 3,
      onClose() {
        setIsConnected(false);
      },
    }
  );
  useEffect(() => {
    if (!lastJsonMessage) {
      return;
    }
    switch (lastJsonMessage.action as GameActionResponseType) {
      case "CONNECTED":
        onResponseConnected(lastJsonMessage);
        break;
      case "GAME_CREATED":
        onResponseGameCreated(lastJsonMessage);
        popStack(lastJsonMessage.requestId);
        break;
      case "AVAILABLE_GAMES":
        onResponseRetrieveAvailableGames(lastJsonMessage);
        popStack(lastJsonMessage.requestId);
        break;
      case "JOINED_GAME":
        onResponseJoinedGame(lastJsonMessage);
        popStack(lastJsonMessage.requestId);
        break;
      case "UPDATE_GAME":
        onResponseUpdateGame(lastJsonMessage);
        popStack(lastJsonMessage.requestId);
        break;
      case "NEW_PLAYER_JOINED":
        onResponseNewPlayerJoinedGame(lastJsonMessage);
        break;
      case "PLAYER_QUIT":
        onResponseOpponentQuit(lastJsonMessage);
        popStack(lastJsonMessage.requestId);
        break;
      case "WIN":
        onResponseWin(lastJsonMessage);
        break;
      case "DRAW":
        onResponseDraw();
        break;
      case "ERROR":
        onResponseError(lastJsonMessage);
        popStack(lastJsonMessage.requestId);
        break;
      default:
        break;
    }
  }, [lastJsonMessage]);
  function sendCellUpdate(payload: ReturnType<typeof UpdateGamePayload>) {
    pushToStack(payload.action, payload.requestId);
    setIsSending(true);
    sendJsonMessage(payload);
  }
  function sendJoin(payload: ReturnType<typeof JoinGamePayload>) {
    pushToStack(payload.action, payload.requestId);
    setIsSending(true);
    sendJsonMessage(payload);
  }
  function sendCreateGame(payload: ReturnType<typeof CreateGamePayload>) {
    pushToStack(payload.action, payload.requestId);
    setIsSending(true);
    sendJsonMessage(payload);
  }
  function sendQuitGame(payload: ReturnType<typeof CreateGamePayload>) {
    pushToStack(payload.action, payload.requestId);
    setIsSending(true);
    sendJsonMessage(payload);
  }
  function sendGetIAvailableGames(
    payload: ReturnType<typeof GetAvailableGamesPayload>
  ) {
    pushToStack(payload.action, payload.requestId);
    setIsSending(true);
    sendJsonMessage(payload);
  }
  function onResponseError(response: ServerError) {
    if (response.errorMessage == "Game is not exist") {
      navigate("/", { replace: true });
    }
    toast(`Server Error: ${response.errorMessage}`, {
      type: "error",
    });
  }
  function pushToStack(actionType: GameActionRequestType, requestId: string) {
    switch (actionType) {
      case "CREATE_GAME":
        actionsStack.set(requestId, "CREATE_GAME");
        break;
      case "GET_AVAILABLE_GAMES":
        actionsStack.set(requestId, "GET_AVAILABLE_GAMES");
        break;
      case "JOIN_GAME":
        actionsStack.set(requestId, "JOIN_GAME");
        break;
      case "UPDATE_GAME":
        actionsStack.set(requestId, "UPDATE_GAME");
        break;
      case "QUIT_GAME":
        actionsStack.set(requestId, "QUIT_GAME");
        break;
      default:
        break;
    }
  }
  function popStack(requestId: string) {
    actionsStack.delete(requestId);
    setIsSending(false);
    // console.log(requestId, "=> DELETED");
  }
  return {
    isConnected,
    sendCellUpdate,
    sendJoin,
    sendCreateGame,
    sendGetIAvailableGames,
    sendQuitGame,
    isSending,
    toastId,
  };
}

interface useGameConnectionControllerProps {
  onResponseConnected: (response: IPlayerConnected) => void;
  onResponseGameCreated: (response: IGameCreated) => void;
  onResponseRetrieveAvailableGames: (response: IAvailableGames) => void;
  onResponseJoinedGame: (response: IJoinedGame) => void;
  onResponseUpdateGame: (response: IUpdateGame) => void;
  onResponseNewPlayerJoinedGame: (response: INewPlayerJoinedGame) => void;
  onResponseOpponentQuit: (response: IPlayerQuit) => void;
  onResponseWin: (response: IGameWin) => void;
  onResponseDraw: () => void;
}
