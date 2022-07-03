# Azure Redis Example for BIC code cache

This is a simple example of caching data in Redis. 

It reads BIC codes from a text file and adds data to Redis cache if BIC doesn't exist. 

Once this service is up, user check details by calling the end point below. 

http://<server_address>:8080/get/{bic_code}

