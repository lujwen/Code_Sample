deployed db url:

DATABASE_URL='mysql://k1dain6m3810:pscale_pw__e-9SxTBprpNXkes8cAPJBbgZbg229NcCY5a2haBVXk@cgy9o8j2qnz3.us-east-3.psdb.cloud/deploy-test?sslaccept=strict'

Please note on db tables: since we made a change to the checkout functionality, we are using the Transactions table to store cart information instead of Cart table. 

deployed API url: http://shopdb-demo-345.ue.r.appspot.com


Our website is to complete online shopping platform for client to free sign up and sign in, order items online and pay online.

People can visit the website without sign in and they can see complete operation without sign in.

After sign up and sign in, you can update your profile though the welcome button on the top of the right under the cart.

Click the product name or photo can transfer to the product detail page and people can check the detail of the item and add them to cart.

After signing in and adding item to cart, you can check the cart link on home page to transfer to the cart page, on the cart page, you can remove items that you don't want it anymore, or click the checkout button to complete the shopping.

After checkout, you can see the address of our store, and the google map API to check where it is, and after that, our cart will be empty and the operation finish at this point.

Frontend deploy link: https://frontend-test-lujwen.vercel.app/, for our cody, some page and link just work locally, so the deploy may have some operations link to empty page or caused error.
