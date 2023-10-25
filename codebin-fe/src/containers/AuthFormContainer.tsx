import { GoAlertFill, GoInfo, GoX } from "react-icons/go";
import { useSelector } from "react-redux";

interface AuthContainerProps {
  formTitle: string;
  children: any;
  errors: string[];
  successMessage?: string;
}

const AuthContainer = ({
  formTitle,
  children,
  errors,
  successMessage,
}: AuthContainerProps) => {
  const { error } = useSelector((state: any) => state.auth);

  return (
    <div className="bg-gray-50 min-h-screen">
      <div className="grid place-items-center py-12">
        <div className="w-full bg-white rounded-lg shadow md:mt-0 sm:max-w-lg xl:p-0">
          <div className="p-6 space-y-8 md:space-y-10 sm:p-8">
            {successMessage && (
              <div className="bg-green-100 rounded-lg p-4 text-sm md:text-lg flex">
                <GoInfo className="text-2xl text-green-800 my-auto" />
                <p className="ml-3 font-semibold text-green-800">
                  {successMessage}
                </p>
              </div>
            )}
            {(errors.length || error) && (
              <div className="bg-red-100 rounded-lg p-4 text-sm md:text-lg">
                <GoAlertFill className="text-2xl text-red-800 mb-2" />
                <p className="font-bold mb-1 text-lg text-red-500">
                  Validation failed. Reason:
                </p>
                {errors.map((err: any) => (
                  <p key={err} className="flex font-semibold text-red-500">
                    <div className="my-auto mr-2">
                      <GoX />
                    </div>
                    <div>{err}</div>
                  </p>
                ))}
                {error && (
                  <div className="flex font-semibold text-red-500">
                    <div className="my-auto mr-2">
                      <GoX />
                    </div>
                    <div>{`${error.status}: ${error.message}`}</div>
                  </div>
                )}
              </div>
            )}
            <div className="text-xl font-bold leading-tight tracking-tight text-gray-900 md:text-3xl">
              {formTitle}
            </div>
            {children}
          </div>
        </div>
      </div>
    </div>
  );
};

export default AuthContainer;
