import React, { useEffect, useState } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { signin } from '../Action/userAction';
import { useDispatch, useSelector } from 'react-redux';


export default function SigninPage() {
    const navigate = useNavigate();
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
        dispatch(signin(email, password));
    }
    useEffect(() => {
        console.log("use effect debug: ");
        console.log(userInfo);
        if (userInfo) {
            navigate(redirect);
        }
    }, [navigate, redirect, userInfo]);

    return (
        /*generate the email and password placeholder in sign in page */
        <div>
            <form className="form" onSubmit={submitHandler}>

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
                    <button className="primary" type="submit">
                        SIGN IN
                    </button>
                </div>

                <div>
                    <label />
                    <button className="registration">
                        {/* <Link to={`/register?redirect=${directTo}`}> */}
                        {/* CREAT AN ACCOUNT */}
                        {/* <Link>
                        </Link> */}
                        <Link to="/register">CREAT AN ACCOUNT</Link>
                    </button>
                </div>


            </form>
        </div>
    )
}
