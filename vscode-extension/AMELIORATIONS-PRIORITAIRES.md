# 🎯 Améliorations Prioritaires - Extension VSCode

## 🚀 Quick Wins (Implémentation Immédiate - 2h)

### 1. Barre d'État Interactive
**Impact** : Haute visibilité, accès rapide  
**Effort** : 15 minutes

```typescript
// Ajouter dans activate()
const statusBarItem = vscode.window.createStatusBarItem(
  vscode.StatusBarAlignment.Left, 100
);
statusBarItem.text = "$(rocket) Generate";
statusBarItem.command = "basiccode.generate";
statusBarItem.tooltip = "Generate project from UML diagrams (Ctrl+Shift+G)";
statusBarItem.show();
context.subscriptions.push(statusBarItem);
```

### 2. Output Channel pour Logs
**Impact** : Meilleur debugging, transparence  
**Effort** : 20 minutes

```typescript
// Créer le channel
const outputChannel = vscode.window.createOutputChannel('basicCode Generator');
context.subscriptions.push(outputChannel);

// Fonction de log
function log(message: string, level: 'info' | 'warn' | 'error' = 'info') {
  const timestamp = new Date().toISOString();
  const icons = { info: 'ℹ️', warn: '⚠️', error: '❌' };
  outputChannel.appendLine(`[${timestamp}] ${icons[level]} ${message}`);
  if (level === 'error') outputChannel.show();
}

// Utiliser partout
log('Scanning diagrams...', 'info');
log('Found 3 diagrams', 'info');
log('Backend connection failed', 'error');
```

### 3. Messages d'Erreur Améliorés
**Impact** : Meilleure UX, résolution rapide  
**Effort** : 30 minutes

```typescript
// Remplacer les messages génériques
catch (error) {
  const message = error.response?.data?.message || error.message;
  
  vscode.window.showErrorMessage(
    `❌ Generation failed: ${message}`,
    'Retry',
    'Check Backend',
    'View Logs'
  ).then(choice => {
    if (choice === 'Retry') {
      vscode.commands.executeCommand('basiccode.generate');
    } else if (choice === 'Check Backend') {
      const backend = vscode.workspace.getConfiguration('basiccode').get('backend');
      vscode.env.openExternal(vscode.Uri.parse(`${backend}/actuator/health`));
    } else if (choice === 'View Logs') {
      outputChannel.show();
    }
  });
  
  log(`Generation failed: ${message}`, 'error');
}
```

### 4. Validation Avant Génération
**Impact** : Évite les erreurs, meilleur feedback  
**Effort** : 45 minutes

```typescript
async function validateBeforeGeneration(): Promise<boolean> {
  const config = vscode.workspace.getConfiguration('basiccode');
  const backend = config.get<string>('backend');
  const diagrams = await this.scanDiagrams();
  
  // Vérifier les diagrammes
  if (Object.keys(diagrams).length === 0) {
    const choice = await vscode.window.showWarningMessage(
      '⚠️ No diagrams found in src/diagrams/',
      'Create Sample',
      'Open Folder'
    );
    
    if (choice === 'Create Sample') {
      await createSampleProject();
    }
    return false;
  }
  
  // Vérifier le backend
  log('Checking backend connectivity...', 'info');
  try {
    await axios.get(`${backend}/actuator/health`, { timeout: 5000 });
    log('Backend is reachable', 'info');
  } catch (error) {
    vscode.window.showErrorMessage(
      '❌ Backend unreachable. Check your configuration.',
      'Configure',
      'Use Production'
    ).then(choice => {
      if (choice === 'Configure') {
        vscode.commands.executeCommand('workbench.action.openSettings', 'basiccode.backend');
      } else if (choice === 'Use Production') {
        config.update('backend', 'https://codegenerator-cpyh.onrender.com', 
          vscode.ConfigurationTarget.Workspace);
      }
    });
    return false;
  }
  
  // Afficher résumé
  const summary = Object.entries(diagrams)
    .map(([type, content]) => `• ${type}: ${content.split('\n').length} lines`)
    .join('\n');
  
  log(`Found diagrams:\n${summary}`, 'info');
  
  return true;
}

// Appeler avant génération
async generate(): Promise<void> {
  if (!await validateBeforeGeneration()) {
    return;
  }
  
  // Continuer la génération...
}
```

