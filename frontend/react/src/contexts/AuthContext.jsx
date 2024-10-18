import {createContext, useContext, useEffect, useState} from "react";
import {login as performLogin} from "../services/Client.js";
import {jwtDecode} from "jwt-decode";

const AuthContext = createContext({});

export const AuthProvider = ({children}) => {

    const [customer, setCustomer] = useState(null);

    const setCustomerFromToken = () => {
        let token = localStorage.getItem("auth");
        if (token) {
            let decodedToken = jwtDecode(token);
            setCustomerData(decodedToken);
        }
    }

    const setCustomerData = (decodedToken) => {
        const customerData = {
            id: decodedToken.id,
            name: decodedToken.name,
            email: decodedToken.email,
            age: decodedToken.age,
            gender: decodedToken.gender,
            username: decodedToken.sub,
            roles: decodedToken.scopes
        };
        setCustomer(customerData);
        localStorage.removeItem("customer")
        localStorage.setItem("customer", JSON.stringify(customerData));
    }


    const updateCustomerData = (updatedData) => {
        const newCustomer = {
            ...customer,
            ...updatedData
        };
        setCustomer(newCustomer);
        localStorage.removeItem("customer")
        localStorage.setItem("customer", JSON.stringify(newCustomer));
    };

    useEffect(() => {
        const storedCustomer = JSON.parse(localStorage.getItem("customer"));
        if (storedCustomer) {
            setCustomer(storedCustomer);
        } else {
            setCustomerFromToken();
        }
    }, []);


    const login = async (usernameAndPassword) => {
        try {
            const res = await performLogin(usernameAndPassword);

            const jwtToken = res.headers["authorization"];
            if (!jwtToken) {
                throw new Error("Authorization token not found in response headers");
            }

            localStorage.setItem("auth", jwtToken);

            const decodedToken = jwtDecode(jwtToken);
            setCustomerData(decodedToken);
            localStorage.removeItem("customer")
            localStorage.setItem("customer", JSON.stringify(decodedToken));

            return res;
        } catch (err) {
            console.error("Login error:", err);
            throw err;
        }
    };

    const logOut = () => {
        localStorage.removeItem("auth");
        localStorage.removeItem("customer");
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
            setCustomerFromToken,
            updateCustomerData,
            setCustomerData
        }}>
            {children}
        </AuthContext.Provider>
    )
}

export const useAuth = () => useContext(AuthContext);

export default AuthContext;