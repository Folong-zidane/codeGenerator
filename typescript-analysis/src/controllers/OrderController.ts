import { Router, Request, Response } from 'express';
import { Order } from '../entities/Order';
import { OrderService } from '../services/OrderService';

export class OrderController {
  public router: Router;
  private service: OrderService;

  constructor() {
    this.router = Router();
    this.service = new OrderService();
    this.initializeRoutes();
  }

  private initializeRoutes(): void {
    this.router.post('/', this.create.bind(this));
    this.router.get('/:id', this.getById.bind(this));
    this.router.get('/', this.getAll.bind(this));
    this.router.put('/:id', this.update.bind(this));
    this.router.delete('/:id', this.delete.bind(this));
  }

  private async create(req: Request, res: Response): Promise<void> {
    try {
      const entity = await this.service.create(req.body);
      res.status(201).json(entity);
    } catch (error) {
      res.status(500).json({ error: 'Internal server error' });
    }
  }

  private async getById(req: Request, res: Response): Promise<void> {
    try {
      const entity = await this.service.findById(req.params.id);
      if (!entity) {
        res.status(404).json({ error: 'Not found' });
        return;
      }
      res.json(entity);
    } catch (error) {
      res.status(500).json({ error: 'Internal server error' });
    }
  }

  private async getAll(req: Request, res: Response): Promise<void> {
    try {
      const entities = await this.service.findAll();
      res.json(entities);
    } catch (error) {
      res.status(500).json({ error: 'Internal server error' });
    }
  }

  private async update(req: Request, res: Response): Promise<void> {
    try {
      const entity = await this.service.update(req.params.id, req.body);
      if (!entity) {
        res.status(404).json({ error: 'Not found' });
        return;
      }
      res.json(entity);
    } catch (error) {
      res.status(500).json({ error: 'Internal server error' });
    }
  }

  private async delete(req: Request, res: Response): Promise<void> {
    try {
      await this.service.delete(req.params.id);
      res.status(204).send();
    } catch (error) {
      res.status(500).json({ error: 'Internal server error' });
    }
  }
}
