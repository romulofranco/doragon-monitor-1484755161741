Get started with ${app}
-----------------------------------
Welcome to Vaadin Rich Web Starter

This application demonstrates how to use the Vaadin UI Framework to build rich HTML5 applications, with the Liberty for Java runtime and DB2 database on IBM Cloud.

1. Download the Vaadin application source
    
        git clone https://hub.jazz.net/git/vaadin/vaadin-jpa-app

2. 'cd' into the downloaded package.

        cd vaadin-jpa-app

3. Compile the application

        mvn install

4. Connect and log into Bluemix:

        cf login -a ${api-url}

5. Deploy your app:

		cf push ${app} -p target/vaadin-jpa-application.war

7. Access your app: [http://${route}](http://${route})

Visit [https://hub.jazz.net/project/vaadin/vaadin-jpa-app/overview](https://hub.jazz.net/project/vaadin/vaadin-jpa-app/overview) for more information.