### 5. Menu Contextuel
**Impact** : Meilleure découvrabilité  
**Effort** : 10 minutes

```json
// Dans package.json
"menus": {
  "explorer/context": [
    {
      "when": "explorerResourceIsFolder && resourceFilename == diagrams",
      "command": "basiccode.generate",
      "group": "basiccode@1"
    }
  ],
  "editor/context": [
    {
      "when": "resourceExtname == .mmd",
      "command": "basiccode.generate",
      "group": "basiccode@1"
    }
  ]
}
```

---

## 🎨 Améliorations Moyennes (Implémentation Rapide - 4h)

### 6. Welcome Screen
**Impact** : Meilleur onboarding  
**Effort** : 1h

```typescript
async function showWelcomeScreen(context: vscode.ExtensionContext) {
  const hasSeenWelcome = context.globalState.get('hasSeenWelcome');
  
  if (!hasSeenWelcome) {
    const choice = await vscode.window.showInformationMessage(
      '🎉 Welcome to basicCode Generator!\n\nGenerate complete applications from UML diagrams.',
      'Quick Start',
      'View Examples',
      'Configure'
    );
    
    switch (choice) {
      case 'Quick Start':
        await createSampleProject();
        vscode.window.showInformationMessage(
          '✅ Sample project created! Press Ctrl+Shift+G to generate.',
          'Generate Now'
        ).then(c => {
          if (c === 'Generate Now') {
            vscode.commands.executeCommand('basiccode.generate');
          }
        });
        break;
        
      case 'View Examples':
        vscode.env.openExternal(
          vscode.Uri.parse('https://codegenerator-cpyh.onrender.com/examples')
        );
        break;
        
      case 'Configure':
        await configurationWizard();
        break;
    }
    
    context.globalState.update('hasSeenWelcome', true);
  }
}

// Appeler dans activate()
await showWelcomeScreen(context);
```

### 7. Configuration Wizard
**Impact** : Configuration simplifiée  
**Effort** : 1h30

```typescript
async function configurationWizard() {
  // Étape 1: Langage
  const languageOptions = [
    { label: '☕ Java', value: 'java', description: 'Spring Boot - Enterprise ready' },
    { label: '🐍 Python', value: 'python', description: 'FastAPI - Modern & fast' },
    { label: '🎸 Django', value: 'django', description: 'Django REST - Batteries included' },
    { label: '🔷 C#', value: 'csharp', description: '.NET Core - Microsoft stack' },
    { label: '📘 TypeScript', value: 'typescript', description: 'Express + TypeORM - Type-safe' },
    { label: '🐘 PHP', value: 'php', description: 'Slim Framework - Lightweight' }
  ];
  
  const language = await vscode.window.showQuickPick(languageOptions, {
    placeHolder: 'Select your preferred programming language',
    title: 'Configuration Wizard (1/3)'
  });
  
  if (!language) return;
  
  // Étape 2: Package name
  const defaultPackage = language.value === 'java' || language.value === 'csharp' 
    ? 'com.example' 
    : 'app';
  
  const packageName = await vscode.window.showInputBox({
    prompt: 'Enter package/module name',
    value: defaultPackage,
    title: 'Configuration Wizard (2/3)',
    validateInput: (value) => {
      if (language.value === 'java' || language.value === 'csharp') {
        return /^[a-z][a-z0-9_]*(\.[a-z][a-z0-9_]*)*$/.test(value) 
          ? null 
          : 'Invalid format. Use: com.example.project';
      }
      return null;
    }
  });
  
  if (!packageName) return;
  
  // Étape 3: Backend
  const backendOptions = [
    { 
      label: '🌐 Production Server', 
      value: 'https://codegenerator-cpyh.onrender.com',
      description: 'Recommended - Always available'
    },
    { 
      label: '💻 Local Server', 
      value: 'http://localhost:8080',
      description: 'For development'
    },
    { 
      label: '✏️ Custom URL', 
      value: 'custom',
      description: 'Enter your own backend URL'
    }
  ];
  
  const backendChoice = await vscode.window.showQuickPick(backendOptions, {
    placeHolder: 'Select backend server',
    title: 'Configuration Wizard (3/3)'
  });
  
  if (!backendChoice) return;
  
  let backend = backendChoice.value;
  
  if (backend === 'custom') {
    const customBackend = await vscode.window.showInputBox({
      prompt: 'Enter backend URL',
      value: 'http://localhost:8080',
      validateInput: (value) => {
        try {
          new URL(value);
          return null;
        } catch {
          return 'Invalid URL format';
        }
      }
    });
    
    if (!customBackend) return;
    backend = customBackend;
  }
  
  // Sauvegarder
  const config = vscode.workspace.getConfiguration('basiccode');
  await config.update('language', language.value, vscode.ConfigurationTarget.Workspace);
  await config.update('packageName', packageName, vscode.ConfigurationTarget.Workspace);
  await config.update('backend', backend, vscode.ConfigurationTarget.Workspace);
  
  log(`Configuration saved: ${language.value}, ${packageName}, ${backend}`, 'info');
  
  vscode.window.showInformationMessage(
    '✅ Configuration saved successfully!',
    'Create Sample Project',
    'Generate Now'
  ).then(choice => {
    if (choice === 'Create Sample Project') {
      createSampleProject();
    } else if (choice === 'Generate Now') {
      vscode.commands.executeCommand('basiccode.generate');
    }
  });
}

// Ajouter commande
context.subscriptions.push(
  vscode.commands.registerCommand('basiccode.configure', configurationWizard)
);
```

