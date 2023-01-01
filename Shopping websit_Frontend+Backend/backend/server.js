import http from 'http';
import { Server } from 'socket.io';
import express from 'express';
import data from './data.js';
import userRouter from './userAPI.js';
import productRouter from './productAPI.js';
import cartRouter from './cartAPI.js';
import path from 'path';
import { fileURLToPath } from 'url';
import orderRouter from './orderAPI.js';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const app = express();
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

app.get('/api/products', (req, res) => {
    res.send(data.products);
});

app.use('/api/users', userRouter);
// localhost:5000/api/product/methodnam
app.use('/api/product', productRouter);
app.use('/api/cart', cartRouter);
app.use('/api/order', orderRouter)
console.log(path.join(__dirname, "..", '/frontend/public/images'));
app.use('/images', express.static(path.join(__dirname, "..", '/frontend/public/images')));


app.get('/', (req, res) => {
    res.send('Server is ready');
});

console.log(path.join(__dirname, "..", '/frontend/public/images'));
app.use('/images', express.static(path.join(__dirname, "..", '/frontend/public/images')));

const port = process.env.PORT || 5000;

const httpServer = http.Server(app);
const io = new Server(httpServer, { cors: { origin: '*' } });
const users = [];

io.on('connection', (socket) => {
    console.log('connection', socket.id);
    socket.on('disconnect', () => {
        const user = users.find((x) => x.socketId === socket.id);
        if (user) {
            user.online = false;
            console.log('Offline', user.name);
            const admin = users.find((x) => x.isAdmin && x.online);
            if (admin) {
                io.to(admin.socketId).emit('updateUser', user);
            }
        }
    });
    socket.on('onLogin', (user) => {
        const updatedUser = {
            ...user,
            online: true,
            socketId: socket.id,
            messages: [],
        };
        const existUser = users.find((x) => x._id === updatedUser._id);
        if (existUser) {
            existUser.socketId = socket.id;
            existUser.online = true;
        } else {
            users.push(updatedUser);
        }
        console.log('Online', user.name);
        const admin = users.find((x) => x.isAdmin && x.online);
        if (admin) {
            io.to(admin.socketId).emit('updateUser', updatedUser);
        }
        if (updatedUser.isAdmin) {
            io.to(updatedUser.socketId).emit('listUsers', users);
        }
    });

    socket.on('onUserSelected', (user) => {
        const admin = users.find((x) => x.isAdmin && x.online);
        if (admin) {
            const existUser = users.find((x) => x._id === user._id);
            io.to(admin.socketId).emit('selectUser', existUser);
        }
    });

    socket.on('onMessage', (message) => {
        if (message.isAdmin) {
            const user = users.find((x) => x._id === message._id && x.online);
            if (user) {
                io.to(user.socketId).emit('message', message);
                user.messages.push(message);
            }
        } else {
            const admin = users.find((x) => x.isAdmin && x.online);
            if (admin) {
                io.to(admin.socketId).emit('message', message);
                const user = users.find((x) => x._id === message._id && x.online);
                user.messages.push(message);
            } else {
                io.to(socket.id).emit('message', {
                    name: 'Admin',
                    body: 'Sorry. I am not online right now',
                });
            }
        }
    });
});

httpServer.listen(port, () => {
    console.log(`Serve at http://localhost:${port}`);
});