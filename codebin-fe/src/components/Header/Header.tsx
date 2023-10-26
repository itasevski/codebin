import { Link } from "react-router-dom";
import { GoCodescan } from "react-icons/go";
import { isUserAuthenticated } from "../../services/userServices";

const Header = () => {
  return (
    <nav className="bg-blue-800 p-4">
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
            <Link to="#" className="hover:text-blue-100">
              Logout
            </Link>
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
