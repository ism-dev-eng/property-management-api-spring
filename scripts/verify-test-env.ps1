# scripts/verify-test-env.ps1
Write-Host "Verifying TEST Environment Components..." -ForegroundColor Cyan
Write-Host ""

# Check namespace
Write-Host "Namespace:" -ForegroundColor Yellow
kubectl get namespace property-management-test

# Check ArgoCD Application
Write-Host "`nArgoCD Application:" -ForegroundColor Yellow
kubectl get application property-management-test -n argocd -o wide

# Check all resources
Write-Host "`nAll Resources:" -ForegroundColor Yellow
kubectl get all -n property-management-test

# Check ConfigMaps
Write-Host "`nConfigMaps:" -ForegroundColor Yellow
kubectl get configmap -n property-management-test

# Check Secrets
Write-Host "`nSecrets:" -ForegroundColor Yellow
kubectl get secret -n property-management-test

# Check PVCs
Write-Host "`nPersistentVolumeClaims:" -ForegroundColor Yellow
kubectl get pvc -n property-management-test

# Check Pods in detail
Write-Host "`nPods (Detailed):" -ForegroundColor Yellow
kubectl get pods -n property-management-test -o wide

# Check Pod status
Write-Host "`nPod Status:" -ForegroundColor Yellow
kubectl get pods -n property-management-test -o jsonpath='{range .items[*]}{.metadata.name}{"\t"}{.status.phase}{"\t"}{.status.containerStatuses[0].ready}{"\n"}{end}'

# Check Services
Write-Host "`nServices:" -ForegroundColor Yellow
kubectl get svc -n property-management-test

# Check Deployments
Write-Host "`nDeployments:" -ForegroundColor Yellow
kubectl get deployment -n property-management-test

# Check current image tags
Write-Host "`nImage Tags:" -ForegroundColor Yellow
kubectl get deployment app -n property-management-test -o jsonpath='{.spec.template.spec.containers[0].image}'
Write-Host ""

# Check ArgoCD sync status
Write-Host "`nArgoCD Sync Status:" -ForegroundColor Yellow
$syncStatus = kubectl get application property-management-test -n argocd -o jsonpath='{.status.sync.status}'
$healthStatus = kubectl get application property-management-test -n argocd -o jsonpath='{.status.health.status}'
Write-Host "Sync: $syncStatus | Health: $healthStatus"

Write-Host "`n[OK] Verification complete!" -ForegroundColor Green