'use client'

import {
    Alert,
    AlertIcon,
    Box,
    Button,
    Flex,
    FormLabel,
    Heading,
    Image,
    Input,
    Link, resolveStyleConfig,
    Stack,
    Text,
} from '@chakra-ui/react'
import {Form, Formik, useField} from "formik";
import * as Yup from 'yup'
import {useAuth} from "../context/AuthContext.jsx";
import {errorNotification} from "../../services/Notification.js";
import {useNavigate} from "react-router-dom";
import {useEffect} from "react";

const MyTextInput = ({label, ...props}) => {
    const [field, meta] = useField(props);
    return (
        <Box>
            <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
            <Input className="text-input" {...field} {...props} />
            {meta.touched && meta.error ? (
                <Alert className="error" status={"error"} mt={2}>
                    <AlertIcon/>
                    {meta.error}</Alert>
            ) : null}
        </Box>
    );
};


const LoginForm = () => {
    const { login } = useAuth();
    const navigate = useNavigate();
    return (
        <Formik
            validateOnMount={true}
            validationSchema={
                Yup.object({
                    username: Yup.string()
                        .email("Must be valid email.")
                        .required("Email is required."),
                    password: Yup.string()
                        .min(4, "Password cannot be less than 4 characters")
                        .max(10, "Password cannot be more than 10 characters")
                        .required("Password is required.")
                })
            }
            initialValues={{username: '', password: ''}}
            onSubmit={(values, {setSubmitting}) => {
                setSubmitting(true);
                login(values).then(res => {
                    navigate("/dashboard")
                    console.log("Successfully log in")
                }).catch(err => {
                    errorNotification(err.code, err.response.data.message)
                }).finally(() => {
                    setSubmitting(false)
                })
            }}>

            {({isValid, isSubmitting}) => (
                <Form>
                    <Stack spacing={15}>
                        <Box display="flex" justifyContent="center">
                            <Image
                                alt={'Login Image'}
                                objectFit={"scale-down"}
                                src={
                                    '/img/logo-round.png'
                                }
                                width={'200px'}
                                height={'auto'}
                            />
                        </Box>
                        <Box display="flex" justifyContent="center">
                            <Heading fontSize={'2xl'} mb={15}>Sign in to your account</Heading>
                        </Box>
                        <MyTextInput
                            label={"Email"}
                            name={"username"}
                            type={"email"}
                            placeholder={"example@example.com"}
                        />
                        <MyTextInput
                            label={"Password"}
                            name={"password"}
                            type={"password"}
                            placeholder={"************"}
                        />
                        <Button
                            type="submit"
                            isDisabled={!isValid || isSubmitting}>
                            Login
                        </Button>
                    </Stack>
                </Form>
            )}

        </Formik>
    )
}

const Login = () => {

    const { customer } = useAuth();
    const navigate = useNavigate();

    useEffect(()=> {
        if(customer) {
            navigate("/dashboard");
        }
    })

    return (
        <Stack minH={'100vh'} direction={{base: 'column', md: 'row'}}>
            <Flex p={8} flex={1} align={'center'} justify={'center'}>
                <Stack spacing={4} w={'full'} maxW={'md'}>
                    <LoginForm/>
                </Stack>
            </Flex>
            <Flex flex={1}
                  p={10}
                  flexDirection={'column'}
                  alignItems={'center'}
                  justifyContent={'center'}
                  bgGradient={{sm: 'linear(to-r,blue.200,purple.600)'}}>
                <Text fontSize={"6xl"} color={'white'} fontWeight={'bold'} mb={5}>
                    <Link href={"#"}>
                        JAKIŚ RANDOMOWY LINK
                    </Link>
                </Text>
                <Image
                    alt={'Login Image'}
                    objectFit={"scale-down"}
                    src={
                        '/img/logo.jpg'
                    }
                    width={'600px'}
                    height={'auto'}
                />
            </Flex>
        </Stack>
    )
}

export default Login