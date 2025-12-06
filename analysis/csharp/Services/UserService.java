using com.test.Models;
using System.Collections.Generic;
using System.Threading.Tasks;

using com.test.Enums;

namespace com.test.Services
{
    public interface IUserService
    {
        Task<IEnumerable<User>> GetAllAsync();
        Task<User?> GetByIdAsync(int id);
        Task<User> CreateAsync(User entity);
        Task<User> UpdateAsync(int id, User entity);
        Task DeleteAsync(int id);
        Task<User> SuspendUserAsync(int id);
        Task<User> ActivateUserAsync(int id);
    }
}


using com.test.Models;
using com.test.Repositories;
using System.Collections.Generic;
using System.Threading.Tasks;
using System;

using com.test.Enums;

namespace com.test.Services
{
    public class UserService : IUserService
    {
        private readonly IUserRepository _repository;

        public UserService(IUserRepository repository)
        {
            _repository = repository;
        }

        public async Task<IEnumerable<User>> GetAllAsync()
        {
            return await _repository.GetAllAsync();
        }

        public async Task<User?> GetByIdAsync(int id)
        {
            return await _repository.GetByIdAsync(id);
        }

        public async Task<User> CreateAsync(User entity)
        {
            // Add business logic and validation here
            ValidateEntity(entity);
            
            return await _repository.CreateAsync(entity);
        }

        public async Task<User> UpdateAsync(int id, User entity)
        {
            var existing = await _repository.GetByIdAsync(id);
            if (existing == null)
            {
                throw new ArgumentException($"User with id {id} not found");
            }
            
            // Add business logic and validation here
            ValidateEntity(entity);
            
            // Update properties
            entity.Id = id;
            return await _repository.UpdateAsync(entity);
        }

        public async Task DeleteAsync(int id)
        {
            var exists = await _repository.ExistsAsync(id);
            if (!exists)
            {
                throw new ArgumentException($"User with id {id} not found");
            }
            
            await _repository.DeleteAsync(id);
        }

        public async Task<User> SuspendUserAsync(int id)
        {
            var entity = await _repository.GetByIdAsync(id);
            if (entity == null)
            {
                throw new ArgumentException($"User with id {id} not found");
            }
            
            entity.Suspend();
            return await _repository.UpdateAsync(entity);
        }

        public async Task<User> ActivateUserAsync(int id)
        {
            var entity = await _repository.GetByIdAsync(id);
            if (entity == null)
            {
                throw new ArgumentException($"User with id {id} not found");
            }
            
            entity.Activate();
            return await _repository.UpdateAsync(entity);
        }

        private void ValidateEntity(User entity)
        {
            if (entity == null)
            {
                throw new ArgumentNullException(nameof(entity));
            }
            
            // Add custom validation logic here
        }
    }
}
