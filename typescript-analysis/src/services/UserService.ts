import { User } from '../entities/User';
import { UserRepository } from '../repositories/UserRepository';

export class UserService {
  private repository: UserRepository;

  constructor() {
    this.repository = new UserRepository();
  }

  async create(entity: User): Promise<User> {
    return await this.repository.save(entity);
  }

  async findById(id: string): Promise<User | null> {
    return await this.repository.findById(id);
  }

  async findAll(): Promise<User[]> {
    return await this.repository.findAll();
  }

  async update(id: string, entity: Partial<User>): Promise<User | null> {
    const existing = await this.repository.findById(id);
    if (!existing) return null;

    Object.assign(existing, entity);
    return await this.repository.save(existing);
  }

  async delete(id: string): Promise<void> {
    await this.repository.delete(id);
  }
}
