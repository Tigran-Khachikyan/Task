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
After the initialization, several methods are available for communication with the server. Each of these requests can be made both synchronously and asynchronously. To pass the needed parameters for the request an interface is used, as well as its realization.<br/>
All the examples below are associated with the **Standard payment** method.
### Asynchronous requests
This kind of request must be done on the **Main thread**. Results are obtained in the callback methods.<br/>
Java version<br/>
An instance of *PaymentRequestDataImpl* class is used.

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
Kotlin version<br/>
An object from *PaymentRequestData* interface is created.

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


### Synchrounous requests
This kind of request must be done on **background thread**.
Java version<br/>
To operate with the result from the Main thread the AsyncTask or RxJava must be used.

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
Kotlin version<br/>
The following function must be called either from **suspend function/coroutine** or on background Thread:

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
This function must be called from suspend function/coroutine:

        GlobalScope.launch(Dispatchers.IO) {
                        val result: Result<PaymentResponseData> = NexoProvider.Payment.suspendRequest(
                                PaymentRequestDataImpl(
                                        "transactionId555",
                                        "2020-01-20T12:02:21+01:00",
                                        "EUR",
                                        77.5,
                                        "saleId888",
                                        "serviceId444",
                                        "poiId777")
                        )
                        withContext(Dispatchers.Main) {
                                 if(result.success) //so something with (result.data)
                                 else  //get the error (result.errorMessage)
                        }
                    }









