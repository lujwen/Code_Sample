import express from "express";
import pkg from "@prisma/client";
import bcrypt from 'bcryptjs';

const userRouter = express.Router();

const { PrismaClient } = pkg;
const prisma = new PrismaClient();

// CRUD - Create, Return, Update, Delete
// POST: creates new user
userRouter.post("/createUser", async (req, res) => {
  const { email, password, firstname, lastname, username } = req.body;
  const encry_password = bcrypt.hashSync(password, 8)
  if (firstname.length <= 0 || firstname.length > 50
    || lastname.length <= 0 || lastname.length > 50) {
    res.status(406).send("Invalid first name and/or last name.");
  } else if (!email.includes("@")) {
    res.status(406).send("Invalid email address.");
  } else if (username.length <= 0 || username.length >= 50) {
    res.status(406).send("Invalid username.");
  } else {
    const newUser = await prisma.User.create({
      data: {
        email: email,
        password: encry_password,
        firstname: firstname,
        lastname: lastname,
        username: username
      },
    });
    res.json(newUser);
  }
});

// GET: return all users in database
userRouter.get("/showUser", async (req, res) => {
  const allUsers = await prisma.User.findMany();
  res.json(allUsers);
});

// GET:check user sign in
userRouter.post("/login", async (req, res) => {
  console.log(req.body);
  const user = await prisma.User.findUnique({
    where: {
      email: req.body.email,
    },
  });
  if (user) {
    if (bcrypt.compareSync(req.body.password, user.password)) {
      res.send({
        id: user.id,
        name: user.username,
        email: user.email,
        firstname: user.firstname,
        lastname: user.lastname
      });
      return;
    }
  }
  res.status(401).send({ message: 'Invalid email or password' });
});

// UPDATE: update an user's email, password, firstname, lastname, 
// username, address  by id
userRouter.put("/updateUser", async (req, res) => {
  const { id, email, firstname, lastname, username } = req.body;
  const currUser = await prisma.User.update({
    where: {
      id
    },
    data: {
      id: id,
      email: email,
      firstname: firstname,
      lastname: lastname,
      username: username
    }
  });
  res.json(currUser);
});

// DELETE: deletes an user by user id
userRouter.delete("/delteUser", async (req, res) => {
  const { id } = req.body;
  const user = await prisma.User.delete({
    where: {
      id
    }
  });
  const currUsers = await prisma.User.findMany();
  res.json(currUsers);
});

// GET: get user cart_id
userRouter.get("/getUserCart", async (req, res) => {
  const id = parseInt(req.query.id);
  const currUser = await prisma.User.findUnique({
    where: {
      id
    }
  });
  res.json(currUser);
});

export default userRouter;
