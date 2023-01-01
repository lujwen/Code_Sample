import React from "react";


export default function Product(prod) {
    const { product } = prod;
    return (
        <div key={product["id"]} className="card">
            <a href={`/product/${product["id"]}`}>
                <img className="medium" src={product.image} alt={product.name}></img>
            </a>
            <div className="card-body">
                <a href={`/product/${product["id"]}`}>
                    <h2>{product["name"]}</h2>
                </a>
            </div>
            <div className="card-body price">
                <a href={`/product/${product["id"]}`}>
                    <h2>Current Price: ${product["price"]}</h2>
                </a>
            </div>
        </div>
    );
}