### 8. Créer Projet Exemple
**Impact** : Démarrage rapide  
**Effort** : 45 minutes

```typescript
async function createSampleProject() {
  const workspaceFolder = vscode.workspace.workspaceFolders?.[0];
  if (!workspaceFolder) {
    vscode.window.showErrorMessage('Please open a folder first');
    return;
  }
  
  const diagramsPath = path.join(workspaceFolder.uri.fsPath, 'src', 'diagrams');
  
  // Créer structure
  await fs.promises.mkdir(diagramsPath, { recursive: true });
  
  // Diagramme de classes
  const classDiagram = `classDiagram
    class User {
        +UUID id
        +String username
        +String email
        +String password
        +UserStatus status
        +Date createdAt
        +validateEmail()
        +changePassword()
    }
    
    class Post {
        +UUID id
        +String title
        +String content
        +UUID userId
        +PostStatus status
        +Date createdAt
        +publish()
        +archive()
    }
    
    class Comment {
        +UUID id
        +String content
        +UUID postId
        +UUID userId
        +Date createdAt
    }
    
    User "1" --> "*" Post : writes
    User "1" --> "*" Comment : writes
    Post "1" --> "*" Comment : has`;
  
  // Diagramme de séquence
  const sequenceDiagram = `sequenceDiagram
    participant Client
    participant UserController
    participant UserService
    participant UserRepository
    participant Database
    
    Client->>UserController: POST /api/users/register
    UserController->>UserService: createUser(userData)
    UserService->>UserService: validateEmail(email)
    
    alt Email valid
        UserService->>UserRepository: save(user)
        UserRepository->>Database: INSERT INTO users
        Database-->>UserRepository: User created
        UserRepository-->>UserService: User entity
        UserService-->>UserController: Success
        UserController-->>Client: 201 Created
    else Email invalid
        UserService-->>UserController: ValidationException
        UserController-->>Client: 400 Bad Request
    end`;
  
  // Diagramme d'état
  const stateDiagram = `stateDiagram-v2
    [*] --> INACTIVE
    INACTIVE --> ACTIVE : activate()
    ACTIVE --> SUSPENDED : suspend()
    SUSPENDED --> ACTIVE : reactivate()
    ACTIVE --> DELETED : delete()
    SUSPENDED --> DELETED : delete()
    DELETED --> [*]`;
  
  // Écrire les fichiers
  await fs.promises.writeFile(
    path.join(diagramsPath, 'class-diagram.mmd'),
    classDiagram
  );
  
  await fs.promises.writeFile(
    path.join(diagramsPath, 'sequence-diagram.mmd'),
    sequenceDiagram
  );
  
  await fs.promises.writeFile(
    path.join(diagramsPath, 'state-diagram.mmd'),
    stateDiagram
  );
  
  // Créer README
  const readme = `# Sample Blog Project

