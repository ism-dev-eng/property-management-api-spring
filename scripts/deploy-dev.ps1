# scripts/deploy-dev.ps1
Write-Host "🚀 Deploying to DEV..." -ForegroundColor Green

docker pull ghcr.io/ism-dev-eng/property-management-api:develop
kubectl apply -k k8s/overlays/dev
kubectl rollout status deployment/app -n property-management-dev --timeout=5m

Write-Host "✅ Deployment complete!" -ForegroundColor Green