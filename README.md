# nasa-image-service
JAVA application to fetch rover images based on file provided. Performance has been taken into consideration and led to the use of **async REST calls** in order to minimize wait time by users.  

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. 
See deployment for notes on how to deploy the project on a live system.

### Prerequisites 

You will need JAVA 8 installed on host machine and knowledge of createing a JAR. 
Google can be used as reference if needing assistance on either. 

The NASA API used requires a private API key for continuous use. This project used the default public API key that the API provides but is limited to n number of calls per day.  

### Installing

If java 8 is not preset on host machine, install.  

Verify installation by executing java -version. If installation was successful then your java version should be given.  


## Testing

Junits w/ Assertj  

## Highlights

* Asynchronous file downloads via (com.squareup.okhttp:okhttp:2.7.5)  

```
		client.newCall(buildRequest(date)).enqueue(new Callback() {
			@Override
			public void onFailure(Request request, IOException e) {
				e.printStackTrace();
			}
```

## Future Enhancements
* Testing


## Built With
* [JAVA](https://java.com) - JAVA


## Authors

* **Javier Abonza** - [Resume](http://jabonza.me)


## Acknowledgments

* **Raptor**
