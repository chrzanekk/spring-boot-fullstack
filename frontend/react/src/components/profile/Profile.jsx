'use client'

import {Avatar, Box, Center, Flex, Heading, Image, Stack, Tag, Text, useColorModeValue,} from '@chakra-ui/react'
import React from "react";
import UpdateProfileForm from "./UpdateProfile.jsx";
import SidebarWithHeader from "../../layout/Sidebar.jsx";
import {getUser} from "../../hooks/getUser.jsx";

export default function Profile() {

    const user = getUser();

    if (!user) return <Text>Loading...</Text>;
    const {id, name, email, age, gender} = user;

    const randomUserGender = gender === "MALE" ? "men" : "women";
    return (
        <SidebarWithHeader>
            <Center py={6}>
                <Box
                    maxW={'600px'}
                    minW={'600px'}
                    w={'full'}
                    m={2}
                    bg={useColorModeValue('white', 'gray.800')}
                    boxShadow={'lg'}
                    rounded={'md'}
                    overflow={'hidden'}>
                    <Image
                        h={'120px'}
                        w={'full'}
                        src={
                            'https://images.unsplash.com/photo-1612865547334-09cb8cb455da?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=634&q=80'
                        }
                        objectFit="cover"
                        alt="#"
                    />
                    <Flex justify={'center'} mt={-12}>
                        <Avatar
                            size={'xl'}
                            src={
                                `https://randomuser.me/api/portraits/${randomUserGender}/${id}.jpg`
                            }
                            css={{
                                border: '2px solid white',
                            }}
                        />
                    </Flex>

                    <Box p={6}>
                        <Stack spacing={2} align={'center'} mb={5}>
                            <Tag borderRadius={"full"}>{id}</Tag>
                            <Heading fontSize={'2xl'} fontWeight={500} fontFamily={'body'}>
                                {name}
                            </Heading>
                            <Text color={'gray.500'}>{email}</Text>
                            <Text color={'gray.500'}>Age {age} | {gender}</Text>
                        </Stack>
                    </Box>
                    <Stack direction={"row"} justify={'center'} spacing={6} p={4}>
                        <Stack>
                            <UpdateProfileForm
                                initialValues={{name,age}}
                                customerId={id}
                            />
                        </Stack>
                    </Stack>
                </Box>
            </Center>
        </SidebarWithHeader>
    )
}