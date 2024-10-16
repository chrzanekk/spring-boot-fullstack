import {createBrowserRouter} from "react-router-dom";
import Login from "../components/login/Login.jsx";
import Register from "../components/register/Register.jsx";
import ProtectedRoute from "../components/shared/ProtectedRoute.jsx";
import App from "../App.jsx";
import Profile from "../components/profile/Profile.jsx";

const router = createBrowserRouter([
    {
        path: "/",
        element: <Login/>
    },
    {
        path: "/register",
        element: <Register/>
    },
    {
        path: "dashboard",
        element: <ProtectedRoute>
            <App/>
        </ProtectedRoute>
    },
    {
        path: "profile",
        element: <ProtectedRoute>
            <Profile/>
        </ProtectedRoute>
    }
]);

export default router;