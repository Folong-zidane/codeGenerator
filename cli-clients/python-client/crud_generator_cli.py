#!/usr/bin/env python3
"""
CLI Client pour UML-to-CRUD Generator
Usage: python crud_generator_cli.py generate --uml diagram.mermaid --output ./my-project --framework spring-boot
"""

import click
import requests
import json
import os
from pathlib import Path

API_BASE_URL = "http://localhost:8080/api/v2/generate"

@click.group()
@click.version_option(version="1.0.0")
def cli():
    """UML-to-CRUD Generator CLI Client"""
    pass

@cli.command()
@click.option('--uml', '-u', required=True, type=click.Path(exists=True), help='UML diagram file (.mermaid)')
@click.option('--output', '-o', required=True, type=click.Path(), help='Output directory path')
@click.option('--package', '-p', default='com.example', help='Base package name')
@click.option('--framework', '-f', 
              type=click.Choice(['spring-boot', 'django', 'flask', 'dotnet', 'express']),
              default='spring-boot', help='Target framework')
@click.option('--type', '-t',
              type=click.Choice(['files-only', 'complete-project']),
              default='complete-project', help='Generation type')
@click.option('--api-url', default=API_BASE_URL, help='API base URL')
def generate(uml: str, output: str, package: str, framework: str, type: str, api_url: str):
    """Generate CRUD code from UML diagram"""
    
    click.echo(f"🚀 Generating {framework} project from {uml}")
    click.echo(f"📁 Output directory: {output}")
    
    # Read UML content
    with open(uml, 'r') as f:
        uml_content = f.read()
    
    # Prepare request
    payload = {
        "umlContent": uml_content,
        "packageName": package,
        "outputPath": os.path.abspath(output),
        "generationType": type.upper().replace('-', '_'),
        "framework": framework.upper().replace('-', '_')
    }
    
    try:
        response = requests.post(f"{api_url}/files", json=payload, timeout=30)
        
        if response.status_code == 200:
            result = response.json()
            if result['success']:
                click.echo(f"✅ {result['message']}")
                click.echo(f"📊 Generated {result['classCount']} classes")
                show_next_steps(framework, output)
            else:
                click.echo(f"❌ Generation failed: {result['message']}", err=True)
        else:
            click.echo(f"❌ API Error: {response.status_code}", err=True)
            
    except requests.exceptions.RequestException as e:
        click.echo(f"❌ Connection error: {e}", err=True)

def show_next_steps(framework: str, output_path: str):
    """Show framework-specific next steps"""
    
    click.echo("\n🎯 Next Steps:")
    
    if framework == 'spring-boot':
        click.echo("  cd " + output_path)
        click.echo("  mvn spring-boot:run")
        
    elif framework == 'django':
        click.echo("  cd " + output_path)
        click.echo("  pip install -r requirements.txt")
        click.echo("  python manage.py runserver")

if __name__ == '__main__':
    cli()