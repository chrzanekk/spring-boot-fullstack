import axios from 'axios'


const api = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL,
});

const getAuthConfig = () => ({
    headers: {
        Authorization: `Bearer ${localStorage.getItem("auth")}`
    }
})

export const getCustomers = async () => {
    try {
        return await api.get(`/api/v1/customers`,
            getAuthConfig()
        )
    } catch (e) {
        throw e;
    }
}

export const saveCustomer = async (customer) => {
    try {
        console.log(`url to backend: ${import.meta.env.VITE_API_BASE_URL}`)
        return await api.post(
            `/api/v1/customers`,
            customer
        )
    } catch (e) {
        throw e;
    }
}

export const updateCustomer = async (id, update) => {
    try {
        return await api.put(
            `/api/v1/customers/${id}`,
            update,
            getAuthConfig()
        )
    } catch (e) {
        throw e;
    }
}

export const deleteCustomer = async (id) => {
    try {
        return await api.delete(
            `/api/v1/customers/${id}`,
            getAuthConfig()
        )
    } catch (e) {
        throw e;
    }
}

export const login = async (usernameAndPassword) => {
    try {
        return await api.post(
            `/api/v1/auth/login`,
            usernameAndPassword
        )
    } catch (e) {
        throw e;
    }
}

export const getCustomerByEmail = async (email) => {
    try {
        return await api.get(`api/v1/customers/email/${email}`, getAuthConfig());
    } catch (e) {
        throw e;
    }
}


