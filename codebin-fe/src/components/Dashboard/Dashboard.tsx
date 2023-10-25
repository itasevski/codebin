import { useSelector } from "react-redux";
import { test } from "../../services/codebinServices";
import { getStorageItem } from "../../services/storageServices";

const Dashboard = () => {
  console.log(getStorageItem("csrf_token"));
  test().then(({ data }) => console.log(data));

  const { user } = useSelector((state: any) => state.auth);

  return (
    <div className="grid place-items-center py-12">
      <div className="text-5xl font-semibold">Welcome, {user}.</div>
    </div>
  );
};

export default Dashboard;
