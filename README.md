
# DM111

## Project

This repository contains the solution of the final project implemented by the DM111 discipline from the Pos Graduation course Mobile and Cloud Development by Inatel.

During the classes was implemented a web service to deal with a minimal structure of supermarket, i.e. The _CRUD_ operations for **Products**, **Users** _(Client and Admin)_, **Shopping Lists**, **Authentication** that uses JWT token. For the final task, it was created a **Promotion API** with the following operations:

- Delete Promo
- Update Promo
- Create Promos
- Fetch Promo by UserID
- Fetch Valid Promos
- Fetch PromoID

There is an Insomnia collection ([Insomnia_Collection.json](https://github.com/mateusbrodrigues/dm111/blob/main/Insomnia_Collection.json "Insomnia_Collection.json")) available to make it easier to validate the API.

## Integrations

The current project does have the integrations with: _Firebase DB_, _Firebase Cloud Message_, _Pub Sub_.

### Firebase

In order to reduce the cost and also reduce the risk of reach the Free tier limit of U$ 300 provided by the Google Cloud
I have decided to use Firebase as our main database solution. That decision helped us to reduce the number of resources
required to be deployed and simplify our deployment letting us move our focus on the code itself.

#### Remember
_You have to generate the service accounts from your Firebase project and paste into the right place, i.e.the folder_
_src/main/resources_

The following entities are stored on the Firebase:

> User
```json
{
  "email": "email@email.com.br",
  "id": "uuid",
  "name": "name",
  "password": "encrypted",
  "role": "ADMIN|CLIENT"
}
```

> Products
```json
{
  "id": "uuid",
  "name": "name",
  "amount": 0,
  "brand": "brand",
  "price": 5.99,
  "unit": "kg|lt|ml"
}
```

> Supermarket list
```json
{
  "id": "uuid",
  "name": "name",
  "products": [
    "product_id1",
    "product_id2"
  ]
}
```

>Promos
```json
{
 "name": "Promo name",
 "starting": "YYYY-MM-DD",
 "expiration": "YYYY-MM-DD",
 "products": [
	 {
	 "productId": "uuid",
	 "discount": 1
	 },
	  {
	 "productId": "uuid",
	 "discount": 99
	 }
 ]
}
```

### Firebase Cloud Message

It was also implemented the Publisher to the **FCM** (_Firebase Cloud Message_) and whenever a user was created a message
is published into the FCM topic.

### Pub Sub

One more integration added to the application was the **Pub Sub** that is composed by the topic and their subscriptions.
Whenever a supermarket list has been created, updated or either deleted an event is published through this topic and the
consumer services will be aware of the change. So, the consumer applications can apply their specific logics.


