import { BrowserRouter, Route, Routes } from "react-router-dom";
import Header from "../Header/Header";
import NotFound from "../NotFound/NotFound";
import Dashboard from "../Dashboard/Dashboard";
import Home from "../Home/Home";
import Login from "../Login/Login";
import Register from "../Register/Register";
import PrivateRoute from "../../util/route/PrivateRoute";
import { fetchUserData, getCsrfToken } from "../../services/codebinServices";
import { useEffect } from "react";
import { setStorageItem } from "../../services/storageServices";
import { useDispatch } from "react-redux";
import { fetchCurrentUser } from "../../store";

const App = () => {
  // TODO create fetchUser hook and invoke here to determine whether user is authenticated by checking the access token
  // ... generate CSRF token afterwards
  const dispatch = useDispatch<any>();

  useEffect(() => {
    fetchCsrfToken();
  }, []);

  const fetchCsrfToken = async () => {
    const { data } = await getCsrfToken();
    setStorageItem("csrf_token", data);

    dispatch(fetchCurrentUser())
      .unwrap()
      .catch(() => console.log("user is not authenticated"));
  };

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
