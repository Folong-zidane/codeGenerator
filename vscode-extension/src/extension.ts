import * as vscode from 'vscode';
import * as fs from 'fs';
import * as path from 'path';
import axios from 'axios';
const AdmZip = require('adm-zip');

// Output channel global
let outputChannel: vscode.OutputChannel;

// Fonction de log
function log(message: string, level: 'info' | 'warn' | 'error' = 'info') {
    const timestamp = new Date().toISOString();
    const icons = { info: 'ℹ️', warn: '⚠️', error: '❌' };
    outputChannel.appendLine(`[${timestamp}] ${icons[level]} ${message}`);
    if (level === 'error') {
        outputChannel.show();
    }
}

// Watch mode
let watcher: vscode.FileSystemWatcher | undefined;

export function activate(context: vscode.ExtensionContext) {
    
    // Créer l'output channel
    outputChannel = vscode.window.createOutputChannel('basicCode Generator');
    context.subscriptions.push(outputChannel);
    
    log('Extension basicCode Generator activated', 'info');
    
    // Créer la barre d'état
    const statusBarItem = vscode.window.createStatusBarItem(
        vscode.StatusBarAlignment.Left, 100
    );
    statusBarItem.text = "$(rocket) Generate";
    statusBarItem.command = "basiccode.generate";
    statusBarItem.tooltip = "Generate project from UML diagrams (Ctrl+Shift+G)";
    statusBarItem.show();
    context.subscriptions.push(statusBarItem);
    
    // Welcome screen au premier lancement
    showWelcomeScreen(context);
    
    // Commande: Generate
    context.subscriptions.push(
        vscode.commands.registerCommand('basiccode.generate', async () => {
            const workspaceFolder = vscode.workspace.workspaceFolders?.[0];
            if (!workspaceFolder) {
                log('No workspace folder found', 'error');
                vscode.window.showErrorMessage('No workspace folder found');
                return;
            }
            const generator = new ProjectGenerator(workspaceFolder.uri.fsPath, context);
            await generator.generate();
        })
    );
    
    // Commande: Configure
    context.subscriptions.push(
        vscode.commands.registerCommand('basiccode.configure', async () => {
            await configurationWizard();
        })
    );
    
    // Commande: Watch Mode Toggle
    context.subscriptions.push(
        vscode.commands.registerCommand('basiccode.toggleWatch', async () => {
            if (watcher) {
                disableWatchMode();
            } else {
                enableWatchMode();
            }
        })
    );
    
    // Commande: Generate with Git Commit
    context.subscriptions.push(
        vscode.commands.registerCommand('basiccode.generateWithCommit', async () => {
            await generateWithGitCommit();
        })
    );
}

// Welcome Screen
async function showWelcomeScreen(context: vscode.ExtensionContext) {
    const hasSeenWelcome = context.globalState.get('hasSeenWelcome');
    
    if (!hasSeenWelcome) {
        const choice = await vscode.window.showInformationMessage(
            '🎉 Welcome to basicCode Generator!',
            'Quick Start',
            'Configure',
            'Later'
        );
        
        if (choice === 'Quick Start') {
            const workspaceFolder = vscode.workspace.workspaceFolders?.[0];
            if (workspaceFolder) {
                const generator = new ProjectGenerator(workspaceFolder.uri.fsPath, context);
                await generator.createSampleProject();
            }
        } else if (choice === 'Configure') {
            await configurationWizard();
        }
        
        context.globalState.update('hasSeenWelcome', true);
    }
}

