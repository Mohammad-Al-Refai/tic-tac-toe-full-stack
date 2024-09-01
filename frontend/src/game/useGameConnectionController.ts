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
  const [actionsStack, setActionsStack] = useState(
    new Map<GameActionResponseType, GameActionRequestType>()
  );
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
    console.log(actionsStack);
  }, [actionsStack]);
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
        popStack("GAME_CREATED");
        break;
      case "AVAILABLE_GAMES":
        onResponseRetrieveAvailableGames(lastJsonMessage);
        popStack("AVAILABLE_GAMES");
        break;
      case "JOINED_GAME":
        onResponseJoinedGame(lastJsonMessage);
        popStack("JOINED_GAME");
        break;
      case "UPDATE_GAME":
        onResponseUpdateGame(lastJsonMessage);
        popStack("UPDATE_GAME");
        break;
      case "NEW_PLAYER_JOINED":
        onResponseNewPlayerJoinedGame(lastJsonMessage);
        break;
      case "PLAYER_QUIT":
        onResponseOpponentQuit(lastJsonMessage);
        break;
      case "WIN":
        onResponseWin(lastJsonMessage);
        break;
      case "DRAW":
        onResponseDraw();
        break;
      case "ERROR":
        onResponseError(lastJsonMessage);
        break;
      default:
        break;
    }
  }, [lastJsonMessage]);
  function sendCellUpdate(payload: ReturnType<typeof UpdateGamePayload>) {
    pushToStack(payload.action);
    setIsSending(true);
    sendJsonMessage(payload);
  }
  function sendJoin(payload: ReturnType<typeof JoinGamePayload>) {
    pushToStack(payload.action);
    setIsSending(true);
    sendJsonMessage(payload);
  }
  function sendCreateGame(payload: ReturnType<typeof CreateGamePayload>) {
    pushToStack(payload.action);
    setIsSending(true);
    sendJsonMessage(payload);
  }
  function sendQuitGame(payload: ReturnType<typeof CreateGamePayload>) {
    pushToStack(payload.action);
    setIsSending(true);
    sendJsonMessage(payload);
  }
  function sendGetIAvailableGames(
    payload: ReturnType<typeof GetAvailableGamesPayload>
  ) {
    pushToStack(payload.action);
    setIsSending(true);
    sendJsonMessage(payload);
  }
  function onResponseError(response: ServerError) {
    if ((response.errorMessage = "Game is not exist")) {
      navigate("/", { replace: true });
    }
    toast(`Server Error: ${response.errorMessage}`, {
      type: "error",
    });
  }
  function pushToStack(actionType: GameActionRequestType) {
    switch (actionType) {
      case "CREATE_GAME":
        actionsStack.set("GAME_CREATED", "CREATE_GAME");
        break;
      case "GET_AVAILABLE_GAMES":
        actionsStack.set("AVAILABLE_GAMES", "GET_AVAILABLE_GAMES");

        break;
      case "JOIN_GAME":
        actionsStack.set("JOINED_GAME", "JOIN_GAME");

        break;
      case "UPDATE_GAME":
        actionsStack.set("UPDATE_GAME", "UPDATE_GAME");
        break;
      default:
        break;
    }
    setActionsStack(new Map(actionsStack));
  }
  function popStack(actionType: GameActionResponseType) {
    actionsStack.delete(actionType);
    setActionsStack(new Map(actionsStack));
    setIsSending(false);
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
