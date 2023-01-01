import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { BrowserRouter, Link, Routes } from "react-router-dom";
import { signout } from './Action/userAction';
import { Route } from 'react-router-dom';
import HomeScreen from './screens/HomeScreen'
import ProductScreen from './screens/ProductScreen';
import CartScreen from './screens/CartScreen';
import SigninPage from './screens/SigninPage';
import CreateAccount from './screens/CreateAccountPage';
import Profile from './screens/UserProfile';
import AfterCheckout from './screens/afterCheckout';
import NotFound from './screens/NotFound';

function App() {
  const userSignin = useSelector((state) => state.userSignin);
  const { userInfo } = userSignin;
  const cart = useSelector((state) => state.cart);
  const { cartItems } = cart;

  const dispatch = useDispatch();
  const signoutHandler = () => {
    dispatch(signout());
  };

  return (
    <BrowserRouter>
      <div className="grid-container">
        <header className="row">
          <div className='brand'>
            <Link to="/" >Antiques & You</Link>
          </div>
          <div className='cartIcon'>
            <Link to="/cart">
              Cart
            </Link>
            {userInfo ? (
              // updated signin page showing username after signing or registering
              <div className="dropdown">
                <Link className="user-btn" to="/profile">
                 Welcome! {userInfo.name} <i ></i>{'  '}
                </Link>
                <Link className="signout-btn" to="/signout" onClick={signoutHandler}>
                  Sign Out
                </Link>
              </div>
            ) :
              (<Link to="/signin">Sign In</Link>)}
          </div >
        </header >
        <main>
          <Routes>
            <Route path="/cart" element={<CartScreen />}></Route>
            <Route path="/product/:id" element={<ProductScreen />} exact></Route>
            <Route path="/signin" element={<SigninPage />}></Route>
            <Route path="/signout" element={<SigninPage />}></Route>
            <Route path="/" element={<HomeScreen />}></Route>
            <Route path="/register" element={<CreateAccount />}></Route>
            <Route path="/profile" element={<Profile />}></Route>
            <Route path="/aftercheckout" element={<AfterCheckout />}></Route>
            <Route path="/notfound" element={<NotFound />}></Route>

          </Routes>
        </main>
      </div >
    </BrowserRouter >
  );
}

export default App;
