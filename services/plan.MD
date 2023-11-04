* ProductsManager (user/ to add)
  * [x] fix logging --> using app properties 
  * [x] fix general routing at nginx level
  * [x] Add kafka production in manager
* Products Consumer (new)
    * [x] fix logging --> using app properties 
    * [x] expose health check
* Products API (products)
  * [x] api to handle product read requests 
  * [x] make sure env properties are read correctly both locally and in docker
  * [x] api to be exposed in docker and nginx
  * [x] kafka listener to update db
  * [x] reach kafka from outside the docker container 
  * [x] manage the thread pool
  


 * [ ] Testcontainer test for kafka
 * [ ] How to integrate spring boot with docker in a way to make it compile on the fly when running docker-compose up
 * [ ] fix kafka consumption from outside docker in products api
 * [ ] db management using jooq

* Kafka
 * [ ] Make persistent 
 * [ ] Move second product-api.yaml from under same project in docker

* Monitoring
* [ ] Add prometheus or other monitoring tool
