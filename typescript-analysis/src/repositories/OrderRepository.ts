import { Repository } from 'typeorm';
import { AppDataSource } from '../config/database';
import { Order } from '../entities/Order';

export class OrderRepository {
  private repository: Repository<Order>;

  constructor() {
    this.repository = AppDataSource.getRepository(Order);
  }

  async findById(id: string): Promise<Order | null> {
    return await this.repository.findOne({ where: { id } });
  }

  async findAll(): Promise<Order[]> {
    return await this.repository.find();
  }

  async save(entity: Order): Promise<Order> {
    return await this.repository.save(entity);
  }

  async delete(id: string): Promise<void> {
    await this.repository.delete(id);
  }
}
