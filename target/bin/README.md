# GDPR Purge

To comply with GDPR - General Data Protection Regulation - this tool helps to remove organisations from Zendesk while keeping the tickets metadata we need from the Zendesk cases.

This program requests tickets by org name from Zendesk does the following:
* [ ] Gets the metadata from all ZD tickets for the given org
* [ ] Stores the metadata in the Support DataWarehouse
* [ ] Deletes the tickets from the Organization
* [ ] Deletes users
* [ ] And, finally, deletes the organization itself

## Getting Started
You will need the StreamSets SupportLibrary which is in a StreamSets internal (private) repo:
* [StreamSets Support Libary](https://github.com/streamsets/SupportLibrary)

It is easiest to clone the Support Library repo first :

`git clone git@github.com:streamsets/SupportLibrary.git` 

Then cd into the directory and install the library into your maven repo with this command:

`mvn clean install`

## Running GDPRpurge
Once you have the Support Libary, you can download the GDPRPurge tool:

`git clone git@github.com:streamsets/support-GDPRpurge.git`

cd into the directory then:

`mvn clean package`

You will need AWS credentials, with the correctl roles, as the Zendesk Name and Password are pulled from AWS Secret Manager.
set these environment variables:

````shell
export SECRET_NAME=<secret>
export AWS_REGION=<region>
````

Configure conf/application.properties

Run the run.sh / run.bat file


### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.7.3/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.7.3/maven-plugin/reference/html/#build-image)

