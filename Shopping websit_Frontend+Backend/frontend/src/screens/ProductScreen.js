import React, { useEffect, useState } from "react";
import { Link, useNavigate, useParams, useLocation } from "react-router-dom";
import { useDispatch } from 'react-redux';
import { addToCart } from "../Action/cartAction";
import axios from 'axios';

export default function ProductScreen() {
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const params = useParams();
    const { id: productId } = params;
    const [products, setProducts] = useState([]);
    useEffect(() => {
        const fetchData = async () => {
            const { data } = await axios.get('https://shopdb-demo-345.ue.r.appspot.com/api/product/showAllProducts')
            setProducts(data);
        };
        fetchData();
    }, [])
    var product = null;
    for (var i = 0; i < products.length; i++) {
        if (parseInt(products[i]["id"]) === parseInt(productId)) {
            product = products[i];
        }
    }
    if (!product) {
        return <div>Product Not Found</div>;
    }
    const { search } = useLocation();
    const redirectInUrl = new URLSearchParams(search).get('redirect');
    const redirect = redirectInUrl ? redirectInUrl : '/';

    const addToCartHandler = () => {
        dispatch(addToCart(productId)).then(navigate(`/`));
    }
    return (
        <div>

            <div className="row top">
                <div className="col-2">
                    <img className="large" src={product.image} alt={product.name}></img>
                </div>
                <div className="col-1">
                    <ul className="productCard">
                        <li>
                            <h1 className="productDetail">{product.name}</h1>
                        </li>
                        <li className="productDetail">
                            price : ${product.price}
                        </li>
                        <li className="productDetail">
                            Description:
                            <p>
                                {product.description}
                            </p>
                            <div className="row">
                                <div className="productDetail">Status</div>
                                <div>
                                    {product.stockCount > 0 ? (
                                        <span className="success">In Stock</span>
                                    ) : (
                                        <span className="danger">Unavailable</span>
                                    )}
                                </div>
                            </div>
                        </li>
                        <button onClick={addToCartHandler} className="primary block">Add to Cart</button>



                    </ul >
                </div >
            </div >
        </div >
    )
}
