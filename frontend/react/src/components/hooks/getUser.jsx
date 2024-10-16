import {useEffect, useState} from "react";
import {jwtDecode} from "jwt-decode";
import {getCustomerByEmail} from "../../services/Client.js";
import {errorNotification} from "../../services/Notification.js";

export const getUser = () => {
    const [user, setUser] = useState(null);

    useEffect(() => {
        const token = localStorage.getItem("auth");
        if (token) {
            const decodedToken = jwtDecode(token);
            const email = decodedToken.sub;
            getCustomerByEmail(email).then(response => {
                setUser(response.data)
            }).catch(err => {
                console.log(err);
                errorNotification(
                    err.code,
                    err.response.data.message
                )
            });
        }
    }, []);
    return user;
}
