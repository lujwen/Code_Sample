import express from "express";
import pkg from "@prisma/client";

const productRouter = express.Router();

const { PrismaClient } = pkg;
const prisma = new PrismaClient();

// CRUD - Create, Return, Update, Delete
// POST: creates new product
productRouter.post("/createProduct", async (req, res) => {
  const { name, image, price, stockCount, description } = req.body;
  if (name.length <= 0 || name.length > 50
    || description.length <= 0 || description.length > 500) {
    res.status(406).send("Invalid name and/or description input.");
  } else if (stockCount < 0) {
    res.status(406).send("Invalid stock count input.");
  } else {
    const productItem = await prisma.Product.create({
      data: {
        name, image, price, stockCount, description
      },
    });
    res.json(productItem);
  }
});

// GET: return all products in database
productRouter.get("/showAllProducts", async (req, res) => {
  const productItems = await prisma.Product.findMany();
  res.json(productItems);
});

// GET: return product info for product
productRouter.get("/getProduct", async (req, res) => {
  const product_id = parseInt(req.query.id);
  const productInfo = await prisma.Product.findUnique({
    where: {
      id: product_id
    }
  });
  res.json(productInfo);
});


// GET: return all products in a certain cartId
productRouter.get("/productsInCart", async (req, res) => {
  const { cartId } = req.body;
  const productItems = await prisma.Product.findMany({
    where: {
      cartId: {
        equals: cartId
      }
    }
  });
  res.json(productItems);
});

// UPDATE: update a product's cartId by id
productRouter.put("/updateProduct", async (req, res) => {
  const { id, cartId } = req.body;
  const currProduct = await prisma.Product.update({
    where: {
      id
    },
    data: {
      cartId
    }
  });
  res.json(currProduct);
});

// DELETE: deletes a product by product id
// after an order containing a product is completed (order placed),
// this function should be called to remove the product from db
productRouter.delete("/deleteProduct", async (req, res) => {
  const { id } = req.body;
  const product = await prisma.Product.delete({
    where: {
      id
    }
  });
  const currProducts = await prisma.Product.findMany();
  res.json(currProducts);
});

export default productRouter;
