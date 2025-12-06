import { Product } from '../entities/Product';
import { ProductRepository } from '../repositories/ProductRepository';

export class ProductService {
  private repository: ProductRepository;

  constructor() {
    this.repository = new ProductRepository();
  }

  async create(entity: Product): Promise<Product> {
    return await this.repository.save(entity);
  }

  async findById(id: string): Promise<Product | null> {
    return await this.repository.findById(id);
  }

  async findAll(): Promise<Product[]> {
    return await this.repository.findAll();
  }

  async update(id: string, entity: Partial<Product>): Promise<Product | null> {
    const existing = await this.repository.findById(id);
    if (!existing) return null;

    Object.assign(existing, entity);
    return await this.repository.save(existing);
  }

  async delete(id: string): Promise<void> {
    await this.repository.delete(id);
  }
}
