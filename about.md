# Specification Heading
## Specification Heading
**The library provides safe and encapsulated communication with Nexo server, handling the request data from user applications and processing the response from the server.**
To start working with the library, initialization is required, otherwise, the user will get an ExceptionInInitializerError.</p>
<p>Examples of <b>initialization</b>:</p>
Java version:

        NexoProvider.Companion
                .initialize("https://nexo.../", "usersCredentials", 5);
</p>
Kotlin version:
                
            NexoProvider.initialize(
                baseUrl = "https://nexo.../",
                basicAuth = "usersCredentials"
           )
After the initialization, several methods are avalable for communication with the server. Each of these requests can be made both synchronously and asynchronously.
<p>Examples for <b>Standard payment</b>:</p>
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
