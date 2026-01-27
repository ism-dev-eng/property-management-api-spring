# scripts/cleanup-test-env.ps1
Write-Host "🧹 Cleaning TEST Environment..." -ForegroundColor Red
Write-Host ""

# Confirm deletion
$confirmation = Read-Host "Are you sure you want to delete all TEST resources? (yes/no)"
if ($confirmation -ne "yes") {
    Write-Host "❌ Cleanup cancelled" -ForegroundColor Yellow
    exit 0
}

Write-Host "🗑️  Deleting all resources in property-management-test namespace..." -ForegroundColor Yellow

# Delete all resources
kubectl delete all --all -n property-management-test --ignore-not-found=true
kubectl delete configmap --all -n property-management-test --ignore-not-found=true
kubectl delete secret --all -n property-management-test --ignore-not-found=true
kubectl delete pvc --all -n property-management-test --ignore-not-found=true

Write-Host "⏳ Waiting for resources to be deleted..." -ForegroundColor Cyan
Start-Sleep -Seconds 10

# Verify cleanup
$remaining = kubectl get all -n property-management-test --no-headers 2>$null
if ($remaining) {
    Write-Host "⚠️  Some resources still exist. Force deleting..." -ForegroundColor Yellow
    kubectl delete all --all -n property-management-test --force --grace-period=0
}

# Optionally delete namespace
$deleteNamespace = Read-Host "Delete namespace too? (yes/no)"
if ($deleteNamespace -eq "yes") {
    kubectl delete namespace property-management-test --ignore-not-found=true
    Write-Host "✅ Namespace deleted" -ForegroundColor Green
}

Write-Host "`n✅ Cleanup complete!" -ForegroundColor Green
Write-Host "`n💡 To redeploy, trigger ArgoCD sync or run CI/CD workflow" -ForegroundColor Cyan