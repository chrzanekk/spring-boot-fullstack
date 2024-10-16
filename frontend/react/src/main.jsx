import {StrictMode} from 'react'
import {createRoot} from 'react-dom/client'
import './index.css'
import {ChakraProvider, createStandaloneToast} from '@chakra-ui/react'
import {RouterProvider} from "react-router-dom";
import {AuthProvider} from "./components/context/AuthContext.jsx"
import router from './services/router.jsx'

const {ToastContainer} = createStandaloneToast()


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
