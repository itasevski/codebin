import { useSelector } from "react-redux";
import { test } from "../../services/codebinServices";
import Spinner from "../Spinner/Spinner";
import { useEffect } from "react";

const Dashboard = () => {
  useEffect(() => {
    test()
      .then(({ data }) => console.log(data))
      .catch((err) => console.log(err));
  }, []);

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
