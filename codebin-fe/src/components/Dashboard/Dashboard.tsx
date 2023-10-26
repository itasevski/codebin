import { useSelector } from "react-redux";

const Dashboard = () => {
  const { user } = useSelector((state: any) => state.auth);

  return (
    <div className="grid place-items-center py-12">
      {user ? (
        <div className="text-5xl font-semibold">Welcome, {user.username}.</div>
      ) : (
        <div className="w-6 h-6 border-t-4 border-blue-500 border-solid rounded-full animate-spin my-auto" />
      )}
    </div>
  );
};

export default Dashboard;
