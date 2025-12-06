import 'reflect-metadata';
import express from 'express';
import { AppDataSource } from './config/database';
import { UserController } from './controllers/UserController';
import { ProductController } from './controllers/ProductController';
import { OrderController } from './controllers/OrderController';


const app = express();
const PORT = process.env.PORT || 3000;

app.use(express.json());

// Initialize database
AppDataSource.initialize()
  .then(() => {
    console.log('Database connected successfully');
  })
  .catch((error) => console.log('Database connection error:', error));

// Routes
app.use('/api/user', new UserController().router);
app.use('/api/product', new ProductController().router);
app.use('/api/order', new OrderController().router);


app.get('/', (req, res) => {
  res.json({ message: 'Generated TypeScript API is running', docs: '/api-docs' });
});

app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});
