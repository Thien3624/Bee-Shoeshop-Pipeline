# 🛠️ Bee-Shoeshop Jenkins CI/CD Pipelines

This repository contains two Jenkins pipelines for the Bee-Shoeshop project:
- 🧠 **Backend Pipeline** (Java)
- 🎨 **Frontend Pipeline** (React)

Both pipelines are designed with **DevSecOps** principles, integrating static code analysis, vulnerability scanning, containerization, and Kubernetes deployment via GitOps.

---

## 📋 Common Pipeline Stages

| Stage                  | Description                                                                 |
|------------------------|-----------------------------------------------------------------------------|
| 1. SonarQube Scan      | Run SonarScanner in Docker to check code quality (with token auth)         |
| 2. Trivy FS Scan       | Scan local source code for filesystem vulnerabilities                      |
| 3. Docker Build        | Build two images: one with Git tag (`:commit`) and one `:latest`           |
| 4. Trivy Image Scan    | Scan built image for OS and package vulnerabilities                        |
| 5. Docker Push         | Push both images to Docker Hub                                             |
| 6. K8s Manifest Update | Update Kubernetes manifest with new image tag and push to GitHub `UAT`     |
| 7. Cleanup             | Remove built images, Trivy tools, and change file ownership for reports    |

---

## 🔐 DevSecOps Integrations

| Integration     | Tool / Service                  |
|------------------|---------------------------------|
| Static Analysis  | SonarQube (via Docker)          |
| FS Scan          | Trivy (using `fs` mode)         |
| Image Scan       | Trivy (using `image` mode)      |
| Registry         | DockerHub                       |
| GitOps           | GitHub repo triggers ArgoCD     |
| Deployment       | Kubernetes manifests updated    |

---

## 🧠 Backend Pipeline Summary

- **Project Name:** `bee-shoeshop-backend`
- **Scanner Token:** `bee-shoeshop-scaner-token`
- **SonarQube Key:** `bee-shoeshop-be`
- **Docker Image:** `thien3624/bee-shoeshop-backend:<tag>`
- **Manifest Path:** `./bee-shoes-backend/manifests/deployment.yaml`

### Special Behavior:
- Excludes `**/*.java` from Sonar scan for demo purposes.
- Updates deployment manifest via `sed`.
- Pushes to `UAT` branch for ArgoCD sync.

---

## 🎨 Frontend Pipeline Summary

- **Project Name:** `bee-shoeshop-frontend`
- **Scanner Token:** `bee-shoeshop-fe-scanner`
- **SonarQube Key:** `bee-shoeshop-fe`
- **Docker Image:** `thien3624/bee-shoeshop-frontend:<tag>`
- **Manifest Path:** `./bee-shoes-frontend/manifests/deployment.yaml`

### Notes:
- Uses default scanner config (no exclusions).
- Same GitOps behavior as backend.

---

## 📦 Image Tagging Strategy

| Tag Format                | Description                    |
|---------------------------|--------------------------------|
| `latest`                  | Always overwritten             |
| `branch-commit`           | Immutable build version (e.g. `UAT-a1b2c3d`) |

---

## 📁 Report Output (HTML)

Both pipelines generate two HTML reports:
- `${WORKSPACE}/<project>/trivyfs-scan-<project>-<tag>_report.html`
- `${WORKSPACE}/<project>/image-scan-<project>-<tag>_report.html`

Ensure appropriate permissions via `chown` after build.

---

## 🧼 Cleanup Actions

To save storage, the pipelines:
- Remove built Docker images (`:latest`, `:<tag>`)
- Remove scanner images (`trivy`, `sonar-scanner-cli`)
- Logout from DockerHub

---

## 📬 Contact

[![LinkedIn Badge](https://img.shields.io/badge/LinkedIn-Profile-blue?logo=linkedin)](https://www.linkedin.com/in/duythien3624)