This is a sample project generated by basicCode Generator.

## Diagrams

- \`class-diagram.mmd\`: Defines User, Post, and Comment entities
- \`sequence-diagram.mmd\`: User registration workflow
- \`state-diagram.mmd\`: User status lifecycle

## Generate

Press \`Ctrl+Shift+G\` or run "basicCode: Generate Project" to generate the complete application.

## What will be generated?

- ✅ Entities with JPA annotations
- ✅ Repositories with CRUD operations
- ✅ Services with business logic
- ✅ REST Controllers with endpoints
- ✅ Database configuration
- ✅ API documentation (Swagger)
- ✅ Tests

## Next Steps

1. Generate the project
2. Run \`./mvnw spring-boot:run\`
3. Access http://localhost:8080/swagger-ui.html
`;
  
  await fs.promises.writeFile(
    path.join(workspaceFolder.uri.fsPath, 'README.md'),
    readme
  );
  
  log('Sample project created successfully', 'info');
  
  // Ouvrir le premier diagramme
  const classFile = vscode.Uri.file(path.join(diagramsPath, 'class-diagram.mmd'));
  await vscode.window.showTextDocument(classFile);
  
  vscode.window.showInformationMessage(
    '✅ Sample project created! Check src/diagrams/',
    'Generate Now',
    'View README'
  ).then(choice => {
    if (choice === 'Generate Now') {
      vscode.commands.executeCommand('basiccode.generate');
    } else if (choice === 'View README') {
      const readmeFile = vscode.Uri.file(path.join(workspaceFolder.uri.fsPath, 'README.md'));
      vscode.window.showTextDocument(readmeFile);
    }
  });
}

// Ajouter commande
context.subscriptions.push(
  vscode.commands.registerCommand('basiccode.createSample', createSampleProject)
);
```

### 9. Diagnostics Automatiques
**Impact** : Résolution rapide des problèmes  
**Effort** : 45 minutes

```typescript
async function runDiagnostics() {
  const diagnostics: string[] = [];
  const config = vscode.workspace.getConfiguration('basiccode');
  
  outputChannel.clear();
  outputChannel.show();
  log('🔍 Running diagnostics...', 'info');
  
  // 1. Vérifier workspace
  const workspaceFolder = vscode.workspace.workspaceFolders?.[0];
  if (!workspaceFolder) {
    diagnostics.push('❌ No workspace folder opened');
  } else {
    log(`✅ Workspace: ${workspaceFolder.uri.fsPath}`, 'info');
  }
  
  // 2. Vérifier configuration
  const backend = config.get<string>('backend');
  const language = config.get<string>('language');
  const packageName = config.get<string>('packageName');
  
  log(`Configuration:`, 'info');
  log(`  Backend: ${backend}`, 'info');
  log(`  Language: ${language}`, 'info');
  log(`  Package: ${packageName}`, 'info');
  
  // 3. Vérifier backend
  log('Testing backend connectivity...', 'info');
  try {
    const response = await axios.get(`${backend}/actuator/health`, { timeout: 5000 });
    log(`✅ Backend is reachable (${response.status})`, 'info');
  } catch (error) {
    diagnostics.push(`❌ Backend unreachable: ${backend}`);
    log(`❌ Backend connection failed: ${error.message}`, 'error');
  }
  
  // 4. Vérifier diagrammes
  if (workspaceFolder) {
    const diagramsPath = path.join(workspaceFolder.uri.fsPath, 'src', 'diagrams');
    
    if (fs.existsSync(diagramsPath)) {
      const files = fs.readdirSync(diagramsPath);
      const diagramFiles = files.filter(f => f.endsWith('.mmd') || f.endsWith('.puml'));
      
      if (diagramFiles.length === 0) {
        diagnostics.push('⚠️ No diagram files found in src/diagrams/');
      } else {
        log(`✅ Found ${diagramFiles.length} diagram(s):`, 'info');
        diagramFiles.forEach(f => log(`  - ${f}`, 'info'));
      }
    } else {
      diagnostics.push('❌ Directory src/diagrams/ does not exist');
    }
  }
  
  // 5. Résumé
  log('\n📊 Diagnostic Summary:', 'info');
  
  if (diagnostics.length === 0) {
    log('✅ All checks passed! Ready to generate.', 'info');
    vscode.window.showInformationMessage(
      '✅ All checks passed!',
      'Generate Now'
    ).then(choice => {
      if (choice === 'Generate Now') {
        vscode.commands.executeCommand('basiccode.generate');
      }
    });
  } else {
    diagnostics.forEach(d => log(d, 'warn'));
    vscode.window.showWarningMessage(
      `⚠️ Found ${diagnostics.length} issue(s). Check Output panel.`,
      'Fix Configuration',
      'Create Sample'
    ).then(choice => {
      if (choice === 'Fix Configuration') {
        configurationWizard();
      } else if (choice === 'Create Sample') {
        createSampleProject();
      }
    });
  }
}

