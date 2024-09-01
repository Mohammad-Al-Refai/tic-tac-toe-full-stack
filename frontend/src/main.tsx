import ReactDOM from "react-dom/client";
import "./core.css";
import "./App.css";
import App from "./App.tsx";
import { BrowserRouter } from "react-router-dom";
import { Provider } from "react-redux";
import store from "./store/store.ts";
import { ThemeProvider } from "styled-components";
import { AppTheme } from "./UI/theme/AppTheme.ts";
import { GlobalStyle } from "./UI/theme/GlobalCSS.ts";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
ReactDOM.createRoot(document.getElementById("root")!).render(
  <BrowserRouter>
    <Provider store={store}>
      <ThemeProvider theme={AppTheme}>
        <GlobalStyle />
        <App />
        <ToastContainer />
      </ThemeProvider>
    </Provider>
  </BrowserRouter>
);
