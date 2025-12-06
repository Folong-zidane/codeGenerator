using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System;

using com.improved.test.Enums;

namespace com.improved.test.Models
{
    [Table("users")]
    public class User
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public string Id { get; set; }

        public string Username { get; set; }

        public string Email { get; set; }

        [Column("status")]
        public UserStatus Status { get; set; }

        [Column("created_at")]
        public DateTime CreatedAt { get; set; } = DateTime.UtcNow;

        [Column("updated_at")]
        public DateTime UpdatedAt { get; set; } = DateTime.UtcNow;

        public bool ValidateEmail()
        {
            if (string.IsNullOrWhiteSpace(Email))
            {
                throw new ArgumentException("Email cannot be empty");
            }
            var emailRegex = @"^[A-Za-z0-9+_.-]+@(.+)$";
            return System.Text.RegularExpressions.Regex.IsMatch(Email, emailRegex);
        }

        public void ChangePassword(string newPassword)
        {
            if (string.IsNullOrEmpty(newPassword) || newPassword.Length < 8)
            {
                throw new ArgumentException("Password must be at least 8 characters");
            }
            // TODO: Hash password with BCrypt
            UpdatedAt = DateTime.UtcNow;
        }

        public void Suspend()
        {
            if (Status != UserStatus.ACTIVE)
            {
                throw new InvalidOperationException($"Cannot suspend entity in state: {Status}");
            }
            Status = UserStatus.SUSPENDED;
            UpdatedAt = DateTime.UtcNow;
        }

        public void Activate()
        {
            if (Status != UserStatus.SUSPENDED)
            {
                throw new InvalidOperationException($"Cannot activate entity in state: {Status}");
            }
            Status = UserStatus.ACTIVE;
            UpdatedAt = DateTime.UtcNow;
        }

    }
}
