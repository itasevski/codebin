import { Link, useNavigate } from "react-router-dom";
import AuthFormContainer from "../../containers/AuthFormContainer";
import { useEffect, useRef, useState } from "react";
import { PASSWORD_REGEX, USERNAME_REGEX } from "../../util/regexps";
import { GoInfo, GoX } from "react-icons/go";
import { register } from "../../services/codebinServices";
import Spinner from "../Spinner/Spinner";

const Register = () => {
  const navigate = useNavigate();

  const usernameRef = useRef<HTMLInputElement>(null);

  const [username, setUsername] = useState("");
  const [usernameValid, setUsernameValid] = useState(false);

  const [password, setPassword] = useState("");
  const [passwordValid, setPasswordValid] = useState(false);

  const [confirmPassword, setConfirmPassword] = useState("");

  const [passwordsMatch, setPasswordsMatch] = useState(false);

  const [errors, setErrors] = useState<string[]>([]);

  const [loading, setLoading] = useState(false);

  useEffect(() => {
    usernameRef.current?.focus(); // focus on username field
  }, []);

  useEffect(() => {
    setUsernameValid(USERNAME_REGEX.test(username));
  }, [username]);

  useEffect(() => {
    setPasswordValid(PASSWORD_REGEX.test(password));
    setPasswordsMatch(password === confirmPassword);
  }, [password, confirmPassword]);

  useEffect(() => {
    setErrors([]);
  }, [username, password, confirmPassword]);

  const formSubmit = async (event: any) => {
    event?.preventDefault();

    if (!usernameValid || !passwordValid || !passwordsMatch) {
      let errors = [];
      !usernameValid && errors.push("Invalid username");
      !passwordValid && errors.push("Invalid password");
      !passwordsMatch && errors.push("Passwords do not match");
      setErrors(errors);
      return;
    }

    try {
      setLoading(true);
      const response = await register(username, password, confirmPassword);
      if (response.status === 200)
        navigate("/login", { state: { message: response.data } });
      // state object is provided by react-router-dom to set history state that other components rendered by routes can access via
      // the "useLocation" hook
      // this state is deleted after Login component is unmounted
    } catch (err: any) {
      setErrors(
        err.response
          ? [err.response.status + ": " + err.response.data.message]
          : [
              "N/A: Network error. Check your internet connection or try again later.",
            ]
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <AuthFormContainer formTitle="Sign up" errors={errors}>
      <form className="space-y-4 md:space-y-6" onSubmit={formSubmit}>
        <div>
          <label
            htmlFor="username"
            className="block mb-2 text-sm md:text-xl font-medium text-gray-900 flex"
          >
            Username{" "}
            <GoX
              className={`my-auto text-red-500 ml-1 ${
                username && !usernameValid ? "block" : "hidden"
              }`}
            />
          </label>
          <input
            ref={usernameRef}
            type="text"
            name="username"
            id="username"
            className="bg-gray-50 border border-gray-300 text-gray-900 text-sm md:text-lg rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5"
            placeholder="ex. User123"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
          {username && !usernameValid && (
            <p className="bg-black text-white rounded-lg p-4 mt-1 font-semibold text-sm md:text-lg">
              <GoInfo className="mb-1" />
              4 to 24 characters.
              <br />
              Must begin with a letter.
              <br />
              Letters, numbers, underscores, hyphens allowed.
            </p>
          )}
        </div>
        <div>
          <label
            htmlFor="password"
            className="block mb-2 text-sm md:text-xl font-medium text-gray-900 flex"
          >
            Password{" "}
            <GoX
              className={`my-auto text-red-500 ml-1 ${
                password && !passwordValid ? "block" : "hidden"
              }`}
            />
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
          {password && !passwordValid && (
            <p className="bg-black text-white rounded-lg p-4 mt-1 font-semibold text-sm md:text-lg">
              <GoInfo className="mb-1" />
              8 to 24 characters.
              <br />
              Must include uppercase and lowercase letters, a number and a
              special character.
              <br />
              Allowed special characters:{" "}
              <span aria-label="exclamation mark">!</span>{" "}
              <span aria-label="at symbol">@</span>{" "}
              <span aria-label="hashtag">#</span>{" "}
              <span aria-label="dollar sign">$</span>{" "}
              <span aria-label="percent">%</span>
            </p>
          )}
        </div>
        <div>
          <label
            htmlFor="confirmPassword"
            className="block mb-2 text-sm md:text-xl font-medium text-gray-900 flex"
          >
            Confirm password{" "}
            <GoX
              className={`my-auto text-red-500 ml-1 ${
                confirmPassword && !passwordsMatch ? "block" : "hidden"
              }`}
            />
          </label>
          <input
            type="password"
            name="confirmPassword"
            id="confirmPassword"
            className="bg-gray-50 border border-gray-300 text-gray-900 text-sm md:text-lg rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5"
            placeholder="••••••••"
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
          />
          {confirmPassword && !passwordsMatch && (
            <p className="bg-black text-white rounded-lg p-4 mt-1 font-semibold text-sm md:text-lg">
              <GoInfo className="mb-1" />
              Passwords do not match.
            </p>
          )}
        </div>
        <button type="submit" className="btn btn-primary flex">
          {loading ? <Spinner /> : "Register"}
        </button>
        <p className="text-sm font-light text-gray-500">
          Already have an account?{" "}
          <Link
            to="/login"
            className="font-medium text-primary-600 hover:underline"
          >
            Log in
          </Link>
        </p>
      </form>
    </AuthFormContainer>
  );
};

export default Register;
