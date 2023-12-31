import { Link } from "react-router-dom";
import { GoCodescan } from "react-icons/go";
import { isUserAuthenticated } from "../../services/userServices";
import { useThunk } from "../../hooks/useThunk";
import { fetchCurrentUser, userLogout } from "../../store";
import Spinner from "../Spinner/Spinner";
import ErrorAlert from "../ErrorAlert/ErrorAlert";
import { useEffect } from "react";
import { useDispatch } from "react-redux";

const Header = () => {
  const dispatch = useDispatch<any>();
  const [logout, loading, error] = useThunk(userLogout, "/");

  const handleLogout = () => {
    logout(undefined);
  };

  useEffect(() => {
    if (isUserAuthenticated())
      dispatch(fetchCurrentUser())
        .unwrap()
        .catch(() => handleLogout()); // log user out if Axios response interceptor returns an error (Promise.reject)
  }, [dispatch]);

  return (
    <nav className="bg-blue-800 p-4">
      {error && (
        <ErrorAlert
          status={error.status}
          message={error.message || "Forbidden"}
          timestamp={error.timestamp || new Date().toString()}
        />
      )}
      <div className="flex justify-between mx-0 md:mx-16">
        <Link to="/">
          <div className="flex my-auto space-x-3 font-bold text-4xl text-white">
            <span>
              <GoCodescan />
            </span>{" "}
            <span>CodeBin</span>
          </div>
        </Link>
        <ul className="flex space-x-3 md:space-x-9 font-semibold text-2xl text-white my-auto">
          {isUserAuthenticated() ? (
            <button
              type="submit"
              className="hover:text-blue-100"
              onClick={handleLogout}
            >
              {loading ? <Spinner /> : "Logout"}
            </button>
          ) : (
            <>
              <Link to="/login" className="hover:text-blue-100">
                Login
              </Link>
              <Link to="/register" className="hover:text-blue-100">
                Register
              </Link>
            </>
          )}
        </ul>
      </div>
    </nav>
  );
};

export default Header;
