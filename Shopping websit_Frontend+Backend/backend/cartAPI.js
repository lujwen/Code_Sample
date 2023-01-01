import express from "express";
import pkg from "@prisma/client";

const cartRouter = express.Router();

const { PrismaClient } = pkg;
const prisma = new PrismaClient();

// CRUD - Create, Return, Update, Delete
// POST: creates new cart
cartRouter.post("/createCart", async (req, res) => {
  const { userId } = req.body;
  console.log("userId");
  console.log(userId);
  const newCart = await prisma.Cart.create({
    data: {
      userId
    },
  });
  res.json(newCart);
});

// 

// GET: return all carts in database
cartRouter.get("/showAllCarts", async (req, res) => {
  const allCarts = await prisma.Cart.findMany();
  res.json(allCarts);
});

// UPDATE: update a cart's userId
cartRouter.put("/updateCart", async (req, res) => {
  const { id, userId } = req.body;
  const currCart = await prisma.Cart.update({
    where: {
      id
    },
    data: {
      userId
    }
  });
  res.json(currCart);
});

// DELETE: deletes a cart by cart id
cartRouter.delete("/deleteCart", async (req, res) => {
  const { id } = req.body;
  const cart = await prisma.Cart.delete({
    where: {
      id
    }
  });
  const currCarts = await prisma.Cart.findMany();
  res.json(currCarts);
});

export default cartRouter;

