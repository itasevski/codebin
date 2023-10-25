import { useCallback, useState } from "react";
import { useDispatch } from "react-redux";
import { ErrorStructure } from "../types/types";

/**
 * Reusable hook used for async thunks. Used when we want separate state for multiple items on a given page.
 * Example: List of items that can be expanded. When a given item is expanded, a request is made to BE to fetch data. In this case, we use
 * this hook in order to have separate loading and error states for every one of those items.
 * @param thunk
 * @returns
 */
export function useThunk(
  thunk: any
): [(arg: any) => void, boolean, ErrorStructure | null] {
  const dispatch = useDispatch();

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const runThunk = useCallback((arg: any) => {
    setLoading(true);
    dispatch(thunk(arg))
      .unwrap() // unwrap Promise returned from dispatching the thunk
      .catch((err: any) => setError(err)) // catch error if present (catches our error payload, returned with rejectWithValue)
      .finally(() => setLoading(false));
  }, []);

  return [runThunk, loading, error];
}
