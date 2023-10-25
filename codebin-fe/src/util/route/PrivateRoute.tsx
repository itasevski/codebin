import { useSelector } from "react-redux";
import { Navigate } from "react-router-dom";

interface PrivateRouteProps {
  redirectTo: string;
  children: any;
}

const PrivateRoute = ({ redirectTo, children }: PrivateRouteProps) => {
  // if (!cookieExists("access-token")) return <Navigate to={redirectTo} />;

  return children;
};

export default PrivateRoute;
