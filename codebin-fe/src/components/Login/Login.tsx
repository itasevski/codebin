import { useState, useRef, useEffect } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import AuthContainer from "../../containers/AuthFormContainer";
import { clearError, userLogin } from "../../store/";
import { useDispatch, useSelector } from "react-redux";

const Login = () => {
  const location = useLocation(); // object used to access history state set by "useNavigate"
  const navigate = useNavigate();

  const { isLoading, error } = useSelector((state: any) => state.auth);
  const dispatch = useDispatch<any>();

  const usernameRef = useRef<HTMLInputElement>(null);

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const [errors, setErrors] = useState<string[]>([]); // form validation level errors

  useEffect(() => {
    usernameRef.current?.focus();
  }, []);

  useEffect(() => {
    if (error) dispatch(clearError(undefined));
    setErrors([]);
  }, [username, password]);

  const formSubmit = (event: any) => {
    event.preventDefault();

    if (location.state?.message) location.state = {}; // remove success message after login attempt

    if (!username || !password) {
      let errors = [];
      !username && errors.push("Please provide username");
      !password && errors.push("Please provide password");
      setErrors(errors);
      return;
    }

    dispatch(userLogin({ username, password }))
      .unwrap()
      .then(() => navigate("/dashboard")) // if successful, navigate to "/dashboard"
      .catch((err: any) =>
        console.error(`${err.status}: ${err.message} (${err.timestamp})`)
      );
    // if error is thrown, "rejected" action is dispatched by async thunk and error is also caught here
  };

  return (
    <AuthContainer
      formTitle="Sign in to your account"
      errors={errors}
      successMessage={location.state?.message}
    >
      <form className="space-y-4 md:space-y-6" onSubmit={formSubmit}>
        <div>
          <label
            htmlFor="username"
            className="block mb-2 text-sm md:text-xl font-medium text-gray-900"
          >
            Username
          </label>
          <input
            ref={usernameRef}
            type="text"
            name="username"
            id="username"
            className="bg-gray-50 border border-gray-300 text-gray-900 text-sm md:text-lg rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5"
            placeholder="username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
        </div>
        <div>
          <label
            htmlFor="password"
            className="block mb-2 text-sm md:text-xl font-medium text-gray-900"
          >
            Password
          </label>
          <input
            type="password"
            name="password"
            id="password"
            className="bg-gray-50 border border-gray-300 text-gray-900 text-sm md:text-lg rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5"
            placeholder="••••••••"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
        </div>
        <div className="flex items-center justify-between">
          <div className="flex items-start">
            <div className="flex items-center h-5 my-auto">
              <input
                id="remember"
                aria-describedby="remember"
                type="checkbox"
                className="w-4 h-4 border border-gray-300 rounded bg-gray-50 focus:ring-3 focus:ring-primary-300 "
              />
            </div>
            <div className="ml-3 text-sm md:text-lg">
              <label htmlFor="remember" className="text-gray-500">
                Remember me
              </label>
            </div>
          </div>
          <a
            href="#"
            className="text-sm font-medium text-primary-600 hover:underline my-auto"
          >
            Forgot password?
          </a>
        </div>
        <button type="submit" className="btn btn-primary">
          {isLoading ? (
            <div className="w-6 h-6 border-t-4 border-blue-500 border-solid rounded-full animate-spin my-auto" />
          ) : (
            "Login"
          )}
        </button>
        <p className="text-sm font-light text-gray-500">
          Don’t have an account yet?{" "}
          <Link
            to="/register"
            onClick={() => dispatch(clearError(undefined))}
            className="font-medium text-primary-600 hover:underline"
          >
            Sign up
          </Link>
        </p>
      </form>
    </AuthContainer>
  );
};

export default Login;
