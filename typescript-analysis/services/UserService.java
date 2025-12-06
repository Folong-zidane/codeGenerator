import { User } from '../entities/User';
import { UserRepository, IUserRepository } from '../repositories/UserRepository';

export interface IUserService {
  getAll(): Promise<User[]>;
  getById(id: number): Promise<User | null>;
  create(data: Partial<User>): Promise<User>;
  update(id: number, data: Partial<User>): Promise<User | null>;
  delete(id: number): Promise<void>;
  suspendUser(id: number): Promise<User | null>;
  activateUser(id: number): Promise<User | null>;
}

export class UserService implements IUserService {
  private repository: IUserRepository;

  constructor() {
    this.repository = new UserRepository();
  }

  async getAll(): Promise<User[]> {
    return await this.repository.findAll();
  }

  async getById(id: number): Promise<User | null> {
    return await this.repository.findById(id);
  }

  async create(data: Partial<User>): Promise<User> {
    this.validateEntity(data);
    return await this.repository.create(data);
  }

  async update(id: number, data: Partial<User>): Promise<User | null> {
    const existing = await this.repository.findById(id);
    if (!existing) {
      throw new Error(`User with id ${id} not found`);
    }
    
    this.validateEntity(data);
    return await this.repository.update(id, data);
  }

  async delete(id: number): Promise<void> {
    const exists = await this.repository.exists(id);
    if (!exists) {
      throw new Error(`User with id ${id} not found`);
    }
    
    await this.repository.delete(id);
  }

  async suspendUser(id: number): Promise<User | null> {
    const entity = await this.repository.findById(id);
    if (!entity) {
      throw new Error(`User with id ${id} not found`);
    }
    
    entity.suspend();
    return await this.repository.update(id, entity);
  }

  async activateUser(id: number): Promise<User | null> {
    const entity = await this.repository.findById(id);
    if (!entity) {
      throw new Error(`User with id ${id} not found`);
    }
    
    entity.activate();
    return await this.repository.update(id, entity);
  }

  private validateEntity(data: Partial<User>): void {
    if (!data) {
      throw new Error('Entity data is required');
    }
    // Add custom validation logic here
  }
}
