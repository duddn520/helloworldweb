import React from "react";
import FlatList from 'flatlist-react';
import PostCard from "../component/blog/post/PostCard";
import {Container, Paper} from '@mui/material';

//useEffect사용하여 getPosts 로 postList 받고, 렌더링.
const people = [
    {firstName: 'Elson', lastName: 'Correia', info: {age: 24}},
    {firstName: 'John', lastName: 'Doe', info: {age: 18}},
    {firstName: 'Jane', lastName: 'Doe', info: {age: 34}},
    {firstName: 'Maria', lastName: 'Carvalho', info: {age: 22}},
    {firstName: 'Kelly', lastName: 'Correia', info:{age: 23}},
    {firstName: 'Don', lastName: 'Quichote', info: {age: 39}},
    {firstName: 'Marcus', lastName: 'Correia', info: {age: 0}},
    {firstName: 'Bruno', lastName: 'Gonzales', info: {age: 25}},
    {firstName: 'Alonzo', lastName: 'Correia', info: {age: 44}}
]

export default function FlatlistTest()
{


    const renderPerson = (person) => {
        return (
                <PostCard title={person.firstName} content={person.lastName}
                          sx={{}}

                />
        );
    }

    return (
        <ul>
            <FlatList
                list={people}
                renderItem={renderPerson}
                renderWhenEmpty={() => <div>List is empty!</div>}
                sortBy={["firstName", {key: "lastName", descending: true}]}
                display={{
                    grid: true,
                }}
                minColumnWidth={`${window.screen.width*0.3}px`}
            />
        </ul>
    )
}