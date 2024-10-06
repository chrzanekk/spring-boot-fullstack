import {Spinner, Text, Wrap, WrapItem} from '@chakra-ui/react'
import React, {useEffect, useState} from "react";
import SidebarWithHeader from "./components/shared/Sidebar.jsx";
import {getCustomers} from "./services/Client.js";
import CardWithImage from "./components/customer/CustomerCard.jsx";
import CreateCustomerDrawer from "./components/customer/CreateCustomerDrawer.jsx";
import {errorNotification} from "./services/Notification.js";

const App = () => {

    const [customers, setCustomers] = useState([]);
    const [loading, setLoading] = useState(false);
    const [err, setError] = useState("");

    const fetchCustomers = () => {
        setLoading(true)
        getCustomers().then(res => {
            setCustomers(res.data)
        }).catch(err => {
            setError(err.response.data.message)
            errorNotification(
                err.code,
                err.response.data.message
            )
        }).finally(() => setLoading(false));
    }

    useEffect(() => {
        fetchCustomers();
    }, [])

    if (loading) {
        return <SidebarWithHeader>
            <Spinner
                thickness='4px'
                speed='0.65s'
                emptyColor='gray.200'
                color='blue.500'
                size='xl'
            />
        </SidebarWithHeader>
    }


    if (err) {
        return (<SidebarWithHeader>
            <CreateCustomerDrawer
                fetchCustomers={fetchCustomers}
            />
            <Text mt={"5"}>Ooops there was an error</Text>
        </SidebarWithHeader>)
    }

    if (customers.length <= 0) {
        return (<SidebarWithHeader>
            <CreateCustomerDrawer
                fetchCustomers={fetchCustomers}
            />
            <Text mt={"5"}>No customers available</Text>
        </SidebarWithHeader>)
    }

    return (
        <SidebarWithHeader>
            <CreateCustomerDrawer
            fetchCustomers={fetchCustomers}
            />
            <Wrap justify={"center"} spacing={"30"}>
                {customers.map((customer, index) => (
                    <WrapItem key={index}>
                        <CardWithImage{...customer}
                                      imageNumber={index}
                                      fetchCustomers={fetchCustomers}
                        />
                    </WrapItem>
                ))}
            </Wrap>
        </SidebarWithHeader>
    )
}

export default App;

