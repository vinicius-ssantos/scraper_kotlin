$zipPath = "$PSScriptRoot\Handle.zip"
$destPath = "C:\tools\handle"

if (-Not (Test-Path $zipPath)) {
    Write-Host "Arquivo Handle.zip não encontrado em: $zipPath"
    exit 1
}

if (-Not (Test-Path $destPath)) {
    New-Item -ItemType Directory -Path $destPath | Out-Null
    Write-Host "Criado diretório: $destPath"
}

Expand-Archive -Path $zipPath -DestinationPath $destPath -Force
Write-Host "Arquivo extraído com sucesso para: $destPath"

$userPath = [Environment]::GetEnvironmentVariable("Path", "User")
if ($userPath -notlike "*$destPath*") {
    $newPath = "$userPath;$destPath"
    [Environment]::SetEnvironmentVariable("Path", $newPath, "User")
    Write-Host "Caminho adicionado ao PATH do usuário: $destPath"
} else {
    Write-Host "Caminho já está no PATH."
}

Write-Host ""
Write-Host "Instalação concluída. Feche e reabra o terminal para usar o comando 'handle'."
