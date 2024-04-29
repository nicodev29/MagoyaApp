Magoya App - Backend

Se desarrolla el backend segun requisitos solicitados.

La app en si, permite la creacion de cuentas, consulta de saldos por numero de cuenta y crear transacciones, ya sean depositos o retiros.
En el caso de los depositos, cuando estos fuesen mayores a un monto de 10000 se interceptan a traves de un middleware y se notifica por consola los datos de la operacion/transaccion.
A nivel front, se desarrolla una solucion visual con el mismo objetivo.

Con respecto al sistema, se desarrollo con Java, en la version 17 del JDK, y Spring Boot 3.2.5, la version mas reciente hasta la fecha.
Se utilizaron dependencias de terceros, que se cargaran automaticamente al levantar el proyecto.

Se debe tener instalado Java y tenerlo setteado dentro de las variables de entorno para que el sistema lo reconozca.
Recomendaciones, tener instalado Docker Desktop y MongoDB Compose para visualizar tanto el contenedor e imagen, como la base. (Se puede utilizar cualquier gestor de BD)

Al momento de correr el proyecto, en primera instancia se ejecutara el archivo de docker-compose, el cual creara un contenedor con una imagen de Mongo DB, como fue solicitado.

Posteriormente la app se iniciara en el puerto 8080, y Mongo en el puerto 27017

Para este momento deberiamos tener la app corriendo y sin problemas. 

Se desarrollan tambien, varios endpoints, los cuales explico a continuacion:

POST:

CREAR CUENTA:
http://localhost:8080/api/accounts 

Request Example:

{
  "name": "MagoyAPP",
  "accountNumber": "123123",
  "balance": 10500
}

CREAR TRANSACCION
http://localhost:8080/api/transactions

Request Example:

{
  "accountId": "6630004bfd548525ea315d59",
  "amount": 10000,
  "type": "DEPOSIT"
}

GET:

OBTENER BALANCE POR ID DE CUENTA
http://localhost:8080/api/accounts/{id}/balance

OBTER TRANSACCION POR ID
http://localhost:8080/api/transactions/{id}

OBTENER TODAS LAS TRANSACCIONES
http://localhost:8080/api/transactions/all

A nivel funcional, hay mas endpoints que se utilzan para facilitar la exp de usuario, como obtener el balance por numero de cuenta, que se implementa como solucion en el front. 
Lo mismo para crear transacciones, el dato solicitado en el front, es el numero de cuenta del usuario y el monto.



