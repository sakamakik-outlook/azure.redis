# Azure Redis Example for BIC code cache

This is a simple example of caching data in Redis. 

It reads BIC codes from text file and stores in Redis cache if BIC doesn't exist. 

User can call get method to check bank detail by calling the end point.

http://<server_address>:8080/get/{bic_code}

