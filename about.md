# About the library
## Purpose
The library provides safe and encapsulated communication with Nexo server, handling the request data from user applications and processing the response from the server.
## Initialization
To start working with the library, initialization is required, otherwise, the user will get an **ExceptionInInitializerError**.<br/>
Examples of initialization
Java version:

        NexoProvider.Companion
                .initialize("https://nexo.../", "usersCredentials", 5);
Kotlin version:

            NexoProvider.initialize(
                baseUrl = "https://nexo.../",
                basicAuth = "usersCredentials"
           )
## Usage
After the initialization, several methods are avalable for communication with the server. Each of these requests can be made both synchronously and asynchronously.
Examples for Standard payment
Java version:

       NexoProvider.Payment.INSTANCE.asyncRequest(new PaymentRequestDataImpl(
                "transactionId555",
                "2020-01-20T12:02:21+01:00",
                "EUR",
                77.5,
                "saleId888",
                "serviceId444",
                "poiId777"), new AsyncResultListener<PaymentResponseData>() {
            @Override
            public void onSuccess(PaymentResponseData output) {
            }
  
            @Override
            public void onError(String errorInfo) {

            }
        });
