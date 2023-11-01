import { useSelector } from "react-redux";
import Spinner from "../Spinner/Spinner";

const Dashboard = () => {
  const { user } = useSelector((state: any) => state.auth);

  return (
    <div className="grid place-items-center py-12">
      {user ? (
        <div className="text-5xl font-semibold">Welcome, {user.username}.</div>
      ) : (
        <Spinner />
      )}
    </div>
  );
};

export default Dashboard;
