import Axios from "axios";
import {
    USER_SIGNIN_FAIL,
    USER_SIGNIN_REQUEST,
    USER_SIGNIN_SUCCESS,
    USER_REGISTER_REQUEST,
    USER_REGISTER_SUCCESS,
    USER_REGISTER_FAIL,
    USER_SIGNOUT,
    USER_EDIT_PROFILE_SUCCESS,
    USER_EDIT_PROFILE_FAIL,

} from "../constants/userConstant";


export const register = (email, password, firstname, lastname, username) => async (dispatch) => {
    dispatch({ type: USER_REGISTER_REQUEST, payload: { email, password, firstname, lastname, username } });
    try {
        const { data } = await Axios.post('https://shopdb-demo-345.ue.r.appspot.com/api/users/createUser', {
            email,
            password,
            firstname,
            lastname,
            username
        });

        dispatch({ type: USER_REGISTER_SUCCESS, payload: data });
        dispatch({ type: USER_SIGNIN_SUCCESS, payload: data });
        localStorage.setItem('userInfo', JSON.stringify(data));
    } catch (error) {
        dispatch({
            type: USER_REGISTER_FAIL,
            payload:
                error.response && error.response.data.message
                    ? error.response.data.message
                    : error.message,
        });
    }
};

export const editProfile = (id, email, firstname, lastname, username) => async (dispatch) => {
    dispatch({ type: USER_REGISTER_REQUEST, payload: { id, email, firstname, lastname, username } });
    try {
        const { data } = await Axios.put('https://shopdb-demo-345.ue.r.appspot.com/api/users/updateUser', {
            id, email, firstname, lastname, username
        });
        console.log("data in edit profile");
        console.log(data);
        dispatch({ type: USER_EDIT_PROFILE_SUCCESS, payload: data });
        localStorage.setItem('userInfo', JSON.stringify(data));
    } catch (error) {
        dispatch({
            type: USER_EDIT_PROFILE_FAIL,
            payload:
                error.response && error.response.data.message
                    ? error.response.data.message
                    : error.message,
        });
    }
}

export const signin = (email, password) => async (dispatch) => {
    dispatch({ type: USER_SIGNIN_REQUEST, payload: { email, password } });
    try {
        const { data } = await Axios.post('https://shopdb-demo-345.ue.r.appspot.com/api/users/login', { email, password });
        console.log("sign in data: ");
        console.log(data);
        dispatch({ type: USER_SIGNIN_SUCCESS, payload: data });
        localStorage.setItem('userInfo', JSON.stringify(data));
    } catch (error) {
        console.error(error.response.data);
        dispatch({
            type: USER_SIGNIN_FAIL,
            payload:
                error.response && error.response.data.message
                    ? error.response.data.message
                    : error.message,
        });
    }
};

export const signout = () => (dispatch) => {
    localStorage.removeItem('userInfo');
    localStorage.removeItem('cartItems');
    dispatch({ type: USER_SIGNOUT });
    document.location.href = '/signin';
};

