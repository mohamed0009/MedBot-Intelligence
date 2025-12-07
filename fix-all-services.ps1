# Script PowerShell pour corriger tous les services Python
$services = @('deid', 'indexeur-semantique', 'llm-qa-module', 'synthese-comparative', 'audit-logger')

Write-Host "=== Correction des Dockerfiles ===" -ForegroundColor Cyan

foreach ($svc in $services) {
    $dockerfilePath = "services/$svc/Dockerfile"
    
    if (Test-Path $dockerfilePath) {
        $content = Get-Content $dockerfilePath -Raw
        
        # 1. Changer COPY ./app /app en COPY ./app /app/app
        $content = $content -replace 'COPY \./app /app\r?\n', "COPY ./app /app/app`r`n`r`n# Create __init__.py to make it a package`r`nRUN touch /app/__init__.py`r`n"
        
        # 2. Changer CMD uvicorn en CMD python -m uvicorn
        $content = $content -replace 'CMD \["uvicorn", "main:app"', 'CMD ["python", "-m", "uvicorn", "app.main:app"'
        
        Set-Content $dockerfilePath $content
        Write-Host "✓ Fixed $svc Dockerfile" -ForegroundColor Green
    }
}

Write-Host "`n=== Correction du docker-compose.yml ===" -ForegroundColor Cyan

# Commenter les volumes de code
$dcContent = Get-Content docker-compose.yml
$newDcContent = @()

for ($i = 0; $i -lt $dcContent.Count; $i++) {
    $line = $dcContent[$i]
    
    # Si la ligne contient un volume mount de code app
    if ($line -match '^\s+- \./services/[^/]+/app:/app\s*$') {
        $newDcContent += $line -replace '- \./services/', '# - ./services/'
        Write-Host "✓ Commented volume mount: $line" -ForegroundColor Yellow
    } else {
        $newDcContent += $line
    }
}

$newDcContent | Set-Content docker-compose.yml

Write-Host "`n=== Rebuild de tous les services ===" -ForegroundColor Cyan
Write-Host "Lancement de docker-compose up -d --build..." -ForegroundColor Yellow
