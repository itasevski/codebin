import { useCallback, useState } from "react";
import { useDispatch } from "react-redux";
import { ErrorStructure } from "../types/types";
import { useNavigate } from "react-router-dom";

/**
 * Reusable hook used for async thunks. Used when we want separate state for multiple items on a given page.
 * Example: List of items that can be expanded. When a given item is expanded, a request is made to BE to fetch data. In this case, we use
 * this hook in order to have separate loading and error states for every one of those items.
 * @param thunk the thunk we want to execute
 * @param redirectUrl the URL we want to redirect to after successful logout
 * @returns runThunk hook, loading boolean and error object
 */
export function useThunk(
  thunk: any,
  redirectUrl: string
): [(arg: any) => void, boolean, ErrorStructure | null] {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const runThunk = useCallback(
    (arg: any) => {
      setLoading(true);
      dispatch(thunk(arg))
        .unwrap() // unwrap Promise returned from dispatching the thunk
        .then(() => navigate(redirectUrl))
        .catch((err: any) => setError(err)) // catch error if present (catches our error payload, returned with rejectWithValue)
        .finally(() => setLoading(false));
    },
    [dispatch, navigate, redirectUrl, thunk]
  );

  return [runThunk, loading, error];
}
