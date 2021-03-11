# About the library
## Purpose
The library provides safe and encapsulated communication with Nexo server, handling the request data from user applications and processing the response from the server.
## Initialization
To start working with the library, initialization is required, otherwise, the user will get an **ExceptionInInitializerError**.<br/>
Java version example:

      NexoProvider.Companion
                .initialize("https://nexo.../", "usersCredentials", 5);
Kotlin version example:

            NexoProvider.initialize(
                baseUrl = "https://nexo.../",
                basicAuth = "usersCredentials"
           )
## Usage
After the initialization, several methods are avalable for communication with the server. Each of these requests can be made both synchronously and asynchronously.<br/>
All the examples bellow are associated with the **Standard payment** method. <br/>
**Asynchronous requests:** <br/>
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
Kotlin version:

        NexoProvider.Payment.asyncRequest(
                        object : PaymentRequestData {
                            override val transactionId: String = "transactionId"
                            override val timeStamp: String = "2019-02-18T07:02:22+00:00"
                            override val currency: String = "USD"
                            override val amount: Double = 275.5
                            override val saleId: String = "id45464684"
                            override val serviceId: String = "sId44654"
                            override val poiId: String = "POI_ID64"
                        },
                        object : AsyncResultListener<PaymentResponseData> {

                            override fun onSuccess(output: PaymentResponseData) {
                            }

                            override fun onError(errorInfo: String) {
                            }
                        }
                    )






