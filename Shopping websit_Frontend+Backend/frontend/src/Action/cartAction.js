
import Axios from "axios";
import uuidv4 from "uuidv4";
import {
    USER_ADDTO_CART_REQUEST,
    USER_ADDTO_CART_SUCCESS,
    USER_ADDTO_CART_FAIL
} from "../constants/cartConstant";


export const addToCart = (product_id) => async (dispatch, getState) => {

    dispatch({ type: USER_ADDTO_CART_REQUEST, payload: { product_id } });
    try {
        const url = 'https://shopdb-demo-345.ue.r.appspot.com/api/product/getProduct?id=' + product_id.toString();
        const product_res = await Axios.get(url);
        const {
            cart: { cartItems },
        } = getState();
        // cartItems is a list of productId, use dictionary to count, then show on cart page
        if (cartItems.length > 0) {
            var found = false;
            for (var i = 0; i < cartItems.length; i++) {
                if (parseInt(cartItems[i]['id']) === parseInt(product_id)) {
                    cartItems[i]['qty'] += 1;
                    found = true;
                    break;
                }
            }
            if (!found) {
                const product_qty = {
                    "id": parseInt(product_id),
                    "qty": 1,
                    "imageUrl": product_res.data.image,
                    "price": product_res.data.price,
                    "name": product_res.data.name,
                    "countInStock": product_res.data.stockCount
                }
                cartItems.push(product_qty)
            }
        } else {
            const product_qty = {
                "id": parseInt(product_id),
                "qty": 1,
                "imageUrl": product_res.data.image,
                "price": product_res.data.price,
                "name": product_res.data.name,
                "countInStock": product_res.data.stockCount
            }
            cartItems.push(product_qty)
        }
        dispatch({ type: USER_ADDTO_CART_SUCCESS, payload: { product_id } })
        localStorage.setItem(
            'cartItems',
            JSON.stringify(getState().cart.cartItems)
        );
    } catch (error) {
        console.error(error.response.data);
        dispatch({
            type: USER_ADDTO_CART_FAIL,
            payload:
                error.response && error.response.data.message
                    ? error.response.data.message
                    : error.message,
        });
    }
};

export const removeFromCart = (product_id) => async (dispatch, getState) => {

    dispatch({ type: USER_ADDTO_CART_REQUEST, payload: { product_id } });
    try {
        const {
            cart: { cartItems },
        } = getState();
        // cartItems is a list of productId, use dictionary to count, then show on cart page
        if (cartItems.length > 0) {
            var deleteItemIndex = -1;
            for (var i = 0; i < cartItems.length; i++) {
                if (parseInt(cartItems[i]['id']) === product_id) {
                    cartItems[i]['qty'] -= 1;
                    if (cartItems[i]['qty'] == 0) {
                        deleteItemIndex = i;
                    }
                    break;
                }
            }
            if (deleteItemIndex >= 0) {
                cartItems.splice(deleteItemIndex, 1);
            }
        }
        dispatch({ type: USER_ADDTO_CART_SUCCESS, payload: { product_id } })
        localStorage.setItem(
            'cartItems',
            JSON.stringify(getState().cart.cartItems)
        );
    } catch (error) {
        console.error(error.response.data);
        dispatch({
            type: USER_ADDTO_CART_FAIL,
            payload:
                error.response && error.response.data.message
                    ? error.response.data.message
                    : error.message,
        });
    }
};

export const placeOrder = () => async (dispatch, getState) => {

    dispatch({ type: USER_ADDTO_CART_REQUEST, payload: {} });
    try {
        var {
            cart: { cartItems },
        } = getState();
        const {
            userSignin: { userInfo },
        } = getState();
        // create a unique order id.
        const orderId = uuidv4();
        for (var i = 0; i < cartItems.length; i++) {
            const { data } = await Axios.post('https://shopdb-demo-345.ue.r.appspot.com/api/order/createOrder',
                {
                    orderId: orderId,
                    productId: parseInt(cartItems[i]['id']),
                    userId: userInfo.id,
                    price: cartItems[i]['price'],
                    qty: cartItems[i]['qty']
                });
        }
        // reset cartItems after checkout
        cartItems = []
        localStorage.setItem(
            'cartItems',
            JSON.stringify(cartItems)
        );
    } catch (error) {
        console.error(error.response.data);
        dispatch({
            type: USER_ADDTO_CART_FAIL,
            payload:
                error.response && error.response.data.message
                    ? error.response.data.message
                    : error.message,
        });
    }
};
