using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System;

using com.test.Enums;

namespace com.test.Models
{
    [Table("users")]
    public class User
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public string Id { get; set; }

        public string Username { get; set; }

        [Column("status")]
        public UserStatus Status { get; set; }

        [Column("created_at")]
        public DateTime CreatedAt { get; set; } = DateTime.UtcNow;

        [Column("updated_at")]
        public DateTime UpdatedAt { get; set; } = DateTime.UtcNow;

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
