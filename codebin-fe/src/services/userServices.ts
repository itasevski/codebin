import { getStorageItem, storageItemExists } from "./storageServices";

export const isUserAuthenticated = () =>
  storageItemExists("isAuthenticated") && getStorageItem("isAuthenticated");
