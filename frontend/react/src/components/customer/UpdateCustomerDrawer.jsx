import {
    Button,
    Drawer,
    DrawerBody,
    DrawerCloseButton,
    DrawerContent,
    DrawerFooter,
    DrawerHeader,
    DrawerOverlay,
    useDisclosure
} from "@chakra-ui/react"
import CreateCustomerForm from "./CreateCustomerForm.jsx";
import UpdateCustomerForm from "./UpdateCustomerForm.jsx";
import React from "react";

const AddIcon = () => "+"
const CloseIcon = () => "x"

const UpdateCustomerDrawer = ({fetchCustomers, initialValues, customerId}) => {
    const {isOpen, onOpen, onClose} = useDisclosure()
    return <>
        <Button
            colorScheme={'facebook'}
            rounded={'full'}
            _hover={{
                transform: 'translateY(-2)',
                boxShadow: 'lg'
            }}
            onClick={onOpen}
        >
            Update customer

        </Button>
        <Drawer isOpen={isOpen} onClose={onClose} size={"lg"}>
            <DrawerOverlay/>
            <DrawerContent>
                <DrawerCloseButton/>
                <DrawerHeader>Update customer</DrawerHeader>
                <DrawerBody>
                    <UpdateCustomerForm
                    fetchCustomers={fetchCustomers}
                    initialValues={initialValues}
                    customerId={customerId}
                    />
                </DrawerBody>

                <DrawerFooter>
                    <Button leftIcon={<CloseIcon/>}
                            colorScheme={"facebook"}
                            onClick={onClose}
                    >
                        Close
                    </Button>
                </DrawerFooter>
            </DrawerContent>
        </Drawer>

    </>

}

export default UpdateCustomerDrawer;

