import {Spinner, Text, Wrap, WrapItem} from '@chakra-ui/react'
import React, {useEffect, useState} from "react";
import SidebarWithHeader from "./components/shared/Sidebar.jsx";
import {getCustomers} from "./services/client.js";
import CardWithImage from "./components/Card.jsx";

const App = () => {

    const [customers, setCustomers] = useState([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        setLoading(true)
        getCustomers().then(res => {
            setCustomers(res.data)
        }).catch(err => {
            console.log(err);
        }).finally(() => setLoading(false));
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

    if (customers.length <= 0) {
        return (<SidebarWithHeader>
            <Text>No customers available</Text>
        </SidebarWithHeader>)
    }

    return (
        <SidebarWithHeader>
            <Wrap justifyItems={"center"} spacing={"30"}>
                {customers.map((customer, index) => (
                    <WrapItem key={index}>
                        <CardWithImage{...customer}
                                      imageNumber={index}/>
                    </WrapItem>

                ))}
            </Wrap>

        </SidebarWithHeader>
    )
}

export default App;

