import { Order } from '../entities/Order';
import { OrderRepository } from '../repositories/OrderRepository';

export class OrderService {
  private repository: OrderRepository;

  constructor() {
    this.repository = new OrderRepository();
  }

  async create(entity: Order): Promise<Order> {
    return await this.repository.save(entity);
  }

  async findById(id: string): Promise<Order | null> {
    return await this.repository.findById(id);
  }

  async findAll(): Promise<Order[]> {
    return await this.repository.findAll();
  }

  async update(id: string, entity: Partial<Order>): Promise<Order | null> {
    const existing = await this.repository.findById(id);
    if (!existing) return null;

    Object.assign(existing, entity);
    return await this.repository.save(existing);
  }

  async delete(id: string): Promise<void> {
    await this.repository.delete(id);
  }
}
