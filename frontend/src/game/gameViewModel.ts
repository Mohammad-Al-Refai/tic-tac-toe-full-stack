import { useState } from "react";
import {
  CreateGamePayload,
  GetAvailableGamesPayload,
  JoinGamePayload,
  QuitGamePayload,
  UpdateGamePayload,
} from "../service/Request";
import {
  IPlayerConnected,
  IGameCreated,
  IJoinedGame,
  IUpdateGame,
  INewPlayerJoinedGame,
  IPlayerQuit,
  IAvailableGames,
  IGameWin,
} from "../service/Response";
import { ProfileSliceActions } from "../store/ProfileSlice";
import { useAppDispatch } from "../store/store";
import { GameSliceActions } from "../store/GameSlice";
import { useProfile } from "../hooks/useProfile";
import { useGame } from "../hooks/useGame";
import { toast } from "react-toastify";
import { useGameConnectionController } from "./useGameConnectionController";
import { useNavigate } from "react-router-dom";

export function useGameViewModel() {
  const dispatch = useAppDispatch();
  const profile = useProfile();
  const game = useGame();
  const [isConnected, setIsConnected] = useState(false);
  const [gameIdToJoin, setGameIdToJoin] = useState("");
  const [availableGames, setAvailableGames] = useState<
    { id: string; name: string }[]
  >([]);
  const navigate = useNavigate();
  const gameConnectionController = useGameConnectionController({
    onResponseConnected: onConnected,
    onResponseGameCreated: onGameCreated,
    onResponseRetrieveAvailableGames: onRetrieveAvailableGames,
    onResponseJoinedGame: onJoinedGame,
    onResponseUpdateGame: onUpdateGame,
    onResponseNewPlayerJoinedGame: onNewPlayerJoinedGame,
    onResponseOpponentQuit: onOpponentQuit,
    onResponseWin: onWin,
    onResponseDraw: onDraw,
  });

  function onConnected(response: IPlayerConnected) {
    setIsConnected(true);
    dispatch(
      ProfileSliceActions.setProfile({
        id: response.clientId,
        name: response.name,
      })
    );
    refreshAvailableGames(response.clientId);
  }
  function onGameCreated(response: IGameCreated) {
    navigate(`/play/${response.game.id}`, { replace: true });
  }
  function onOpponentQuit(response: IPlayerQuit) {
    toast(`${response.playerName} quit the game`, {
      type: "warning",
    });

    dispatch(
      GameSliceActions.setOpponent({
        id: "",
        name: "",
      })
    );
    navigate(`/`, { replace: true });
  }
  function quitGame() {
    gameConnectionController.sendQuitGame(
      QuitGamePayload(profile.id, game.gameId)
    );
    navigate(`/`, { replace: true });
  }
  function onNewPlayerJoinedGame(response: INewPlayerJoinedGame) {
    dispatch(
      GameSliceActions.setOpponent({
        id: response.playerId,
        name: response.playerName,
      })
    );
    toast(`${response.playerName} joined game`, { type: "info" });
  }
  function onJoinedGame(response: IJoinedGame) {
    if (response.game.isDone) {
      toast(`Game finished`, { type: "info" });
      navigate(`/`, { replace: true });
    }
    if (response.game.playerId1 == profile.id) {
      dispatch(
        GameSliceActions.setOpponent({
          name: response.game.player2Name ?? "",
          id: response.game.playerId2 ?? "",
        })
      );
    } else {
      dispatch(
        GameSliceActions.setOpponent({
          name: response.game.player1Name ?? "",
          id: response.game.playerId1 ?? "",
        })
      );
    }
    dispatch(GameSliceActions.setBoard(response.game.board));

    dispatch(GameSliceActions.setIsJoinedGame(true));
    if (response.game.playerId1 == profile.id) {
      dispatch(GameSliceActions.setIam("X"));
    }
    if (response.game.playerId2 == profile.id) {
      dispatch(GameSliceActions.setIam("O"));
    }
    dispatch(GameSliceActions.setGameId(response.game.id));
    dispatch(GameSliceActions.setTurn(response.game.currentCellType));
  }
  function refreshAvailableGames(id: string) {
    gameConnectionController.sendGetIAvailableGames(
      GetAvailableGamesPayload(id)
    );
  }
  function onUpdateGame(response: IUpdateGame) {
    dispatch(GameSliceActions.setTurn(response.turn));
    dispatch(GameSliceActions.setBoard(response.board));
  }
  function onWin(response: IGameWin) {
    dispatch(
      GameSliceActions.setWinner({
        id: response.playerId,
        name: "",
        type: response.winner,
      })
    );
  }

  function onDraw() {
    toast(`Draw`, { type: "info" });
    navigate("/");
  }
  function onCellClicked(row: number, column: number) {
    gameConnectionController.sendCellUpdate(
      UpdateGamePayload(profile.id, game.gameId, row, column)
    );
  }
  function onJoinClicked(gameId: string) {
    gameConnectionController.sendJoin(JoinGamePayload(profile.id, gameId));
    dispatch(GameSliceActions.setGameId(gameId));
  }
  function onGameIdChange(id: string) {
    setGameIdToJoin(id);
  }
  function onCreateGameClicked() {
    gameConnectionController.sendCreateGame(CreateGamePayload(profile.id));
  }
  function onRetrieveAvailableGames(response: IAvailableGames) {
    setAvailableGames(response.games);
  }
  function onRefresh() {
    refreshAvailableGames(profile.id);
  }
  return {
    board: game.board,
    isConnected,
    onCellClicked,
    gameId: game.gameId,
    gameIdToJoin,
    onJoinClicked,
    onGameIdChange,
    onCreateGameClicked,
    turn: game.turn,
    Iam: game.Iam,
    availableGames,
    onRefresh,
    quitGame,
  };
}
