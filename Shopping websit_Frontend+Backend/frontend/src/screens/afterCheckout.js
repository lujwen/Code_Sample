import MyComponents from "../components/map";
import React from "react";

export default function AfterCheckout() {
    return (
        <div className="intro">
            <h1>Congratulations on your reservation! ✨ </h1>
            <p> 
            Please visit us at 4 N 2nd St, San Jose, CA 95113 within 14 days to 
            pick up your order. We look forward to seeing you in person. </p >
            <section className='hidden'>
                <a href=' '>(Address of our store)</a >
                <MyComponents />
            </section>
        </div>
    )
}
