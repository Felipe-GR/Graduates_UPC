# Graduates UPC Module
Copyright (C) 2019 Luis F. Guerrón, Camilo A. Caycedo, Juan C. Parra.

## Personal Use License
This project is a school assignment and nobody has the right to use their code for commercial use. In addition, this project has been implemented for academic reasons and the only right allowed is to just test and observe its functionality. We are not responsible for potential problems that may exist.

## About
This is a single page web application that implements a graduates service similar to LinkedIn. This project is an assignment for the course "Software Quality" of the school of systems engineering at the Piloto University of Colombia.

## Build & Execution
To build & run this application make sure that you have installed Java 8 & MySQL Server. Also you must edit your hosts file by adding the following:

```
127.0.0.1 linkdin.dyndns.org
```

You can build the package as a single artifact by running the `./mvnw clean install`.
Next, you can run the application by executing:

```bash
$ java -jar backend/target/linkdin-app.jar
```

The application will be accessible at `https://linkdin.dyndns.org:8080`. <br>
Also make sure that the files `cert.pem`, `key.pem` & `keystore.p12` are in the same directory with the `linkdin-app.jar`.

### Running the backend for development mode
There are multiple ways to run the backend. For development, you can use your favorite IDE and run the
`co.dtech.graduates.Application`. As soon as your code compiles, Spring Boot DevTools will reload the code.

### Running the frontend for development mode
Make sure to install [yarn on your development machine](https://yarnpkg.com/en/docs/install). <br>
To install all the required binaries for your project, you can run following command.

```
$ cd frontend
$ ../mvnw frontend:install-node-and-yarn frontend:yarn
```

Once the above command finishes, you can start the frontend using the `yarn start` command.

### Hot reloading
Both the front-end and back-end modules support hot reloading.

### Database setup
To set up the database of this web application is pretty straight forward. All you have to do is simply to execute the script `create_db.sql` in the directory `database`. <br>
Also we have created a script to initiliaze the database with records that are stored in .csv files. To do that just run the script `import_records.sql` with the only resrtiction that all the .csv files must be stored in a directory with name `db_backup` which must be in the same path with the script or just can use the script `single_file.sql` to create some data to test it quickly.

## Frameworks used

### Backend
* [Spring MVC/Spring Boot](https://spring.io/): With this framework we implemented a RESTful API that handles all the requests of the application.
* [Spring JPA](https://spring.io/projects/spring-data-jpa)/[Hibernate](https://hibernate.org/): These frameworks are responsible for the I/O with the database.
* [MySQL](https://www.mysql.com/): The RDBMS of this web application is MySQL.

### Frontend
* [TypeScript](https://www.typescriptlang.org/)/[Angular  5](https://angular.io/): We have implemented the user interface in Angular, split in components (i.e. navbar.component, post.component etc.).
* [Bootstrap 4](http://getbootstrap.com/): The user interface is styled with the help of Bootstrap framework. Also responsive view is supported.
* HTML 5/CSS: (nothing to say here...)

## Algorithms

### Post Recommendation Algorithm
The post recommendation service executes an algorithm that is based in k-NN classification. More specific, the algorithm fetches the 3 most recent public posts per user based on similar user activity(interests & comments).

### Job Recommendation Algorithm
Each user's job advert page contains job adverts that are based on the personal information of the user. After the stopwords have been filtered out, the algorithm selects the ads that have the most similar content with the user's info.

## Certificates

### TLS
We used a cerificate that is signed from [Let's Encrypt](https://letsencrypt.org/) in order to encrypt all the traffic between the clients and the server.

## Extra features

### Post Visibility
Although it was not in the description of functional requirements, we have added a special feature in the publications. All publications can be public or private. With the attribute 'public' the publication can be visible from all. On the contrary, with the "private" attribute, the publication is only visible within the user's network.

### Admin tools
The administrators can see everyone's activity, even if it's private. Also they have the option to export to XML file all the users' information and activity.
