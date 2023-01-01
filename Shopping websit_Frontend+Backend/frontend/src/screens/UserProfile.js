import React, { useEffect, useState } from 'react';
import { editProfile } from "../Action/userAction";
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';

export default function Profile() {
    const navigate = useNavigate();
    const [username, setUsername] = useState('');
    const [firstname, setFirstname] = useState('');
    const [lastname, setLastname] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const userSignin = useSelector((state) => state.userSignin);
    const { userInfo, loading, error } = userSignin;

    console.log("username:");
    console.log(userInfo.username);
    console.log("userid:");
    console.log(userInfo.id);
    console.log(userInfo.firstname);

    const dispatch = useDispatch();

    /* set an edit handler */
    const editUserNameHandler = (e) => {
        e.preventDefault(); // prevent refresh after click submit
        dispatch(editProfile(userInfo.id, userInfo.email, userInfo.firstname, userInfo.lastname, username)).then(navigate("/profile"));
    }
    const editFirstNameHandler = (e) => {
        e.preventDefault(); // prevent refresh after click submit
        dispatch(editProfile(userInfo.id, userInfo.email, firstname, userInfo.lastname, userInfo.username));
    }
    const editLastNameHandler = (e) => {
        e.preventDefault(); // prevent refresh after click submit
        dispatch(editProfile(userInfo.id, userInfo.email, userInfo.firstname, lastname, userInfo.username));
    }
    const editEmailHandler = (e) => {
        e.preventDefault(); // prevent refresh after click submit
        dispatch(editProfile(userInfo.id, email, userInfo.firstname, userInfo.lastname, userInfo.username));
    }
    return (
        <div>
            <div>
                <form className="form" onSubmit={editUserNameHandler}>
                    <label htmlFor="username">User Name: </label>
                    <input
                        type="username"
                        id="username"
                        placeholder={userInfo.username}
                        required
                        onChange={(e) => setUsername(e.target.value)}
                    ></input>
                    <button type="submit">save</button>
                </form>
                <form className="form" onSubmit={editEmailHandler}>
                    <label htmlFor="email">Email: </label>
                    <input
                        type="email"
                        id="email"
                        placeholder={userInfo.email}
                        required
                        onChange={(e) => setEmail(e.target.value)}
                    ></input>
                    <button type="submit">save</button>
                </form>
            </div >

            <form className="form" onSubmit={editFirstNameHandler}>
                <label htmlFor="fisrtname">First Name: </label>
                <input
                    type="firstname"
                    id="firstname"
                    placeholder={userInfo.firstname}
                    required
                    onChange={(e) => setFirstname(e.target.value)}
                ></input>
                <button type="submit">save</button>
            </form>
            <form className="form" onSubmit={editLastNameHandler}>
                <label htmlFor="lastname">  Last Name: </label>
                <input
                    type="lastname"
                    id="lastname"
                    placeholder={userInfo.lastname}
                    required
                    onChange={(e) => setLastname(e.target.value)}
                ></input>
                <button type="submit">save</button>
            </form>
        </div>
    );
}