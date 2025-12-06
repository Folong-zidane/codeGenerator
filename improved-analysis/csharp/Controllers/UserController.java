using com.improved.test.Models;
using com.improved.test.Services;
using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;
using System.Threading.Tasks;
using System;

using com.improved.test.Enums;

namespace com.improved.test.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class UserController : ControllerBase
    {
        private readonly IUserService _service;

        public UserController(IUserService service)
        {
            _service = service;
        }

        /// <summary>
        /// Get all users
        /// </summary>
        [HttpGet]
        public async Task<ActionResult<IEnumerable<User>>> GetAll()
        {
            try
            {
                var entities = await _service.GetAllAsync();
                return Ok(entities);
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { error = "Internal server error", message = ex.Message });
            }
        }

        /// <summary>
        /// Get user by ID
        /// </summary>
        [HttpGet("{id}")]
        public async Task<ActionResult<User>> GetById(int id)
        {
            try
            {
                var entity = await _service.GetByIdAsync(id);
                if (entity == null)
                {
                    return NotFound(new { error = "User not found", id });
                }
                return Ok(entity);
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { error = "Internal server error", message = ex.Message });
            }
        }

        /// <summary>
        /// Create new user
        /// </summary>
        [HttpPost]
        public async Task<ActionResult<User>> Create([FromBody] User entity)
        {
            try
            {
                if (!ModelState.IsValid)
                {
                    return BadRequest(ModelState);
                }
                
                var created = await _service.CreateAsync(entity);
                return CreatedAtAction(nameof(GetById), new { id = created.Id }, created);
            }
            catch (ArgumentException ex)
            {
                return BadRequest(new { error = "Validation error", message = ex.Message });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { error = "Internal server error", message = ex.Message });
            }
        }

        /// <summary>
        /// Update user by ID
        /// </summary>
        [HttpPut("{id}")]
        public async Task<ActionResult<User>> Update(int id, [FromBody] User entity)
        {
            try
            {
                if (!ModelState.IsValid)
                {
                    return BadRequest(ModelState);
                }
                
                var updated = await _service.UpdateAsync(id, entity);
                return Ok(updated);
            }
            catch (ArgumentException ex)
            {
                return NotFound(new { error = "Entity not found", message = ex.Message });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { error = "Internal server error", message = ex.Message });
            }
        }

        /// <summary>
        /// Delete user by ID
        /// </summary>
        [HttpDelete("{id}")]
        public async Task<IActionResult> Delete(int id)
        {
            try
            {
                await _service.DeleteAsync(id);
                return NoContent();
            }
            catch (ArgumentException ex)
            {
                return NotFound(new { error = "Entity not found", message = ex.Message });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { error = "Internal server error", message = ex.Message });
            }
        }

        /// <summary>
        /// Suspend user by ID
        /// </summary>
        [HttpPatch("{id}/suspend")]
        public async Task<ActionResult<User>> Suspend(int id)
        {
            try
            {
                var entity = await _service.SuspendUserAsync(id);
                return Ok(entity);
            }
            catch (ArgumentException ex)
            {
                return NotFound(new { error = "Entity not found", message = ex.Message });
            }
            catch (InvalidOperationException ex)
            {
                return BadRequest(new { error = "Invalid state transition", message = ex.Message });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { error = "Internal server error", message = ex.Message });
            }
        }

        /// <summary>
        /// Activate user by ID
        /// </summary>
        [HttpPatch("{id}/activate")]
        public async Task<ActionResult<User>> Activate(int id)
        {
            try
            {
                var entity = await _service.ActivateUserAsync(id);
                return Ok(entity);
            }
            catch (ArgumentException ex)
            {
                return NotFound(new { error = "Entity not found", message = ex.Message });
            }
            catch (InvalidOperationException ex)
            {
                return BadRequest(new { error = "Invalid state transition", message = ex.Message });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { error = "Internal server error", message = ex.Message });
            }
        }

    }
}
