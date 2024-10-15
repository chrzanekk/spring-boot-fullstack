import {createContext, useContext, useEffect, useState} from "react";
import {login as performLogin} from "../../services/Client.js";
import {jwtDecode} from "jwt-decode";

const AuthContext = createContext({});


export const AuthProvider = ({children}) => {

    const [customer, setCustomer] = useState(null);

    const setCustomerFromToken = () => {
        let token = localStorage.getItem("auth");
        if(token) {
            let decodedToken = jwtDecode(token);
            setCustomer({
                username: decodedToken.sub,
                roles: decodedToken.scopes
            })
        }
    }

    useEffect(() => {
        setCustomerFromToken();
    },[])

    // const login = async (usernameAndPassword) => {
    //     return new Promise((resolve, reject) => {
    //         performLogin(usernameAndPassword).then(res => {
    //             const jwtToken = res.headers["authorization"];
    //             localStorage.setItem("auth", jwtToken)
    //             const decodedToken = jwtDecode(jwtToken);
    //             setCustomer({
    //                 username: decodedToken.sub,
    //                 roles: decodedToken.scopes
    //             })
    //             resolve(res);
    //         }).catch(err => {
    //             reject(err);
    //         })
    //     })
    // }

    const login = async (usernameAndPassword) => {
        try {
            const res = await performLogin(usernameAndPassword);

            const jwtToken = res.headers["authorization"];
            if (!jwtToken) {
                throw new Error("Authorization token not found in response headers");
            }

            localStorage.setItem("auth", jwtToken);

            const decodedToken = jwtDecode(jwtToken);
            setCustomer({
                username: decodedToken.sub,
                roles: decodedToken.scopes
            });

            return res;
        } catch (err) {
            console.error("Login error:", err);
            throw err;  // This rethrows the error so it can be handled by the caller
        }
    };

    const logOut = () => {
        localStorage.removeItem("auth");
        setCustomer(null);
    }

    const isAuthenticated = () => {
        const token = localStorage.getItem("auth")
        if (!token) {
            return false;
        }
        const {exp: expiration} = jwtDecode(token);
        if (Date.now() > expiration * 1000) {
            logOut()
            return false;
        }
        return true;
    }

    return (
        <AuthContext.Provider value={{
            customer,
            login,
            logOut,
            isAuthenticated,
            setCustomerFromToken
        }}>
            {children}
        </AuthContext.Provider>
    )
}

export const useAuth = () => useContext(AuthContext);

export default AuthContext;