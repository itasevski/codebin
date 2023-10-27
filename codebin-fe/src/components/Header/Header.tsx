import { Link } from "react-router-dom";
import { GoCodescan } from "react-icons/go";
import { isUserAuthenticated } from "../../services/userServices";
import { useThunk } from "../../hooks/useThunk";
import { userLogout } from "../../store";
import Spinner from "../Spinner/Spinner";
import ErrorAlert from "../ErrorAlert/ErrorAlert";

const Header = () => {
  const [logout, loading, error] = useThunk(userLogout, "/");

  const handleLogout = () => {
    logout(undefined);
  };

  return (
    <nav className="bg-blue-800 p-4">
      {error && (
        <ErrorAlert
          status={error.status}
          message={error.message}
          timestamp={error.timestamp}
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
