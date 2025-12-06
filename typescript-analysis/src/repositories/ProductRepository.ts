import { Repository } from 'typeorm';
import { AppDataSource } from '../config/database';
import { Product } from '../entities/Product';

export class ProductRepository {
  private repository: Repository<Product>;

  constructor() {
    this.repository = AppDataSource.getRepository(Product);
  }

  async findById(id: string): Promise<Product | null> {
    return await this.repository.findOne({ where: { id } });
  }

  async findAll(): Promise<Product[]> {
    return await this.repository.find();
  }

  async save(entity: Product): Promise<Product> {
    return await this.repository.save(entity);
  }

  async delete(id: string): Promise<void> {
    await this.repository.delete(id);
  }
}