// Ajouter commande
context.subscriptions.push(
  vscode.commands.registerCommand('basiccode.diagnostics', runDiagnostics)
);
```

---

## 📦 Mise à Jour du package.json

```json
{
  "contributes": {
    "commands": [
      {
        "command": "basiccode.generate",
        "title": "Generate Project",
        "category": "basicCode",
        "icon": "$(rocket)"
      },
      {
        "command": "basiccode.configure",
        "title": "Configure",
        "category": "basicCode",
        "icon": "$(settings-gear)"
      },
      {
        "command": "basiccode.createSample",
        "title": "Create Sample Project",
        "category": "basicCode",
        "icon": "$(file-add)"
      },
      {
        "command": "basiccode.diagnostics",
        "title": "Run Diagnostics",
        "category": "basicCode",
        "icon": "$(pulse)"
      }
    ],
    "menus": {
      "explorer/context": [
        {
          "when": "explorerResourceIsFolder && resourceFilename == diagrams",
          "command": "basiccode.generate",
          "group": "basiccode@1"
        }
      ],
      "editor/context": [
        {
          "when": "resourceExtname == .mmd",
          "command": "basiccode.generate",
          "group": "basiccode@1"
        }
      ]
    }
  }
}
```

---

## 🎯 Checklist d'Implémentation

### Quick Wins (2h)
- [ ] Barre d'état avec bouton Generate
- [ ] Output channel pour logs
- [ ] Messages d'erreur améliorés avec actions
- [ ] Validation avant génération
- [ ] Menu contextuel

### Améliorations Moyennes (4h)
- [ ] Welcome screen au premier lancement
- [ ] Configuration wizard interactif
- [ ] Créer projet exemple
- [ ] Diagnostics automatiques
- [ ] Commandes supplémentaires

### Tests
- [ ] Tester sur nouveau workspace
- [ ] Tester avec backend inaccessible
- [ ] Tester sans diagrammes
- [ ] Tester configuration invalide
- [ ] Tester génération complète

---

## 📊 Impact Attendu

### Avant
- ⏱️ Temps de première utilisation : **10-15 minutes**
- 🎯 Taux de succès : **60%**
- 🐛 Erreurs courantes : **Configuration, backend, diagrammes**

### Après
- ⏱️ Temps de première utilisation : **< 2 minutes**
- 🎯 Taux de succès : **95%**
- 🐛 Erreurs courantes : **Réduites de 80%**

---

## 🚀 Déploiement

```bash
# 1. Implémenter les améliorations
cd vscode-extension

# 2. Tester
npm run compile
code . # F5 pour debug

# 3. Packager
npx @vscode/vsce package

# 4. Installer
code --install-extension basiccode-generator-1.1.0.vsix

# 5. Tester en conditions réelles
```