// Configuration Wizard
async function configurationWizard() {
    // Étape 1: Langage
    const language = await vscode.window.showQuickPick(
        [
            { label: '☕ Java', value: 'java', description: 'Spring Boot' },
            { label: '🐍 Python', value: 'python', description: 'FastAPI' },
            { label: '🎸 Django', value: 'django', description: 'Django REST' },
            { label: '🔷 C#', value: 'csharp', description: '.NET Core' },
            { label: '📘 TypeScript', value: 'typescript', description: 'Express + TypeORM' },
            { label: '🐘 PHP', value: 'php', description: 'Slim Framework' }
        ],
        { placeHolder: 'Select your preferred language' }
    );
    
    if (!language) return;
    
    // Étape 2: Package name
    const packageName = await vscode.window.showInputBox({
        prompt: 'Enter package name',
        value: 'com.example',
        validateInput: (value) => {
            if (language.value === 'java' || language.value === 'csharp') {
                return /^[a-z][a-z0-9_]*(\.[a-z][a-z0-9_]*)*$/.test(value) 
                    ? null 
                    : 'Invalid package name format';
            }
            return null;
        }
    });
    
    if (!packageName) return;
    
    // Étape 3: Backend
    const backend = await vscode.window.showQuickPick(
        [
            { label: '🌐 Production', value: 'https://codegenerator-cpyh.onrender.com' },
            { label: '💻 Local', value: 'http://localhost:8080' }
        ],
        { placeHolder: 'Select backend server' }
    );
    
    if (!backend) return;
    
    // Sauvegarder
    const config = vscode.workspace.getConfiguration('basiccode');
    await config.update('language', language.value, vscode.ConfigurationTarget.Workspace);
    await config.update('packageName', packageName, vscode.ConfigurationTarget.Workspace);
    await config.update('backend', backend.value, vscode.ConfigurationTarget.Workspace);
    
    log(`Configuration saved: ${language.value}, ${packageName}, ${backend.value}`, 'info');
    vscode.window.showInformationMessage('✅ Configuration saved!');
}

// Watch Mode
function enableWatchMode() {
    const workspaceFolder = vscode.workspace.workspaceFolders?.[0];
    if (!workspaceFolder) return;
    
    const pattern = new vscode.RelativePattern(workspaceFolder, 'src/diagrams/**/*.{mmd,puml}');
    watcher = vscode.workspace.createFileSystemWatcher(pattern);
    
    watcher.onDidChange(async (uri) => {
        const choice = await vscode.window.showInformationMessage(
            `📝 Diagram changed: ${path.basename(uri.fsPath)}`,
            'Regenerate',
            'Ignore'
        );
        
        if (choice === 'Regenerate') {
            await vscode.commands.executeCommand('basiccode.generate');
        }
    });
    
    log('Watch mode enabled', 'info');
    vscode.window.showInformationMessage('👁️ Watch mode enabled');
}

function disableWatchMode() {
    if (watcher) {
        watcher.dispose();
        watcher = undefined;
        log('Watch mode disabled', 'info');
        vscode.window.showInformationMessage('👁️ Watch mode disabled');
    }
}

// Generate with Git Commit
async function generateWithGitCommit() {
    await vscode.commands.executeCommand('basiccode.generate');
    
    const choice = await vscode.window.showInformationMessage(
        '✅ Project generated! Commit changes?',
        'Commit',
        'Skip'
    );
    
    if (choice === 'Commit') {
        const message = await vscode.window.showInputBox({
            prompt: 'Enter commit message',
            value: 'chore: regenerate project from UML diagrams'
        });
        
        if (message) {
            const terminal = vscode.window.createTerminal('Git Commit');
            terminal.sendText('git add .');
            terminal.sendText(`git commit -m "${message}"`);
            terminal.show();
        }
    }
}

class ProjectGenerator {
    private workspacePath: string;
    private config: any;
    private context: vscode.ExtensionContext;
    
    constructor(workspacePath: string, context: vscode.ExtensionContext) {
        this.workspacePath = workspacePath;
        this.config = vscode.workspace.getConfiguration('basiccode');
        this.context = context;
    }
    
    async generate(): Promise<void> {
        try {
            log('Starting generation...', 'info');
            
            // Validation avant génération
            if (!await this.validateBeforeGeneration()) {
                return;
            }
            
            // 1. Scanner les diagrammes
            const diagrams = await this.scanDiagrams();
            if (Object.keys(diagrams).length === 0) {
                log('No diagrams found', 'warn');
                vscode.window.showWarningMessage('No diagrams found in src/diagrams/');
                return;
            }
            
            log(`Found ${Object.keys(diagrams).length} diagram(s)`, 'info');
            
            // 2. Afficher progress
            await vscode.window.withProgress({
                location: vscode.ProgressLocation.Notification,
                title: "Generating project...",
                cancellable: true
            }, async (progress, token) => {
                
                progress.report({ increment: 10, message: "Uploading diagrams..." });
                
                // 3. Initier génération
                const generationId = await this.initiateGeneration(diagrams);
                
                progress.report({ increment: 20, message: "Processing on server..." });
                
                // 4. Attendre completion
                await this.waitForCompletion(generationId, progress);
                
                progress.report({ increment: 30, message: "Downloading..." });
                
                // 5. Télécharger et merger
                await this.downloadAndMerge(generationId, progress);
                
                progress.report({ increment: 100, message: "Complete!" });
            });
            
            vscode.window.showInformationMessage('✅ Project generated successfully!');
            
        } catch (error: any) {
            const message = error.response?.data?.message || error.message || 'Unknown error';
            
            log(`Generation failed: ${message}`, 'error');
            
            vscode.window.showErrorMessage(
                `❌ Generation failed: ${message}`,
                'Retry',
                'Check Backend',
                'View Logs'
            ).then(choice => {
                if (choice === 'Retry') {
                    vscode.commands.executeCommand('basiccode.generate');
                } else if (choice === 'Check Backend') {
                    const backend = this.config.get('backend');
                    vscode.env.openExternal(vscode.Uri.parse(`${backend}/actuator/health`));
                } else if (choice === 'View Logs') {
                    outputChannel.show();
                }
            });
        }
    }
    
