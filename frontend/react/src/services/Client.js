import axios from 'axios'

const getAuthConfig = () => ({
    headers: {
        Authorization: `Bearer ${localStorage.getItem("auth")}`
    }
})

export const getCustomers = async () => {
    try {
        return await axios.get(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customers`,
            getAuthConfig()
        )
    } catch (e) {
        throw e;
    }
}

export const saveCustomer = async (customer) => {
    try {
        console.log(`url to backend: ${import.meta.env.VITE_API_BASE_URL}`)
        return await axios.post(
            `${import.meta.env.VITE_API_BASE_URL}/api/v1/customers`,
            customer
        )
    } catch (e) {
        throw e;
    }
}

export const updateCustomer = async (id, update) => {
    try {
        return await axios.put(
            `${import.meta.env.VITE_API_BASE_URL}/api/v1/customers/${id}`,
            update,
            getAuthConfig()
        )
    } catch (e) {
        throw e;
    }
}

export const deleteCustomer = async (id) => {
    try {
        return await axios.delete(
            `${import.meta.env.VITE_API_BASE_URL}/api/v1/customers/${id}`,
            getAuthConfig()
        )
    } catch (e) {
        throw e;
    }
}

export const login = async (usernameAndPassword) => {
    try {
        return await axios.post(
            `${import.meta.env.VITE_API_BASE_URL}/api/v1/auth/login`,
            usernameAndPassword
        )
    } catch (e) {
        throw e;
    }
}

