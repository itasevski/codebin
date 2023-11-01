import { BrowserRouter, Route, Routes } from "react-router-dom";
import Header from "../Header/Header";
import NotFound from "../NotFound/NotFound";
import Dashboard from "../Dashboard/Dashboard";
import Home from "../Home/Home";
import Login from "../Login/Login";
import Register from "../Register/Register";
import PrivateRoute from "../../util/route/PrivateRoute";

const App = () => {
  return (
    <BrowserRouter>
      <Header />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" Component={Login} />
        <Route path="/register" Component={Register} />
        <Route
          path="/dashboard"
          element={
            <PrivateRoute redirectTo="/login">
              <Dashboard />
            </PrivateRoute>
          }
        />
        <Route path="*" Component={NotFound} />
      </Routes>
    </BrowserRouter>
  );
};

export default App;
