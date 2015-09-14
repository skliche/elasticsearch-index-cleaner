# Introduction #
Elasticsearch provides a comprehensive documentation on index maintenance. A good article on that topic can be found on https://www.elastic.co/guide/en/elasticsearch/guide/current/retiring-data.html .

In order to perform these maintenance tasks on a regular basis, I created a tool that interacts with the elasticsearch server in order to perform index maintenance. Currently only the REST API is supported.

# Configuration #
After building using the 'mvn package' command, a jar and a sample config.yaml file is created. Edit the config.yaml file according to your needs and set the following variables:
- **protocol** the protocol (currently only http is supported)
- **hostname** the hostname of the elasticsearch server
- **port** the port  of the elasticsearch server
- **maxAgeToDelete** the number of days when an index can be deleted
- **maxAgeToArchive** the number of days when an index can be archived
- **maxAgeToOptimize** the number of days when an index shall be optimized (a good startingpoint is to optimize indices after 1 day)
- **dateFormat** the date pattern of your timestamps (e.g. yyyy.MM.dd)
- **indexpattern** - a list of indices to maintain

# Running #

In order to run the tool just execute **java -jar elasticsearch-index-cleaner-0.0.1-SNAPSHOT.jar**. A log file **cleaner.log** is created and gives further details on the execution of the tool.