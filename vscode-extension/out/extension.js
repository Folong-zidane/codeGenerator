"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.deactivate = exports.activate = void 0;
const vscode = require("vscode");
const fs = require("fs");
const path = require("path");
const axios_1 = require("axios");
const AdmZip = require('adm-zip');
function activate(context) {
    const disposable = vscode.commands.registerCommand('basiccode.generate', async () => {
        const workspaceFolder = vscode.workspace.workspaceFolders?.[0];
        if (!workspaceFolder) {
            vscode.window.showErrorMessage('No workspace folder found');
            return;
        }
        const generator = new ProjectGenerator(workspaceFolder.uri.fsPath);
        await generator.generate();
    });
    context.subscriptions.push(disposable);
}
exports.activate = activate;
class ProjectGenerator {
    constructor(workspacePath) {
        this.workspacePath = workspacePath;
        this.config = vscode.workspace.getConfiguration('basiccode');
    }
    async generate() {
        try {
            // 1. Scanner les diagrammes
            const diagrams = await this.scanDiagrams();
            if (Object.keys(diagrams).length === 0) {
                vscode.window.showWarningMessage('No diagrams found in src/diagrams/');
                return;
            }
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
        }
        catch (error) {
            vscode.window.showErrorMessage(`❌ Generation failed: ${error}`);
        }
    }
    async scanDiagrams() {
        const diagramsPath = path.join(this.workspacePath, 'src', 'diagrams');
        const diagrams = {};
        if (!fs.existsSync(diagramsPath)) {
            return diagrams;
        }
        const files = fs.readdirSync(diagramsPath);
        for (const file of files) {
            if (file.endsWith('.mmd') || file.endsWith('.puml')) {
                const filePath = path.join(diagramsPath, file);
                const content = fs.readFileSync(filePath, 'utf-8');
                if (file.includes('class')) {
                    diagrams.classDiagram = content;
                }
                else if (file.includes('sequence')) {
                    diagrams.sequenceDiagram = content;
                }
                else if (file.includes('state')) {
                    diagrams.stateDiagram = content;
                }
            }
        }
        return diagrams;
    }
    async initiateGeneration(diagrams) {
        const backend = this.config.get('backend');
        const response = await axios_1.default.post(`${backend}/api/v2/stream/generate`, {
            classDiagram: diagrams.classDiagram || '',
            sequenceDiagram: diagrams.sequenceDiagram || '',
            stateDiagram: diagrams.stateDiagram || '',
            packageName: this.config.get('packageName'),
            language: this.config.get('language')
        });
        return response.data.generationId;
    }
    async waitForCompletion(generationId, progress) {
        const backend = this.config.get('backend');
        return new Promise((resolve, reject) => {
            const checkStatus = async () => {
                try {
                    const response = await axios_1.default.get(`${backend}/api/v2/stream/status/${generationId}`);
                    if (response.data.status === 'COMPLETED') {
                        resolve();
                    }
                    else {
                        progress.report({ message: `Processing... (${response.data.fileCount} files)` });
                        setTimeout(checkStatus, 1000);
                    }
                }
                catch (error) {
                    reject(error);
                }
            };
            checkStatus();
        });
    }
    async downloadAndMerge(generationId, progress) {
        const backend = this.config.get('backend');
        // Télécharger ZIP
        const response = await axios_1.default.get(`${backend}/api/v2/stream/download/${generationId}`, {
            responseType: 'arraybuffer'
        });
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
        // Cleanup
        await axios_1.default.delete(`${backend}/api/v2/stream/cleanup/${generationId}`);
    }
}
function deactivate() { }
exports.deactivate = deactivate;
//# sourceMappingURL=extension.js.map