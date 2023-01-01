import React from 'react';
import { Link, useNavigate } from "react-router-dom";
import { useSelector, useDispatch } from 'react-redux';
import { addToCart, removeFromCart, placeOrder } from "../Action/cartAction";
import MyComponents from '../components/map';

export default function CartScreen(prod) {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const cart = useSelector((state) => state.cart);
    const { cartItems } = cart;

    const removeFromCartHandler = (id) => {
        dispatch(removeFromCart(id)).then(navigate("/cart"));
    }

    const checkoutHandler = () => {
        // update the navigate direct url
        cartItems.length = 0;
    }
    return (
        <div className="row top">
            <div className="col-2">
                <h1 className="cartTitle">Shopping Cart</h1>
                {cartItems.length === 0 ?
                    (<div>Cart is empty. <Link to="/">Go Shopping</Link></div>
                    ) : (
                        <ul>
                            {cartItems.map((item) => (
                                <li key={item.id}>
                                    <div className="row">
                                        <div>
                                            <img
                                                src={item.imageUrl}
                                                alt={item.name}
                                                className="small"
                                            ></img>
                                        </div>
                                        <div className="min-30">
                                            <Link to={`/product/${item.id}`}>{item.name}</Link>
                                        </div>
                                        <div>
                                            <select
                                                value={item.qty}
                                                onChange={(e) =>
                                                    dispatch(
                                                        addToCart(item.id)
                                                    )
                                                }
                                            >
                                                {[...Array(item.countInStock).keys()].map((x) => (
                                                    <option key={x + 1} value={x + 1}>
                                                        {x + 1}
                                                    </option>
                                                ))}
                                            </select>
                                        </div>
                                        <div>${item.price}</div>
                                        <div>
                                            <button
                                                type="button"
                                                onClick={() => removeFromCartHandler(item.id)}
                                            >
                                                Delete
                                            </button>
                                        </div>
                                    </div>
                                </li>
                            ))}
                        </ul>
                    )}
            </div>
            <div className="col-1">
                <div className="card card-body">
                    <ul>
                        <li>
                            <h2>
                                Subtotal ({cartItems.reduce((a, c) => a + c.qty, 0)} items) : $
                                {cartItems.reduce((a, c) => a + c.price * c.qty, 0)}
                            </h2>
                        </li>
                        <li>
                            <Link
                                to={"/aftercheckout"}
                                type="button"
                                onClick={checkoutHandler}
                                className="primary block"
                                disabled={cartItems.length === 0}
                            >
                                Proceed to Checkout
                            </Link>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    );
}
