import 'reflect-metadata';
import { DataSource } from 'typeorm';
import { User } from './entities/User';

export const AppDataSource = new DataSource({
  type: 'postgres',
  host: process.env.DB_HOST || 'localhost',
  port: parseInt(process.env.DB_PORT || '5432'),
  username: process.env.DB_USERNAME || 'postgres',
  password: process.env.DB_PASSWORD || 'password',
  database: process.env.DB_NAME || 'com.test',
  synchronize: process.env.NODE_ENV === 'development',
  logging: process.env.NODE_ENV === 'development',
  entities: [User],
  migrations: ['src/migrations/*.ts'],
  subscribers: ['src/subscribers/*.ts'],
});


import express from 'express';
import cors from 'cors';
import helmet from 'helmet';
import { AppDataSource } from './data-source';
import { setupRoutes } from './routes';

const app = express();
const PORT = process.env.PORT || 3000;

// Middleware
app.use(helmet());
app.use(cors());
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// Routes
setupRoutes(app);

// Health check
app.get('/health', (req, res) => {
  res.json({ status: 'OK', timestamp: new Date().toISOString() });
});

// Error handling
app.use((err: Error, req: express.Request, res: express.Response, next: express.NextFunction) => {
  console.error(err.stack);
  res.status(500).json({ error: 'Internal server error' });
});

// Start server
AppDataSource.initialize()
  .then(() => {
    console.log('📊 Database connected successfully');
    
    app.listen(PORT, () => {
      console.log(`🚀 Server running on port ${PORT}`);
      console.log(`📖 API documentation: http://localhost:${PORT}/api`);
    });
  })
  .catch((error) => {
    console.error('❌ Database connection failed:', error);
    process.exit(1);
  });

export default app;


import { Express, Router } from 'express';
import { UserController } from './controllers/UserController';

export function setupRoutes(app: Express): void {
  const apiRouter = Router();

  // User routes
  const userController = new UserController();
  const userRouter = Router();
  userRouter.get('/', userController.getAll);
  userRouter.get('/:id', userController.getById);
  userRouter.post('/', userController.create);
  userRouter.put('/:id', userController.update);
  userRouter.delete('/:id', userController.delete);
  userRouter.patch('/:id/suspend', userController.suspend);
  userRouter.patch('/:id/activate', userController.activate);
  apiRouter.use('/users', userRouter);

  app.use('/api', apiRouter);
}
