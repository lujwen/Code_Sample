import React, { useEffect, useState } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { register, signin } from '../Action/userAction';
import { useDispatch, useSelector } from 'react-redux';


export default function CreateAccount() {
    const navigate = useNavigate();
    const [username, setUsername] = useState('');
    const [firstname, setFirstname] = useState('');
    const [lastname, setLastname] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const { search } = useLocation();
    const redirectInUrl = new URLSearchParams(search).get('redirect');
    const redirect = redirectInUrl ? redirectInUrl : '/';

    const userSignin = useSelector((state) => state.userSignin);
    const { userInfo, loading, error } = userSignin;

    const dispatch = useDispatch();

    /* set submit handler after generate sign in form */
    const submitHandler = (e) => {
        e.preventDefault(); // prevent refresh after click submit
        // sign in action
        dispatch(register(email, password, firstname, lastname, username));
    }
    useEffect(() => {
        if (userInfo) {
            navigate(redirect);
        }
    }, [navigate, redirect, userInfo]);

    return (
        /*generate the email and password placeholder in sign in page */
        <div>
            <form className="form" onSubmit={submitHandler}>
                <div>
                    <label htmlFor="username">User Name</label>
                    <input
                        type="username"
                        id="username"
                        placeholder="Enter user name"
                        required
                        onChange={(e) => setUsername(e.target.value)}
                    ></input>
                </div>
                <div>
                    <label htmlFor="firstname">First Name</label>
                    <input
                        type="firstname"
                        id="firstname"
                        placeholder="Enter first name"
                        required
                        onChange={(e) => setFirstname(e.target.value)}
                    ></input>
                </div>
                <div>
                    <label htmlFor="lastname">Last Name</label>
                    <input
                        type="lastname"
                        id="lastname"
                        placeholder="Enter last name"
                        required
                        onChange={(e) => setLastname(e.target.value)}
                    ></input>
                </div>

                <div>
                    <label htmlFor="email">Email address</label>
                    <input
                        type="email"
                        id="email"
                        placeholder="Enter email address"
                        required
                        onChange={(e) => setEmail(e.target.value)}
                    ></input>
                </div>
                <div>
                    <label htmlFor="password">Password</label>
                    <input
                        type="password"
                        id="password"
                        placeholder="Enter your password"
                        required
                        onChange={(e) => setPassword(e.target.value)}
                    ></input>
                </div>

                <div>
                    <label />
                    <button className="registration" type="submit">
                        Submit
                    </button>
                </div>


            </form>
        </div>
    )
}
