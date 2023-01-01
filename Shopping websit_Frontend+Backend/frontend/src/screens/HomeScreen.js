import React, { useEffect, useState } from "react";
import axios from 'axios';
import Product from '../components/Product';

export default function HomeScreen() {
    const [products, setProducts] = useState([]);
    useEffect(() => {
        const fetchData = async () => {
            const { data } = await axios.get('https://shopdb-demo-345.ue.r.appspot.com/api/product/showAllProducts')
            setProducts(data);
        };
        fetchData();
    }, [])

    return (
        <div className="intro-container">
            <div className="intro">
                <h1>Welcome to the best antique shop in North America!</h1>
                <img className="shop-img" src="../images/antique_shop.jpg" alt="image of an antique shop"></img>
                <p>Antiques & You was started by three Northeastern University students
                    who are passionate about antiques and collectibles, back in 2021.
                </p>
                <p>As we partner with many antique dealers all over North America,
                    we have an expansive collection of prestigious antiques. If you
                    have any special requests, please let us know. We are always
                    happy to help you look for a certain piece or connect you with
                    other resources.
                </p>
                <p>The flagship shop of Antiques & You is based in San Jose, California.
                    As our products are valuable and obsolete, we only allow reservations
                    through the online plaform and the transactions take place in
                    person. The flagship shop is where you will be picking up your
                    order, packaged with 100% care.
                </p>
                <p>Here at Antiques & You, we believe every collectible tells a
                    treasured story. It is our honor to pass on those timeless tales.
                </p>
                <p>We hope you enjoy your time at Antiques & You 😊</p>
            </div>
            <hr></hr>
            <div className="product-display">
                <h1>Our Latest Arrivals</h1>
            </div>
            <div className="row center">
                {
                    products.map((product) => (
                        <Product key={product["id"]} product={product}></Product>
                    )

                    )
                }
            </div>

            <div className="intro">
                <h1>Store Policies</h1>
                <p>As we partner with many antique dealers all over North America,
                    we have an expansive collection of prestigious antiques. If you
                    have any special requests, please let us know. We are always
                    happy to help you look for a certain piece or connect you with
                    other resources.
                </p>
                    <p>The flagship shop of Antiques & You is based in San Jose, California.
                        As our products are valuable and obsolete, we currently only allow reservations
                        through our online plaform and the transactions take place in
                        person. The flagship shop is where you will be picking up your
                        order, packaged with 100% care.
                    </p>
                      <p>
                      Please kindly note that all sales are final. 
                    </p>
            </div>
            <div className="intro">
                    <h1>Financing Options</h1>
                    <p>
                      We also offer various financing options, please reach out to us if 
                      you are interested. 
                    </p>
                </div>

                <div className="intro">
                    <h1>We are here for you!</h1>
                    <p>Phone Number: 1-222-333-4444</p>
                    <p>Email: antiquesandyou@somemail.com</p>
                    <p>Address: 4 N 2nd St, San Jose, CA 95113</p>
                    <p>We look forward to connecting with you 😊</p>
                </div>

                <footer className="copyright-container">
                <a className="footer-links" href="https://www.linkedin.com/">LinkedIn</a>
                <a className="footer-links" href="https://www.instagram.com/">Instagram</a>
                <a className="footer-links" href="https://www.facebook.com/">Facebook</a>
                 <p class="copyright">© 2022 CS5610 Group 5</p>
                </footer>
                
        
        </div>
    );
}
