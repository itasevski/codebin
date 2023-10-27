import { GoAlertFill } from "react-icons/go";

interface ErrorAlertProps {
  status: number;
  message: string;
  timestamp: string;
}

const ErrorAlert = ({ status, message, timestamp }: ErrorAlertProps) => {
  return (
    <div
      className="p-4 mb-4 text-sm text-red-800 rounded-lg bg-red-50 dark:bg-gray-800 dark:text-red-400 flex"
      role="alert"
    >
      <GoAlertFill className="text-2xl text-red-800 mr-4 my-auto" />
      <span className="font-medium my-auto">{status}:</span> &nbsp;
      <span className="my-auto">
        {message} ({timestamp})
      </span>
    </div>
  );
};

export default ErrorAlert;
