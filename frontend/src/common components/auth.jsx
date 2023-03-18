import { GoogleLogin, GoogleOAuthProvider } from "@react-oauth/google";
import { useDispatch, useSelector } from "react-redux";
import { authSliceActions } from "../redux/slices/authSlice";

const Auth = () => {
  const dispatch = useDispatch();
  const isLoggedIn = useSelector((state) => state.auth.isLoggedIn);

  const handleLogin = (credentialResponse) => {
    dispatch(authSliceActions.login());
  };

  const handleLogout = () => {
    dispatch(authSliceActions.logout());
  };

  return (
    <>
      {!isLoggedIn ? (
        <GoogleOAuthProvider
          clientId={process.env.REACT_APP_GOOGLE_CLIENT_ID}
          cookiePolicy={"single_host_origin"}
        >
          <GoogleLogin
            onSuccess={handleLogin}
            onFailure={() => console.log("Login Failed")}
          />
        </GoogleOAuthProvider>
      ) : (
        <button onClick={handleLogout}>Logout</button>
      )}
    </>
  );
};

export default Auth;
