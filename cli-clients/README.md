# CLI Clients pour UML-to-CRUD Generator

## 🎯 Concept Important

Les **CLI clients** sont des **outils d'interface** pour communiquer avec votre API Spring Boot. Ils ne génèrent pas directement le code, mais appellent l'API qui génère le code.

## 📋 Workflow

```
1. Votre API Spring Boot (port 8080) ← Le générateur principal
2. CLI Client (Python/Node/Java/Go) ← Outil d'interface  
3. Projet généré (Spring Boot/Django/Flask/.NET) ← Résultat final
```

## 🛠️ CLI Clients Disponibles

### Python CLI (Recommandé)
```bash
cd python-client
pip install -r requirements.txt

# Générer Spring Boot
python crud_generator_cli.py generate \
  --uml ../../examples/sample-diagram.mermaid \
  --output ./mon-spring-boot \
  --framework spring-boot

# Générer Django  
python crud_generator_cli.py generate \
  --uml ../../examples/sample-diagram.mermaid \
  --output ./mon-django \
  --framework django
```

### Node.js CLI
```bash
cd node-client
npm install

# Générer Flask
node crud-generator.js generate \
  --uml ../../examples/sample-diagram.mermaid \
  --output ./mon-flask \
  --framework flask
```

### Java CLI
```bash
cd java-client
javac -cp ".:picocli-4.7.5.jar" CrudGeneratorCLI.java

# Générer .NET
java -cp ".:picocli-4.7.5.jar" com.basiccode.cli.CrudGeneratorCLI generate \
  --uml ../../examples/sample-diagram.mermaid \
  --output ./mon-dotnet \
  --framework dotnet
```

### Go CLI
```bash
cd go-client
go mod init crud-generator-cli
go run main.go \
  --cmd generate \
  --uml ../../examples/sample-diagram.mermaid \
  --output ./mon-express \
  --framework express
```

## 🌐 Frameworks Supportés

Tous les CLI peuvent générer **tous les frameworks** :

| Framework | Description | Commande |
|-----------|-------------|----------|
| `spring-boot` | Java Spring Boot avec Maven | `--framework spring-boot` |
| `django` | Python Django avec DRF | `--framework django` |
| `flask` | Python Flask avec SQLAlchemy | `--framework flask` |
| `dotnet` | C# ASP.NET Core | `--framework dotnet` |
| `express` | Node.js Express | `--framework express` |

## 💡 Exemples Concrets

### CLI Python → Projet Spring Boot
```bash
python crud_generator_cli.py generate \
  --uml ecommerce.mermaid \
  --output /home/user/ecommerce-spring \
  --framework spring-boot \
  --package com.ecommerce

# Résultat: Projet Spring Boot complet
cd /home/user/ecommerce-spring
mvn spring-boot:run
```

### CLI Go → Projet Django
```bash
go run main.go \
  --cmd generate \
  --uml blog.mermaid \
  --output /home/user/blog-django \
  --framework django

# Résultat: Projet Django complet  
cd /home/user/blog-django
pip install -r requirements.txt
python manage.py runserver
```

## 🔧 Pourquoi Plusieurs CLI ?

- **Flexibilité** : Chaque développeur utilise son langage préféré
- **Intégration** : S'intègre dans différents workflows
- **Déploiement** : Distribuer selon l'environnement cible

## ⚡ Démarrage Rapide

1. **Lancez l'API** :
   ```bash
   cd ../..
   mvn spring-boot:run
   ```

2. **Utilisez n'importe quel CLI** :
   ```bash
   # Python CLI
   cd python-client
   python crud_generator_cli.py generate --uml ../../examples/sample-diagram.mermaid --output ./test --framework spring-boot
   ```

3. **Votre projet est généré** dans `./test/` !

## 📝 Note

**Python CLI ≠ Django uniquement**  
Le CLI Python peut générer Spring Boot, Django, Flask, .NET, Express !

C'est juste l'**outil d'interface**, pas le **langage de sortie**.