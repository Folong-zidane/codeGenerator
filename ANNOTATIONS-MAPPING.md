# 🏷️ Mapping des Annotations par Langage

## 📊 Tableau de Correspondance

| Concept | Java (JPA) | Python (SQLAlchemy) | C# (EF Core) | TypeScript (TypeORM) | PHP (Doctrine) |
|---------|------------|-------------------|--------------|---------------------|----------------|
| **Entité** | `@Entity` | `class User(Base)` | `[Table("users")]` | `@Entity()` | `@Entity` |
| **Table** | `@Table(name="users")` | `__tablename__ = 'users'` | `[Table("users")]` | `@Table("users")` | `@Table(name="users")` |
| **Clé Primaire** | `@Id @GeneratedValue` | `Column(primary_key=True)` | `[Key]` | `@PrimaryGeneratedColumn()` | `@Id @GeneratedValue` |
| **Colonne** | `@Column(name="email")` | `Column(String(255))` | `[Column("email")]` | `@Column()` | `@Column(name="email")` |
| **Relation 1:N** | `@OneToMany` | `relationship("Order")` | `public List<Order> Orders` | `@OneToMany()` | `@OneToMany` |
| **Relation N:1** | `@ManyToOne` | `Column(ForeignKey('users.id'))` | `[ForeignKey("UserId")]` | `@ManyToOne()` | `@ManyToOne` |
| **Énumération** | `@Enumerated(STRING)` | `Enum` | `enum UserStatus` | `enum UserStatus` | `@Column(type="string")` |
| **Validation** | `@NotNull @Size` | `nullable=False` | `[Required]` | `@IsNotEmpty()` | `@Assert\NotBlank` |
| **Audit** | `@CreationTimestamp` | `default=datetime.utcnow` | `DateTime.UtcNow` | `@CreateDateColumn()` | `@Gedmo\Timestampable` |

## 🎯 Exemples Concrets par Langage

### Java Spring Boot
```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;
    
    @Column(name = "username", nullable = false, unique = true)
    @Size(min = 3, max = 50)
    private String username;
    
    @Enumerated(EnumType.STRING)
    private UserStatus status;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
}
```

### Python SQLAlchemy
```python
class User(Base):
    __tablename__ = 'users'
    
    id = Column(UUID(as_uuid=True), primary_key=True, default=uuid.uuid4)
    username = Column(String(50), nullable=False, unique=True)
    status = Column(Enum(UserStatus))
    created_at = Column(DateTime, default=datetime.utcnow)
    
    orders = relationship("Order", back_populates="user")
```

### C# Entity Framework
```csharp
[Table("users")]
public class User
{
    [Key]
    public Guid Id { get; set; }
    
    [Column("username")]
    [Required]
    [StringLength(50)]
    public string Username { get; set; }
    
    public UserStatus Status { get; set; }
    
    public List<Order> Orders { get; set; }
    
    public DateTime CreatedAt { get; set; } = DateTime.UtcNow;
}
```

### TypeScript TypeORM
```typescript
@Entity("users")
export class User {
    @PrimaryGeneratedColumn("uuid")
    id: string;
    
    @Column({ length: 50, unique: true })
    @IsNotEmpty()
    username: string;
    
    @Column({ type: "enum", enum: UserStatus })
    status: UserStatus;
    
    @OneToMany(() => Order, order => order.user)
    orders: Order[];
    
    @CreateDateColumn()
    createdAt: Date;
}
```

### PHP Doctrine
```php
/**
 * @Entity
 * @Table(name="users")
 */
class User
{
    /**
     * @Id
     * @GeneratedValue
     * @Column(type="uuid")
     */
    private $id;
    
    /**
     * @Column(type="string", length=50, unique=true)
     * @Assert\NotBlank
     * @Assert\Length(min=3, max=50)
     */
    private $username;
    
    /**
     * @Column(type="string", enumType="UserStatus")
     */
    private $status;
    
    /**
     * @OneToMany(targetEntity="Order", mappedBy="user")
     */
    private $orders;
    
    /**
     * @Gedmo\Timestampable(on="create")
     * @Column(type="datetime")
     */
    private $createdAt;
}
```

## 🔧 Configuration des Générateurs

### Mise à jour des adaptateurs pour annotations correctes

Chaque `LanguageGeneratorFactory` doit mapper les types UML vers les annotations appropriées :

- **Java** : JPA + Bean Validation
- **Python** : SQLAlchemy + Pydantic
- **C#** : Entity Framework + Data Annotations
- **TypeScript** : TypeORM + class-validator
- **PHP** : Doctrine + Symfony Validator

## 🧪 Tests de Conformité

Le script `test-generation.sh` vérifie :

1. **Génération réussie** pour chaque langage
2. **Annotations présentes** dans le code généré
3. **Structure de projet** conforme aux conventions
4. **Compilation/syntaxe** valide (si outils disponibles)

## 🚀 Commandes de Test

```bash
# Lancer tous les tests
./test-generation.sh

# Test spécifique Java
curl -X POST "http://localhost:8080/api/comprehensive/generate" \
  -H "Content-Type: application/json" \
  -d @test-request.json \
  -o java-project.zip

# Vérifier annotations Java
unzip -q java-project.zip
grep -r "@Entity\|@Table\|@Column" . | head -10
```

Les 6 fichiers de diagrammes couvrent tous les aspects : **structure**, **comportement**, **états**, **workflows**, **données** et **architecture** ! 🎯