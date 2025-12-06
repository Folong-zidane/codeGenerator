import { Repository } from 'typeorm';
import { User } from '../entities/User';
import { AppDataSource } from '../data-source';

export interface IUserRepository {
  findAll(): Promise<User[]>;
  findById(id: number): Promise<User | null>;
  create(entity: Partial<User>): Promise<User>;
  update(id: number, entity: Partial<User>): Promise<User | null>;
  delete(id: number): Promise<void>;
  exists(id: number): Promise<boolean>;
}

export class UserRepository implements IUserRepository {
  private repository: Repository<User>;

  constructor() {
    this.repository = AppDataSource.getRepository(User);
  }

  async findAll(): Promise<User[]> {
    return await this.repository.find();
  }

  async findById(id: number): Promise<User | null> {
    return await this.repository.findOne({ where: { id } });
  }

  async create(entity: Partial<User>): Promise<User> {
    const newEntity = this.repository.create(entity);
    return await this.repository.save(newEntity);
  }

  async update(id: number, entity: Partial<User>): Promise<User | null> {
    await this.repository.update(id, entity);
    return await this.findById(id);
  }

  async delete(id: number): Promise<void> {
    await this.repository.delete(id);
  }

  async exists(id: number): Promise<boolean> {
    const count = await this.repository.count({ where: { id } });
    return count > 0;
  }
}
