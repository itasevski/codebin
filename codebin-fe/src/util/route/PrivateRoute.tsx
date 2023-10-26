import { Navigate } from "react-router-dom";
import { isUserAuthenticated } from "../../services/userServices";

interface PrivateRouteProps {
  redirectTo: string;
  children: any;
}

const PrivateRoute = ({ redirectTo, children }: PrivateRouteProps) => {
  if (!isUserAuthenticated()) return <Navigate to={redirectTo} />;

  return children;
};

export default PrivateRoute;
