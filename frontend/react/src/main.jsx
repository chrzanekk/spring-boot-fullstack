import {StrictMode} from 'react'
import {createRoot} from 'react-dom/client'
import App from './App.jsx'
import './index.css'
import {ChakraProvider, createStandaloneToast} from '@chakra-ui/react'
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import Login from "./components/login/Login.jsx";
import {AuthProvider} from "./components/context/AuthContext.jsx"
import ProtectedRoute from "./components/shared/ProtectedRoute.jsx";
import Register from "./components/register/Register.jsx";

const {ToastContainer} = createStandaloneToast()
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
    }

])

createRoot(document.getElementById('root'))
    .render(
        <StrictMode>
            <ChakraProvider>
                <AuthProvider>
                    <RouterProvider router={router}/>
                </AuthProvider>
                <ToastContainer/>
            </ChakraProvider>
        </StrictMode>
    )
