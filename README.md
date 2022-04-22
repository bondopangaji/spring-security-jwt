<div id="top"></div>


<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/othneildrew/Best-README-Template">
    <img src="/assets/img/spring-logo-with-text.svg" alt="Logo" width="500" height="80">
  </a>

<h3 align="center">Spring Security JWT</h3>

  <p align="center">
    JSON Web Token Implementation in Spring Security
    <br />
    
  </p>
</div>



<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgments">Acknowledgments</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project

This project was created as a result of studying Spring Security implementation in Authentication and Authorization with 
    JSON Web Token. The ultimate goal of this project is to make sure the APIs are not exposed and only designated roles can access the
    resources.

Note:
* This implementation exposed the 'secret' in custom ```JwtUtil.class``` which is not ideal, you may want to 
implement this in separate configuration server.

<p align="right">(<a href="#top">back to top</a>)</p>



### Built With

The major frameworks/libraries that powered this project.

* [Spring](https://spring.io/)
* [Auth0 Java JWT](https://github.com/auth0/java-jwt)


<p align="right">(<a href="#top">back to top</a>)</p>



<!-- GETTING STARTED -->
## Getting Started

In case you want to run locally on your machine, follow the steps below.

### Prerequisites

Mandatory requirements before proceeding to installation.
* Set up JDK 17 or OpenJDK 17
* Set up Git SCM
* Set up Postgres or your preferred database

### Installation

1. Clone the repository.
   ```sh
   git clone https://github.com/bondopangaji/spring-security-jwt.git
   ```
2. Change your current directory to the project base directory.
3. If you're not using Postgres, change your ```pom.xml``` settings to reflect the database you use.
4. Hover into ```application.properties``` in ```base-dir/src/main``` and configure:
   ```
   spring.datasource.url=your-db-url
   spring.datasource.username=your-db-username
   spring.datasource.password=your-db-password
   ```
5. Build the project.
   ```sh
    mvn clean install
   ```
6. Run the project.
   ```sh
    mvn spring-boot:run
   ```
7. Terminate the project.
   ```sh
    mvn spring-boot:stop
   ```
   
   
<p align="right">(<a href="#top">back to top</a>)</p>



<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE.txt` for more information.

<p align="right">(<a href="#top">back to top</a>)</p>



<!-- CONTACT -->
## Contact

Bondo Pangaji - [bondopangaji@gmail.com](mailto:bondopangaji@gmail.com)

Project Link: [https://github.com/bondopangaji/spring-security-jwt](https://github.com/bondopangaji/spring-security-jwt)

<p align="right">(<a href="#top">back to top</a>)</p>



<!-- ACKNOWLEDGMENTS -->
## Acknowledgments

Helpful resources.

* [Amigoscode](https://amigoscode.com/)
* [Baeldung](https://www.baeldung.com)

<p align="right">(<a href="#top">back to top</a>)</p>



<!-- REFERENCE -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
