# About the library
## Purpose
The library provides safe and encapsulated communication with Nexo server, handling the request data from user applications and processing the response from the server.
## Initialization
To start working with the library, initialization is required, otherwise, the user will get an **ExceptionInInitializerError**.<br/>
Example of initialization:<br/>
Java version

       NexoProvider.Companion
                .initialize("https://nexo.../", "usersCredentials", 5);
Kotlin version

        NexoProvider.initialize(
            baseUrl = "https://nexo.../",
            basicAuth = "usersCredentials"
       )
## Usage
After the initialization, several methods are avalable for communication with the server. Each of these requests can be made both synchronously and asynchronously. To pass the needed parameters for the request an interface is uses, as well as its realization.<br/>
All the examples bellow are associated with the **Standard payment** method.<br/>
**Asynchronous requests:** <br/>
This kind of request must be done on Main thread, and the result must be received from the callback methods.<br/>
Java version
Here an instance of **PaymentRequestDataImpl** class is used.

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
Kotlin version
Here an object from **PaymentRequestData** interface is created.

        NexoProvider.Payment.asyncRequest(
                        object : PaymentRequestData {
                            override val transactionId: String = "ransactionId555"
                            override val timeStamp: String = "2019-02-18T07:02:22+00:00"
                            override val currency: String = "USD"
                            override val amount: Double = 275.5
                            override val saleId: String = "saleId888"
                            override val serviceId: String = "serviceId444"
                            override val poiId: String = "poiId777"
                        },
                        object : AsyncResultListener<PaymentResponseData> {

                            override fun onSuccess(output: PaymentResponseData) {
                            }

                            override fun onError(errorInfo: String) {
                            }
                        }
                    )
**Synchronous requests:** <br/>
Java version

        new Thread(() -> {
                    Result<PaymentResponseData> responseData = NexoProvider.Payment.INSTANCE.syncRequest(new PaymentRequestDataImpl(
                            "transactionId555",
                            "2020-01-20T12:02:21+01:00",
                            "EUR",
                            77.5,
                            "saleId888",
                            "serviceId444",
                            "poiId777")
                    );
                    if (responseData.getSuccess()) {
                        PaymentResponseData data = responseData.getData();
                    } else {
                        String error = responseData.getErrorMessage();
                    }
                }).start();
Kotlin version

        GlobalScope.launch(Dispatchers.IO) {
                    val response = NexoProvider.Payment.syncRequest(
                        object : PaymentRequestData {
                            override val transactionId: String = "transactionId"
                            override val timeStamp: String = "2019-02-18T07:02:22+00:00"
                            override val currency: String = "USD"
                            override val amount: Double = 275.5
                            override val saleId: String = "saleId55"
                            override val serviceId: String = "serviceId77"
                            override val poiId: String = "poiId64"
                        }
                    )
                    withContext(Dispatchers.Main) {
                   // do some operations with response on Main thread
                    }
                }

        GlobalScope.launch(Dispatchers.IO) {
                    val response = NexoProvider.Payment.suspendRequest(
                        object : PaymentRequestData {
                            override val transactionId: String = "transactionId"
                            override val timeStamp: String = "2019-02-18T07:02:22+00:00"
                            override val currency: String = "USD"
                            override val amount: Double = 275.5
                            override val saleId: String = "saleId55"
                            override val serviceId: String = "serviceId77"
                            override val poiId: String = "poiId64"
                        }
                    )
                    withContext(Dispatchers.Main) {
                   // do some operations with response on Main thread
                    }
                }









