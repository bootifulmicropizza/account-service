# account-service

JPA backed microservice to store customer accounts for the pizza store providing an OAuth2 server.

## Kubernetes secrets

The following secrets are required:

```
$ keytool -genkeypair -alias pizza -keyalg RSA -keypass bootifulmicropizza -keystore src/main/resources/auth-server.jks -storepass bootifulmicropizza
$ keytool -list -rfc --keystore src/main/resources/auth-server.jks | openssl x509 -inform pem -pubkey
$ kubectl create secret generic oauth2-resource \
	--from-literal=jwt.keyValue="[PUBLIC_KEY]"
	
$ kubectl create secret generic account-service \
	--from-literal=db.url=[DB_URL] \
	--from-literal=db.port=[DB_PORT] \
	--from-literal=db.name=[DB_NAME] \
	--from-literal=db.username=[DB_USERNAME] \
	--from-literal=db.password=[DB_PASSWORD]
```
