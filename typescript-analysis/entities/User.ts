import { Entity, PrimaryGeneratedColumn, Column, CreateDateColumn, UpdateDateColumn } from 'typeorm';
import { UserStatus } from '../enums/UserStatus';

@Entity('users')
export class User {
  @PrimaryGeneratedColumn()
  id: string;

  @Column()
  username: string;

  @Column({ type: 'enum', enum: UserStatus })
  status: UserStatus;

  @CreateDateColumn()
  createdAt: Date;

  @UpdateDateColumn()
  updatedAt: Date;

  suspend(): void {
    if (this.status !== UserStatus.ACTIVE) {
      throw new Error(`Cannot suspend entity in state: ${this.status}`);
    }
    this.status = UserStatus.SUSPENDED;
    this.updatedAt = new Date();
  }

  activate(): void {
    if (this.status !== UserStatus.SUSPENDED) {
      throw new Error(`Cannot activate entity in state: ${this.status}`);
    }
    this.status = UserStatus.ACTIVE;
    this.updatedAt = new Date();
  }

}
