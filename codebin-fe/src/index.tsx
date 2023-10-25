import "./index.css";
import { createRoot } from "react-dom/client";
import App from "./components/App/App";
import { Provider } from "react-redux";
import { store } from "./store";

// When working with typescript React:
// 1. Install typescript as a dependency
// 2. Change language mode (bottom right) to TypeScript JSX
// 3. Add tsconfig.json to root directory

const root = createRoot(document.getElementById("root") as HTMLElement);
root.render(
  <Provider store={store}>
    <App />
  </Provider>
);
