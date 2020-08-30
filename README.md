# Kubernetes Autoscaler mit Custom Metrics

## Vorbemerkungen
Um eigene Metriken für das Autoscaling nutzen zu können, benötigt man einen besonderen Metrics-Adapter.

Grundlage des ganzen steht hier:<br>
https://kubernetes.io/de/docs/tasks/run-application/horizontal-pod-autoscale/#unterst%C3%BCtzung-von-benutzerdefinierte-metriken

Und eine Liste verfügbarer Adapter findet man hier:<br>
https://github.com/kubernetes/metrics/blob/master/IMPLEMENTATIONS.md#custom-metrics-api

Für diesen POC habe ich den von Zalando entwickelten [Kube Metrics Adapter](https://github.com/zalando-incubator/kube-metrics-adapter) entwickelt, da er die Möglichkeit bietet, selber Prometheus-Queries zu definieren, anhand derer über ein Scaling entschieden wird.

## Testservice
Für diesen POC nutze ich eine weitere Fähigkeit des Zalando-Adapters, nämlich auch ohne Prometheus eine Restressource aller Pods eines Services zu crawlen.<br>
Der Service stellt hierzu einen Actuator-Endpunkt zur Verfügung, der je Pfad die Anzahl Request / Sekunde liefert in der Form
```
{
    "/pfad1": 10.1234,
    "/pfad2": 20.5432
}
```

## Scenario durchspielen
Das gesamte Scenario lässt sich auf Kubernetes for Docker durchführen.

1. Wechsel in den richtigen von Docker bereitgestellten K8s-Context (bei mir docker-desktop oder docker-for desktop)
2. Sicher stellen, dass der ein Namespace `omm` existiert mit`kubectl get namespaces`, ggf. namespace anlegen: `kubectl create namespace omm`
3. Deployment durch ausführen von `./deploy.sh` (Installiert alle benötigten Komponenten im namespace kube-system sowie baut und deployed den kleine Testservice im namespace omm)
4. Ausführen eines kleinen Lasttest mit `./gradlew clean gatlingRun`, gleichzeitig die pods beobachten.
    * `kubectl -n omm logs -f deployment/podscaler-service --all-containers=true`
    * `watch kubectl -n omm get pods`
    
Nach einiger Zeit sieht man, wie die Pods mit steigenden Requestzahlen hochscaliert werden.

## Cleanup
Um alle Kompenenten aus K8s wieder zu entfernen, einfach `./undeploy.sh` ausführen.

## Was brauchen wir für den Livebetrieb?
Alles, was man benötigt und uns von Quokka zur Verfügung gestellt werden müsste, findet man im Ordner 'metrics-adapter-deployment'. Die darin enthaltenen Files habe ich 1:1 von Zalando übernommen.
Quokka müsste also klären, inwiefern es möglich ist, diese in unserem Cluster auszurollen.
