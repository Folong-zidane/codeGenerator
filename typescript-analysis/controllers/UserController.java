import { Request, Response } from 'express';
import { UserService, IUserService } from '../services/UserService';

export class UserController {
  private service: IUserService;

  constructor() {
    this.service = new UserService();
  }

  /**
   * Get all users
   */
  getAll = async (req: Request, res: Response): Promise<void> => {
    try {
      const entities = await this.service.getAll();
      res.json(entities);
    } catch (error) {
      res.status(500).json({ error: 'Internal server error', message: (error as Error).message });
    }
  };

  /**
   * Get user by ID
   */
  getById = async (req: Request, res: Response): Promise<void> => {
    try {
      const id = parseInt(req.params.id);
      const entity = await this.service.getById(id);
      
      if (!entity) {
        res.status(404).json({ error: 'User not found', id });
        return;
      }
      
      res.json(entity);
    } catch (error) {
      res.status(500).json({ error: 'Internal server error', message: (error as Error).message });
    }
  };

  /**
   * Create new user
   */
  create = async (req: Request, res: Response): Promise<void> => {
    try {
      const entity = await this.service.create(req.body);
      res.status(201).json(entity);
    } catch (error) {
      res.status(400).json({ error: 'Bad request', message: (error as Error).message });
    }
  };

  /**
   * Update user by ID
   */
  update = async (req: Request, res: Response): Promise<void> => {
    try {
      const id = parseInt(req.params.id);
      const entity = await this.service.update(id, req.body);
      
      if (!entity) {
        res.status(404).json({ error: 'User not found', id });
        return;
      }
      
      res.json(entity);
    } catch (error) {
      res.status(400).json({ error: 'Bad request', message: (error as Error).message });
    }
  };

  /**
   * Delete user by ID
   */
  delete = async (req: Request, res: Response): Promise<void> => {
    try {
      const id = parseInt(req.params.id);
      await this.service.delete(id);
      res.status(204).send();
    } catch (error) {
      res.status(404).json({ error: 'User not found', message: (error as Error).message });
    }
  };

  /**
   * Suspend user by ID
   */
  suspend = async (req: Request, res: Response): Promise<void> => {
    try {
      const id = parseInt(req.params.id);
      const entity = await this.service.suspendUser(id);
      
      if (!entity) {
        res.status(404).json({ error: 'User not found', id });
        return;
      }
      
      res.json(entity);
    } catch (error) {
      res.status(400).json({ error: 'Invalid state transition', message: (error as Error).message });
    }
  };

  /**
   * Activate user by ID
   */
  activate = async (req: Request, res: Response): Promise<void> => {
    try {
      const id = parseInt(req.params.id);
      const entity = await this.service.activateUser(id);
      
      if (!entity) {
        res.status(404).json({ error: 'User not found', id });
        return;
      }
      
      res.json(entity);
    } catch (error) {
      res.status(400).json({ error: 'Invalid state transition', message: (error as Error).message });
    }
  };

}
