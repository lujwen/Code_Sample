import express from "express";
import pkg from "@prisma/client";

const orderRouter = express.Router();

const { PrismaClient } = pkg;
const prisma = new PrismaClient();

// CRUD - Create, Return, Update, Delete
// POST: creates new user
orderRouter.post("/createOrder", async (req, res) => {
    const { orderId, productId, userId, price, qty } = req.body;
    if (qty <= 0) {
        res.status(406).send("Invalid quantity.");
    } else {
        const newOrder = await prisma.Transaction.create({
            data: {
                orderId,
                productId,
                userId,
                price,
                qty
            },
        });
        res.json(newOrder);
    }
});

// GET: return all users in database
orderRouter.get("/showOrders", async (req, res) => {
    const user_id = req.query.userId;
    const allOrders = await prisma.Order.findMany({
        where: {
            userId: user_id
        }
    }
    );
    res.json(allOrders);
});


export default orderRouter;
