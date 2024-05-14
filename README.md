# VS-SS-24
## Kubernetes Setup
Realisiert und getestet wurden die vorhandenen Deployments in Minikube.

Zur Reproduktion der Deployments müssen die vorliegenden Deployment-Deklarationen in der korrekten Reihenfolge angewandt werden:
```
$ kubectl apply -f pv-claims.yaml
$ kubectl apply -f deployment.yaml
```

`pv-claims.yaml` definiert die *Persistant Volume Claims* für die Datenbanken der Microservices, um eine persistente Datenhaltung zu realisieren.

`deployment.yaml` definiert alle restlichen Kubernetes-Objekte:
- Deployments
    - Productservice
    - Categoryservice
    - die dazugehörigen Datenbanken
    - Apache Reverse-Proxy
- Config-Maps und Secrets
- Services zum Zugriff auf die jeweiligen Dienste

## Load Balancing
Exemplarisch wurde das Deployment des Categoryservice so skaliert, dass 3 Replicas existieren.
Alle Replicas greifen auf die selbe Datenbank zu.
Das nachfolgende Video demonstriert das LoadBalancing Feature von Kubernetes.
Auf der rechten Seite sind 3 Terminal-Fenster zu sehen, die jeweils den Log einer der 3 Replicas des Categoryservice anzeigt.
Wird nun von einem Client eine große Anzahl von Requests innerhalb einer kurzen Zeit gesendet, kann beobachtet werden, dass diese Anfragen auf die verschiedenen Replicas aufgeteilt werden.

![Demonstration LoadBalancing](./LoadBalancing.gif)