    private async validateBeforeGeneration(): Promise<boolean> {
        const backend = this.config.get('backend') as string;
        
        log('Starting validation...', 'info');
        
        // 1. Vérifier les diagrammes
        const diagrams = await this.scanDiagrams();
        
        if (Object.keys(diagrams).length === 0) {
            log('No diagrams found', 'warn');
            
            const choice = await vscode.window.showWarningMessage(
                '⚠️ No diagrams found in src/diagrams/',
                'Create Sample',
                'Open Folder',
                'Cancel'
            );
            
            if (choice === 'Create Sample') {
                await this.createSampleProject();
                return false;
            } else if (choice === 'Open Folder') {
                const diagramsPath = path.join(this.workspacePath, 'src', 'diagrams');
                if (!fs.existsSync(diagramsPath)) {
                    fs.mkdirSync(diagramsPath, { recursive: true });
                }
                await vscode.commands.executeCommand('revealFileInOS', 
                    vscode.Uri.file(diagramsPath)
                );
                return false;
            }
            
            return false;
        }
        
        log(`Found ${Object.keys(diagrams).length} diagram(s)`, 'info');
        
        // 2. Vérifier le backend
        log('Checking backend connectivity...', 'info');
        
        try {
            const response = await axios.get(`${backend}/actuator/health`, { 
                timeout: 5000 
            });
            
            if (response.status === 200) {
                log('Backend is reachable', 'info');
            }
        } catch (error) {
            log('Backend unreachable', 'error');
            
            const choice = await vscode.window.showErrorMessage(
                '❌ Backend unreachable. Check your configuration.',
                'Configure',
                'Use Production',
                'Continue Anyway'
            );
            
            if (choice === 'Configure') {
                await vscode.commands.executeCommand(
                    'workbench.action.openSettings', 
                    'basiccode.backend'
                );
                return false;
            } else if (choice === 'Use Production') {
                await this.config.update(
                    'backend', 
                    'https://codegenerator-cpyh.onrender.com',
                    vscode.ConfigurationTarget.Workspace
                );
                log('Backend updated to production', 'info');
                return true;
            } else if (choice === 'Continue Anyway') {
                log('Continuing despite backend unreachable', 'warn');
                return true;
            }
            
            return false;
        }
        
        // 3. Afficher résumé
        const summary = Object.entries(diagrams)
            .map(([type, content]) => `• ${type}: ${content.split('\n').length} lines`)
            .join('\n');
        
        log(`Diagrams summary:\n${summary}`, 'info');
        
        return true;
    }
    
    async createSampleProject(): Promise<void> {
        const diagramsPath = path.join(this.workspacePath, 'src', 'diagrams');
        
        await fs.promises.mkdir(diagramsPath, { recursive: true });
        
        const sampleDiagram = `classDiagram
    class User {
        +UUID id
        +String username
        +String email
    }
    class Post {
        +UUID id
        +String title
        +UUID userId
    }
    User "1" --> "*" Post`;
        
        await fs.promises.writeFile(
            path.join(diagramsPath, 'class-diagram.mmd'),
            sampleDiagram
        );
        
        log('Sample project created', 'info');
        
        vscode.window.showInformationMessage(
            '✅ Sample project created!',
            'Generate Now'
        ).then(choice => {
            if (choice === 'Generate Now') {
                vscode.commands.executeCommand('basiccode.generate');
            }
        });
    }
    
