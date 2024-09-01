import { Route, Routes } from "react-router-dom";
import { Game } from "./game";
import { PlayPage } from "./UI/pages/Play";
import { useGameViewModel } from "./game/gameViewModel";
import { LandingPage } from "./UI/pages/Landing";

function App() {
  const vm = useGameViewModel();

  return (
    <Routes>
      <Route path="/" element={<Game />}>
        <Route
          path="/"
          element={
            <LandingPage
              onCreateClicked={vm.onCreateGameClicked}
              availableGames={vm.availableGames}
              onRefresh={vm.onRefresh}
            />
          }
        />
        <Route
          path="/play/:gameId"
          element={
            <PlayPage
              onCellClicked={vm.onCellClicked}
              join={vm.onJoinClicked}
              quitGame={vm.quitGame}
            />
          }
        />
      </Route>
    </Routes>
  );
}

export default App;
