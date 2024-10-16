'use client'

import {Flex, Image, Link, Stack, Text} from '@chakra-ui/react'
import {useAuth} from "../../contexts/AuthContext.jsx";
import {useNavigate} from "react-router-dom";
import {useEffect} from "react";
import LoginForm from "./LoginForm.jsx";

const Login = () => {

    const {customer} = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        if (customer) {
            navigate("/dashboard");
        }
    })

    return (
        <Stack minH={'100vh'} direction={{base: 'column', md: 'row'}}>
            <Flex p={8} flex={1} align={'center'} justify={'center'}>
                <Stack spacing={4} w={'full'} maxW={'md'}>
                    <LoginForm/>
                    <Link color={"blue.600"} href="/register" align={'center'}>
                        Dont have an account? Register now
                    </Link>
                </Stack>
            </Flex>
            <Flex flex={1}
                  p={10}
                  flexDirection='column'
                  alignItems={'center'}
                  justifyContent={'center'}
                  bgGradient={{sm: 'linear(to-r,blue.200,purple.600)'}}>
                <Text fontSize={"6xl"} color={'white'} fontWeight={'bold'} mb={5}>
                    TEST ENVIRONMENT
                </Text>
                <Image
                    alt={'Login Image'}
                    objectFit="scale-down"
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