    private async scanDiagrams(): Promise<Record<string, string>> {
        const diagramsPath = path.join(this.workspacePath, 'src', 'diagrams');
        const diagrams: Record<string, string> = {};
        
        log(`Scanning diagrams in: ${diagramsPath}`, 'info');
        
        if (!fs.existsSync(diagramsPath)) {
            log('Diagrams directory does not exist', 'warn');
            return diagrams;
        }
        
        const files = fs.readdirSync(diagramsPath);
        
        for (const file of files) {
            if (file.endsWith('.mmd') || file.endsWith('.puml')) {
                const filePath = path.join(diagramsPath, file);
                const content = fs.readFileSync(filePath, 'utf-8');
                
                if (file.includes('class')) {
                    diagrams.classDiagram = content;
                } else if (file.includes('sequence')) {
                    diagrams.sequenceDiagram = content;
                } else if (file.includes('state')) {
                    diagrams.stateDiagram = content;
                }
            }
        }
        
        return diagrams;
    }
    
    private async initiateGeneration(diagrams: Record<string, string>): Promise<string> {
        const backend = this.config.get('backend');
        
        log(`Initiating generation on backend: ${backend}`, 'info');
        log(`Language: ${this.config.get('language')}, Package: ${this.config.get('packageName')}`, 'info');
        
        const response = await axios.post(`${backend}/api/v2/stream/generate`, {
            classDiagram: diagrams.classDiagram || '',
            sequenceDiagram: diagrams.sequenceDiagram || '',
            stateDiagram: diagrams.stateDiagram || '',
            packageName: this.config.get('packageName'),
            language: this.config.get('language')
        });
        
        log(`Generation ID: ${response.data.generationId}`, 'info');
        
        return response.data.generationId;
    }
    
    private async waitForCompletion(generationId: string, progress: vscode.Progress<any>): Promise<void> {
        const backend = this.config.get('backend');
        
        log('Waiting for generation completion...', 'info');
        
        return new Promise((resolve, reject) => {
            const checkStatus = async () => {
                try {
                    const response = await axios.get(`${backend}/api/v2/stream/status/${generationId}`);
                    
                    if (response.data.status === 'COMPLETED') {
                        log(`Generation completed: ${response.data.fileCount} files`, 'info');
                        resolve();
                    } else {
                        const msg = `Processing... (${response.data.fileCount} files)`;
                        progress.report({ message: msg });
                        log(msg, 'info');
                        setTimeout(checkStatus, 1000);
                    }
                } catch (error) {
                    log(`Error checking status: ${error}`, 'error');
                    reject(error);
                }
            };
            
            checkStatus();
        });
    }
    
    private async downloadAndMerge(generationId: string, progress: vscode.Progress<any>): Promise<void> {
        const backend = this.config.get('backend');
        
        log('Downloading generated project...', 'info');
        
        // Télécharger ZIP
        const response = await axios.get(`${backend}/api/v2/stream/download/${generationId}`, {
            responseType: 'arraybuffer'
        });
        
        log(`Downloaded ${response.data.byteLength} bytes`, 'info');
        
        progress.report({ increment: 20, message: "Extracting..." });
        
        // Extraire ZIP
        const zip = new AdmZip(Buffer.from(response.data));
        const entries = zip.getEntries();
        
        let extractedCount = 0;
        
        for (const entry of entries) {
            if (!entry.isDirectory) {
                const targetPath = path.join(this.workspacePath, entry.entryName);
                
                // Créer répertoires si nécessaire
                const dir = path.dirname(targetPath);
                if (!fs.existsSync(dir)) {
                    fs.mkdirSync(dir, { recursive: true });
                }
                
                // Smart merge : vérifier si fichier existe et a été modifié
                if (fs.existsSync(targetPath)) {
                    const existing = fs.readFileSync(targetPath, 'utf-8');
                    const newContent = entry.getData().toString('utf-8');
                    
                    // Simple merge : si fichier identique, skip
                    if (existing === newContent) {
                        continue;
                    }
                    
                    // Si différent, créer backup et appliquer
                    fs.writeFileSync(targetPath + '.backup', existing);
                }
                
                // Écrire nouveau fichier
                fs.writeFileSync(targetPath, entry.getData());
                extractedCount++;
                
                if (extractedCount % 10 === 0) {
                    progress.report({ 
                        message: `Extracted ${extractedCount}/${entries.length} files` 
                    });
                }
            }
        }
        
        log(`Extracted ${extractedCount} files`, 'info');
        
        // Cleanup
        log('Cleaning up server resources...', 'info');
        await axios.delete(`${backend}/api/v2/stream/cleanup/${generationId}`);
        
        log('Generation completed successfully!', 'info');
    }
}

export function deactivate() {}