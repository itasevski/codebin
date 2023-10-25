export const getStorageItem = (name: string) => localStorage.getItem(name);

export const setStorageItem = (name: string, value: any) =>
  localStorage.setItem(name, value);

export const removeStorageItem = (name: string) =>
  localStorage.removeItem(name);

export const storageItemExists = (name: string) =>
  getStorageItem(name) !== null;

// Differences between local and session storage
/**
 * Local storage
 * 1. 5-10Mb capacity, depending on browser
 * 2. Available on all tabs currently open on user's browser
 * 3. Never expires
 * Session storage
 * 1. 5Mb capacity
 * 2. Private to current tab on user's browser
 * 3. Expires on tab close
 * When to use which depends on the use case scenario.
 * This service is specific to our CSRF functionality. Since we need to keep the CSRF token available everywhere, we use localStorage.
 */
